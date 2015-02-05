package core

import java.util.Date
import java.util.concurrent.atomic.AtomicLong

import akka.actor.{Props, ActorSystem}
import akka.stream.FlowMaterializer
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl._
import spray.http.{HttpData, HttpEntity}
import scala.concurrent.duration._
import scala.util._
import spray.json._

object Boot extends App {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  val twitterPublisherActor = system.actorOf(Props(new TwitterPublisherActor()))
  val twitterStreamActor = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri, twitterPublisherActor) with OAuthTwitterAuthorization))

  twitterStreamActor ! "the"

  implicit val materializer = FlowMaterializer()

  val errorCounterJson = new AtomicLong()
  val errorCounterFormat = new AtomicLong()


  val out = Sink.foreach[Tweet] { tweet =>
    println(tweet)
  }

  val src = Source(ActorPublisher[HttpData.NonEmpty](twitterPublisherActor))

  val materialized = FlowGraph { implicit builder =>
    import FlowGraphImplicits._

    val convert = Flow[HttpData.NonEmpty].map(e => {
        val json = e.asString.parseJson
        json.convertTo[Tweet]
    })

    src ~> convert ~> out
  }.run()

  materialized.get(out).onComplete {
    case Success(result) =>
      println("OK")
      System.exit(0)
    case Failure(ex) =>
      println(s"Error ${ex.getMessage()}")
      System.exit(1)
  }
}

