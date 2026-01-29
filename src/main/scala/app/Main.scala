package app

import org.apache.pekko.actor.typed.{ActorRef, ActorSystem, Behavior}
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import actors.{DensityCoordinator, DensityGridActor, DisplayActor, RootBehavior}
import actors.DensityGridActor.{ApplyDelta, GetSnapshot}
import domain.{Delta, DensityGrid, Event, Gaussian, GridSpec, Kernel}
import gmm.{BoundingBox, GMMGenerator, Generators}

import scala.util.Random
import org.scalacheck.Gen
import org.scalacheck.rng.Seed

object Main:
  def main(args: Array[String]): Unit =
    val params = Gen.Parameters.default
    val seed   = Seed(37L)
    val mixture =
      Generators.genGaussianMixture.pureApply(params, seed)

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

    val initialGrid = DensityGrid.zeros(grid)
    val kernel = Gaussian(sigma, 3.0)

    ActorSystem(
      RootBehavior(initialGrid, grid, kernel, gmm),
      "density-system"
    )
