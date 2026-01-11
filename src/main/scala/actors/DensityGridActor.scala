package actors

import org.apache.pekko.actor.typed.ActorRef
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import domain.{DensityGrid, Delta}

object DensityGridActor:

  sealed trait Command
  final case class ApplyDelta(delta: Delta) extends Command
  final case class GetSnapshot(replyTo: ActorRef[DensityGrid]) extends Command

  private def behavior(grid: DensityGrid): Behavior[Command] =
    Behaviors.receiveMessage {
      case ApplyDelta(d) =>
        val nextGrid = grid.applyDelta(d)
        behavior(nextGrid)

      case GetSnapshot(r) =>
        r ! grid
        behavior(grid)
    }

  def apply(initial: DensityGrid): Behavior[Command] =
    behavior(initial)
  
