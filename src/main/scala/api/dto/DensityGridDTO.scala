package api.dto

case class DensityGridDTO(
  bounds: BoundsDTO,
  cellSize: Double,
  nx: Int,
  ny: Int,
  values: Vector[Double],
  min: Double,
  max: Double
)
