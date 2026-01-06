package domain

import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import generators.GridGen.gridSpecGen
import generators.PointGen.gridPointGen

object CellSpec extends Properties("Cell"):

  property("Cell indices are non-negative") =
    forAll(gridSpecGen) { (gs: GridSpec) =>
      forAll(gridPointGen(gs)) { point =>
        val cellOpt = gs.cellOf(point)
        cellOpt.isDefined && {
          val Cell(ix, iy) = gs.cellOf(point).get
          ix >=0 && iy >= 0
        }
    }
  }