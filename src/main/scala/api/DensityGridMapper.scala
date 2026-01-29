package api

import domain.DensityGrid
import dto.{BoundsDTO, DensityGridDTO}

object DensityGridMapper:

  def toDTO(grid: DensityGrid): DensityGridDTO =
    val spec = grid.spec
    val values = grid.density

    DensityGridDTO(
      bounds = BoundsDTO(
        spec.minX,
        spec.maxX,
        spec.minY,
        spec.maxY
      ),
      cellSize = spec.cellSize,
      nx = spec.gridDims._1,
      ny = spec.gridDims._2,
      values = values,
      min = values.min,
      max = values.max
    )
