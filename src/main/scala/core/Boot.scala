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

  implicit val materializer = FlowMaterializer()

  val materialized = FlowGraph { implicit builder =>
    import FlowGraphImplicits._

  }.run()

  materialized.get(null).onComplete {
    case Success(result) =>
      println("OK")
      System.exit(0)
    case Failure(ex) =>
      println(s"Error ${ex.getMessage()}")
      System.exit(1)
  }
}

