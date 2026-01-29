package actors

import domain.{DensityGrid, GridSpec, Kernel}
import gmm.GMMGenerator
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import simulation.GMMEventSource

object RootBehavior:

  def apply(
    initialGrid: DensityGrid,
    grid: GridSpec,
    kernel: Kernel,
    gmm: GMMGenerator
  ): Behavior[Nothing] =
    Behaviors.setup[Nothing] { ctx =>
      val coordinator =
        ctx.spawn(
          DensityCoordinator(initialGrid, grid, kernel),
          "coordinator"
        )

      val source = new GMMEventSource(gmm)

      ctx.spawn(
        IngestionActor(source, coordinator),
        "ingestion"
      )

      // Optional: request a snapshot once after startup
      val display =
        ctx.spawn(DisplayActor(), "display")

      coordinator ! DensityCoordinator.GetSnapshot(display)

      Behaviors.empty
    }