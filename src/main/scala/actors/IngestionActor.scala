package actors

import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import simulation.EventSource

import scala.concurrent.duration.*

object IngestionActor:

  sealed trait Command
  case object Tick extends Command

  def apply(
    source: EventSource,
    target: ActorRef[DensityCoordinator.Command]
  ): Behavior[Command] =
    Behaviors.withTimers { timers =>
      timers.startTimerAtFixedRate(Tick, 200.millis)

      Behaviors.receiveMessage {
        case Tick =>
          target ! DensityCoordinator.Ingest(source.next())
          Behaviors.same
      }
    }