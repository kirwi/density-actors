package domain.generators

import org.scalacheck.Gen
import domain.GridSpec
import domain.Point
import domain.Cell

object PointGen:

  def gridPointGen(gs: GridSpec): Gen[Point] =
    // Generate px <- [gs.minX, gs.maxX), py <- [gs.minY, gs.maxY)
    val maxX = math.nextDown(gs.maxX)
    val maxY = math.nextDown(gs.maxY)
    for
      px <- Gen.choose(gs.minX, maxX)
      py <- Gen.choose(gs.minY, maxY)
    yield Point(px, py)

  def gridPointAndCellGen(gs: GridSpec): Gen[(Point, Cell)] =
    gridPointGen(gs).flatMap { p =>
      gs.cellOf(p) match
        case Some(c) => Gen.const((p, c))
        case None => Gen.fail
    }

  def validCellGen(gs: GridSpec): Gen[Cell] =
    val (nx, ny) = gs.gridDims
    val xs = 0 until nx
    val ys = 0 until ny
    if nx <= 0 || ny <= 0 then Gen.fail
    else
      Gen.oneOf(xs.flatMap(x => ys.map(y => Cell(x, y))))

  def cellAndInteriorPointGen(gs: GridSpec): Gen[(Point, Cell)] =
    validCellGen(gs).flatMap { c =>
      Gen.choose(0.0, math.nextDown(1.0)).flatMap { dx =>
        Gen.choose(0.0, math.nextDown(1.0)).map { dy =>
          val px = gs.minX + (c.ix + dx) * gs.cellSize
          val py = gs.minY + (c.iy + dy) * gs.cellSize
          (Point(px, py), c)
        }
      }
    }