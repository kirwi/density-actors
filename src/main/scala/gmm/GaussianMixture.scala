package gmm

case class GaussianMixture(
  components: Vector[GaussianComponent],
  bounds: BoundingBox
)
  
