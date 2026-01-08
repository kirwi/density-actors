package actors

import org.apache.pekko.actor.typed._
import org.apache.pekko.actor.typed.scaladsl._
import org.apache.pekko.actor.typed.ActorRef
import domain.{DensityGrid, Delta}

object DensityGridActors:

  sealed trait Command
  final case class ApplyDelta(delta: Delta) extends Command
  final case class GetSnapshot(replyTo: ActorRef[DensityGrid]) extends Command
  
  
