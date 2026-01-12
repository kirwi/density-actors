package gmm

import domain.Point
import scala.util.Random

final class GMMGenerator(mixture: GaussianMixture, rng: Random):

  private val components = mixture.components
  private val bounds = mixture.bounds
  
  private def chooseComponent(
    components: Vector[GaussianComponent],
    rng: Random
  ): GaussianComponent =
    val totalWeight = components.map(_.weight).sum
    val u = rng.nextDouble() * totalWeight

    @annotation.tailrec
    def go(i: Int, acc: Double): GaussianComponent =
      val next = acc + components(i).weight
      if u <= next then components(i)
      else go(i + 1, next)

    go(0, 0.0)

  private def sampleGaussian(
    comp: GaussianComponent,
    rng: Random
  ): Point =
    val x = rng.nextGaussian() * comp.sigma + comp.mean.x
    val y = rng.nextGaussian() * comp.sigma + comp.mean.y
    Point(x, y)

  @annotation.tailrec
  private def sampleWithinBounds(
    comp: GaussianComponent,
    box: BoundingBox,
    rng: Random
  ): Point =
    val p = sampleGaussian(comp, rng)
    if box.contains(p) then p
    else sampleWithinBounds(comp, box, rng)

  def sample(): Point =
    val comp = chooseComponent(components, rng)
    sampleWithinBounds(comp, bounds, rng)

  def samples(n: Int): Vector[Point] =
    Vector.fill(n)(sample())