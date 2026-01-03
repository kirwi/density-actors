package domain

final case class GridSpec(
  minX: Double,
  minY: Double,
  maxX: Double,
  maxY: Double,
  cellSize: Double
)

final case class Cell(ix: Int, iy: Int)

final case class Point(x: Double, y: Double)
