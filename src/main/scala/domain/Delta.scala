package domain

import Kernel.{supportRadius, weight}

case class Delta(spec: GridSpec, sparseDensity: Vector[(Int, Double)])

object Delta:

  def eventDelta(event: Event, spec: GridSpec, kernel: Kernel): Delta =
    spec.cellOf(event.location) match
      case None => Delta(spec, Vector.empty)
      case _ =>
        val h = spec.cellSize
        val (nx, ny) = spec.gridDims
        val radius = supportRadius(kernel)
        val Point(x, y) = event.location
        val (xMinBox, xMaxBox) = (x - radius, x + radius)
        val (yMinBox, yMaxBox) = (y - radius, y + radius)
        // xMinBox <= minX + (ixMin + 0.5) * cellSize <= xMaxBox
        val ixMin = math.ceil((xMinBox - spec.minX) / h - 0.5).toInt
        val ixMax = math.floor((xMaxBox - spec.minX) / h - 0.5).toInt
        val iyMin = math.ceil((yMinBox - spec.minY) / h - 0.5).toInt
        val iyMax = math.floor((yMaxBox - spec.minY) / h - 0.5).toInt
        val (ixMinClamped, iyMinClamped) = (ixMin max 0, iyMin max 0)
        val (ixMaxClamped, iyMaxClamped) = (ixMax min (nx - 1), iyMax min (ny - 1))
        val ixs = List.range(ixMinClamped, ixMaxClamped + 1)
        val iys = List.range(iyMinClamped, iyMaxClamped + 1)
        val candidates = ixs.flatMap(x => iys.map(y => Cell(x, y)))
        val deltas: List[(Int, Double)] = candidates
          .map { cell =>
            val idx = spec.linearIndex(cell.ix, cell.iy)
            val distanceToCenter = event.location.distance(spec.centerOf(cell))
            (idx, distanceToCenter)
          }
          .filter((i, d) => d <= radius)
          .map((i, d) => (i, weight(kernel, d)))

        Delta(spec, deltas.toVector)
