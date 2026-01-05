package domain

final case class Point(x: Double, y: Double):
  def distance(that: Point): Double =
    val dx = that.x - x
    val dy = that.y - y
    math.sqrt(dx*dx + dy*dy)
