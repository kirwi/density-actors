package domain.generators

import org.scalacheck.Gen
import domain.GridSpec
import domain.Point

object PointGen:

  def gridPointGen(gs: GridSpec): Gen[Point] =
    for
      px <- Gen.choose(gs.minX, gs.maxX)
      py <- Gen.choose(gs.minY, gs.maxY)
    yield Point(px, py) 