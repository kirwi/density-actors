package domain

import org.scalacheck.Properties
import org.scalacheck.Prop.{forAll, propBoolean}
import org.scalacheck.Gen
import generators.GridGen.{gridSpecGen, validIndices}

object GridSpecSpec extends Properties("GridSpec"):
  
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


