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

  twitterStreamActor ! "tomcat"

  implicit val materializer = FlowMaterializer()

  val errorCounterJson = new AtomicLong()
  val errorCounterFormat = new AtomicLong()


  val out = Sink.foreach[(Long, Seq[Tweet])] { case (timestamp, event) =>
    println(event(0).user.url)
    println((errorCounterJson.get(), errorCounterFormat.get()))
    println((new Date(timestamp), event.size))
  }

  val src = Source(ActorPublisher[HttpData.NonEmpty](twitterPublisherActor))

  val materialized = FlowGraph { implicit builder =>
    import FlowGraphImplicits._

    val validateJson = Flow[HttpData.NonEmpty].filter(e => {
      try {
        val json = e.asString.parseJson
        try {
          json.convertTo[Tweet]
          true
        } catch {
          case ex: Exception =>
            //println(json)
            errorCounterFormat.incrementAndGet()
            false
        }
      } catch {
        case ex: Exception =>
          errorCounterJson.incrementAndGet()
          false
      }
    })
    val convert = Flow[HttpData.NonEmpty].map(e => {
      e.asString.parseJson.convertTo[Tweet]
    })
    val count = Flow[Tweet].groupedWithin(10000, 5.seconds).map(tweets => (System.currentTimeMillis(), tweets))

    src ~> validateJson ~> convert ~> count ~> out
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

