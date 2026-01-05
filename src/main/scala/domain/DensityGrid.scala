package domain

case class DensityGrid(spec: GridSpec, density: Vector[Double]):
  def applyDelta(delta: Delta): DensityGrid =
    if delta.spec != spec || delta.sparseDensity.isEmpty
      then this
    else
      val buffer = density.toArray
      for ((idx, weight) <- delta.sparseDensity)
        buffer(idx) += weight
        
      DensityGrid(spec, buffer.toVector)