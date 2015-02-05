package core

import akka.actor._
import akka.stream.actor.ActorPublisher
import spray.http._

class TwitterPublisherActor extends ActorPublisher[HttpData.NonEmpty] with Actor {
  var count = 0
  var dropped = 0

  def receive = {
    case e: HttpData.NonEmpty =>
      count += 1
      if (count % 100 == 0) println(s"ingested: $count, dropped: $dropped")
      if (isActive && totalDemand > 0)
        onNext(e)
      else
        dropped += 1
  }

}
