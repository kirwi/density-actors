package app

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import actors.DensityGridActor
import actors.DensityGridActor.ApplyDelta
import domain.{Delta, DensityGrid, Event, Gaussian, GridSpec}
import gmm.{BoundingBox, GMMGenerator, Generators}

import scala.util.Random
import org.scalacheck.Gen
import org.scalacheck.rng.Seed

object Main:

  def main(args: Array[String]): Unit =
    // Mock historical data params from a previous pipeline step
    val params = Gen.Parameters.default
    val seed   = Seed(37L)
    val mixture = Generators.genGaussianMixture
      .pureApply(params, seed)
    val gmm = GMMGenerator(mixture, new Random(37))

    val BoundingBox(xMin, xMax, yMin, yMax) = mixture.bounds
    val sigma = mixture.components.head.sigma
    val grid = GridSpec(
      xMin,
      yMin,
      xMax,
      yMax,
      sigma / 3.0
    )
    val initialGrid: DensityGrid = DensityGrid.zeros(grid)

    // Start the system and compute initial density from historical data
    val system =
      ActorSystem(
        Behaviors.empty,
        "density-system"
      )

    val gridActor =
      system.systemActorOf(
        DensityGridActor(initialGrid),
        "density-grid"
      )

    gmm.samples(1000).foreach { point =>
      val event = Event(point)
      val kernel = Gaussian(sigma, 3.0)
      val delta = Delta.eventDelta(Event(point), grid, kernel)
      gridActor ! ApplyDelta(delta)
    }
