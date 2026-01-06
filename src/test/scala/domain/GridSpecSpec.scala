package domain

import org.scalacheck.Properties
import org.scalacheck.Prop.{forAll, propBoolean}
import org.scalacheck.Gen

object GridSpecSpec extends Properties("GridSpec"):

  val gridSpecGen: Gen[GridSpec] =
    for
      nx <- Gen.choose(1, 200)
      ny <- Gen.choose(1, 200)
      cellSize <- Gen.choose(1.0e-3, 1.0)
      xMin <- Gen.choose(-10.0, 10.0)
      yMin <- Gen.choose(-10.0, 10.0)
    yield GridSpec(
      xMin,
      yMin,
      xMin + nx * cellSize,
      yMin + ny * cellSize,
      cellSize
    )

  def validIndices(nx: Int, ny: Int): Seq[(Int, Int)] =
    val xs = 0 until nx
    val ys = 0 until ny
    xs.flatMap(x => ys.map(y => (x, y)))


  property("Grid dimensions are non-negative") = forAll(gridSpecGen) {
    (gs: GridSpec) =>
      val (nx, ny) = gs.gridDims
      nx >= 0 && ny >= 0
  }

  property("Cell size is congruent with grid dimensions") = forAll(gridSpecGen) {
    (gs: GridSpec) =>
      val (nx, ny) = gs.gridDims
      nx * gs.cellSize <= gs.maxX - gs.minX &&
      ny * gs.cellSize <= gs.maxY - gs.minY
  }

  property("Linear index stays in bounds") = forAll(gridSpecGen) {
    (gs: GridSpec) =>
      val (nx, ny) = gs.gridDims
      val indices = validIndices(nx, ny)
      indices.nonEmpty ==> {
        forAll(Gen.oneOf(indices)) {
          case (ix, iy) =>
            val linearIdx = gs.linearIndex(ix, iy)
            linearIdx >= 0 && linearIdx < nx * ny
        }
      }

  }

  property("Linear index is unique") = forAll(gridSpecGen) {
    (gs: GridSpec) =>
      val (nx, ny) = gs.gridDims
      val indices = validIndices(nx, ny)
      indices.size >=2 ==> {
        forAll(Gen.pick(2, indices)) {
          picked =>
            val List((ix1, iy1), (ix2, iy2)) = picked.toList
            gs.linearIndex(ix1, iy1) != gs.linearIndex(ix2, iy2)
        }
      }
  }


