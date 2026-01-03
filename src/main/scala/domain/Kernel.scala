package domain

sealed trait Kernel

final case class Box(radius: Double) extends Kernel
final case class Gaussian(sigma: Double, cutoffSigmas: Double) extends Kernel
final case class Epanechnikov(bandwidth: Double) extends Kernel

def supportRadius(kernel: Kernel): Double = kernel match
  case Box(r) => r
  case Gaussian(s, c) => c * s
  case Epanechnikov(b) => b

/**
  weight should satisfy the following laws:
  - if d > supportRadius(k) then weight(k, d) == 0
  - weight(k, 0) is maximal
  - weight(k, d) >= 0
  - weight decreases quadratically for Epanechnikov
*/
def weight(kernel: Kernel, distance: Double): Double =
  val radius = supportRadius(kernel)
  if distance > radius then 0.0
  else kernel match
    case Box(_) => 1.0
    case Gaussian(s, _) =>
      math.exp(-(distance * distance) / (2.0 * s * s))
    case Epanechnikov(b) =>
      val u = distance / b
      0.75 * (1.0 - u * u)