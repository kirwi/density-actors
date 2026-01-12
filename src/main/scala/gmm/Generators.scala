package gmm

import org.scalacheck.Gen
import domain.Point

object Generators:
  
  val genBoundingBox: Gen[BoundingBox] =
    for
      cx <- Gen.choose(-1000.0, 1000.0)
      cy <- Gen.choose(-1000.0, 1000.0)
      w <- Gen.choose(10.0, 500.0)
      h <- Gen.choose(10.0, 500.0)
    yield BoundingBox(
      cx - w,
      cx + w,
      cy - h,
      cy + h
    )
    
  def genPointIn(box: BoundingBox): Gen[Point] =
    for
      x <- Gen.choose(box.xMin, box.xMax)
      y <- Gen.choose(box.yMin, box.yMax)
    yield
      Point(x, y)

  def genSigma(box: BoundingBox): Gen[Double] =
    val w = box.xMax - box.xMin
    val h = box.yMax - box.yMin
    val scale = w min h
    Gen.choose(0.01 * scale, 0.1 * scale)
    
  def genComponent(box: BoundingBox): Gen[GaussianComponent] =
    for
      mean <- genPointIn(box)
      sigma <- genSigma(box)
      weight <- Gen.choose(0.1, 1.0)
    yield
      GaussianComponent(mean, sigma, weight)

  val genGaussianMixture: Gen[GaussianMixture] =
    for
      box <- genBoundingBox
      n <- Gen.choose(1, 5)
      comps <- Gen.listOfN(n, genComponent(box))
    yield GaussianMixture(comps.toVector, box)