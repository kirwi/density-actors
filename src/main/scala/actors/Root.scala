package actors

import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import domain.DensityGrid

object Root:

  def apply(initialGrid: DensityGrid): Behavior[Nothing] =
    Behaviors.setup[Nothing] { context =>
      // Spawn the DensityGridActor
      val gridActor: ActorRef[DensityGridActor.Command] =
        context.spawn(
          DensityGridActor(initialGrid),
          "density-grid"
        )

      // Nothing else to do (yet)
      Behaviors.empty
    }
