package simulation

import domain.{Point, Event}

final class GaussianEventSource(
  center: Point,
  sigma: Double,
  rng: scala.util.Random
) extends EventSource:

  def next(): Event =
    val x = center.x + rng.nextGaussian() * sigma
    val y = center.y + rng.nextGaussian() * sigma
    Event(Point(x, y))

