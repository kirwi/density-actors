package gmm

import domain.Point

case class GaussianComponent(
  mean: Point,
  sigma: Double,
  weight: Double
)
