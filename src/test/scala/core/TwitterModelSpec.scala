package core

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.specs2.mutable.{Specification, SpecificationLike}
import spray.http.Uri

import scala.io.Source
import spray.json._

class TwitterModelSpec extends Specification {
  "twitter model" should {
    "decode json" in {
      val body = Source.fromInputStream(getClass.getResourceAsStream("/tweet.json")).mkString

      body.parseJson.convertTo[Tweet] // no exception
      1 mustEqual 1
    }
  }
}
