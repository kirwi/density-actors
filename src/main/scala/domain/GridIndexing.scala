package domain

/**
 * Utility functions for finding the cell a query point lies
 * in, and finding the center of a given cell. They should
 * satisfy an invariant:
 *
 * cellOf(centerOf(cell)) == Some(cell)
 */

def cellOf(spec: GridSpec, p: Point): Option[Cell] =
  if p.x < spec.minX || p.x >= spec.maxX ||
    p.y < spec.minY || p.y >= spec.maxY
    then None
  else
    val ix = math.floor((p.x - spec.minX) / spec.cellSize).toInt
    val iy = math.floor((p.y - spec.minY) / spec.cellSize).toInt
    Some(Cell(ix, iy))

def centerOf(spec: GridSpec, c: Cell): Point =
  val x = spec.minX + (c.ix + 0.5) * spec.cellSize
  val y = spec.minY + (c.iy + 0.5) * spec.cellSize
  Point(x, y)
