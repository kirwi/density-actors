package domain

import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import generators.GridGen.gridSpecGen
import generators.PointGen.*

object CellSpec extends Properties("Cell"):

  property("Cell indices are non-negative") =
    forAll(gridSpecGen) { (gs: GridSpec) =>
      forAll(gridPointAndCellGen(gs)) { case (_, Cell(ix, iy)) =>
          ix >=0 && iy >= 0
      }
    }

  property("Points interior to the grid map to a cell") =
    forAll(gridSpecGen) { gs =>
      forAll(gridPointGen(gs)) { p =>
        gs.cellOf(p).isDefined
      }
    }

  property("Points inside a cell map to that cell") =
    forAll(gridSpecGen) { gs =>
      forAll(cellAndInteriorPointGen(gs)) { case (p, c) =>
        gs.cellOf(p).contains(c)
      }
    }

  property("Cell centers map back to the same cell") =
    forAll(gridSpecGen) { gs =>
      forAll(validCellGen(gs)) { c =>
        gs.cellOf(gs.centerOf(c)).contains(c)
      }
    }