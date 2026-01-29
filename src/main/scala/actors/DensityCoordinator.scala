package actors

import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import domain.{DensityGrid, Delta, Event, GridSpec, Kernel}

object DensityCoordinator:

  sealed trait Command
  final case class Ingest(event: Event) extends Command
  final case class GetSnapshot(replyTo: ActorRef[DensityGrid]) extends Command

  def apply(
    initial: DensityGrid,
    spec: GridSpec,
    kernel: Kernel
  ): Behavior[Command] =
    Behaviors.setup { ctx =>
      val gridActor =
        ctx.spawn(DensityGridActor(initial), "grid")

      Behaviors.receiveMessage {
        case Ingest(event) =>
          val delta = Delta.eventDelta(event, spec, kernel)
          gridActor ! DensityGridActor.ApplyDelta(delta)
          Behaviors.same

        case GetSnapshot(r) =>
          gridActor ! DensityGridActor.GetSnapshot(r)
          Behaviors.same
      }
    }