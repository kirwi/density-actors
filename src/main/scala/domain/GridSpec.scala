package domain

final case class GridSpec(
  minX: Double,
  minY: Double,
  maxX: Double,
  maxY: Double,
  cellSize: Double
):
  
  def gridDims: (Int, Int) =
    val nx = math.floor((maxX - minX) / cellSize).toInt
    val ny = math.floor((maxY - minY) / cellSize).toInt
    (nx, ny)
    
  def linearIndex(ix: Int, iy: Int): Int =
    val (nx, ny) = gridDims
    iy * nx + ix
  
  def cellOf(p: Point): Option[Cell] =
    if p.x < minX || p.x >= maxX ||
      p.y < minY || p.y >= maxY
    then None
    else
      val ix = math.floor((p.x - minX) / cellSize).toInt
      val iy = math.floor((p.y - minY) / cellSize).toInt
      Some(Cell(ix, iy))

  def centerOf(c: Cell): Point =
    val x = minX + (c.ix + 0.5) * cellSize
    val y = minY + (c.iy + 0.5) * cellSize
    Point(x, y)