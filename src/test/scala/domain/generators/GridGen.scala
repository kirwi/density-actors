package domain.generators

import org.scalacheck.Gen
import domain.GridSpec

object GridGen:

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
      math.nextUp(xMin + nx * cellSize),
      math.nextUp(yMin + ny * cellSize),
      cellSize
    )

  def validIndices(nx: Int, ny: Int): Seq[(Int, Int)] =
    val xs = 0 until nx
    val ys = 0 until ny
    xs.flatMap(x => ys.map(y => (x, y)))
