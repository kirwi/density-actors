package gmm

import domain.Point

case class BoundingBox(
  xMin: Double,
  xMax: Double,
  yMin: Double,
  yMax: Double
):
  def contains(point: Point): Boolean =
    val Point(x, y) = point
    x <= xMax && x >= xMin && y <= yMax && y >= yMin