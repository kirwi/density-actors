# Concurrent Spatial Density Estimation with Actors

## Overview
This is an evolving project exploring the use of actor-based
architecture to perform incremental kernel density estimation
over a fixed spatial grid. The system is designed to accept
a stream of update events, accumulate density values at 
grid-centers, and provide snapshots of the resulting density
field for downstream processing (contour plots, GeoJSON 
representation, etc.)

## Motivation and Design Goals
The project began as a learning exercise to marry the more
unfamiliar actor model to the familiar kernel density
estimation problem. It has intentionally been kept simple
with closed cases of kernel variants (rather than type classes),
putting focus, instead, on
- incremental updates rather than the more familiar
batch update schemes one would use with something like Spark
- clear ownership of mutable state
- predictable concurrency semantics
- separation between the kde numerical model and coordination model.


## High-Level Architecture
There is currently only one main actor. DensityGridActor 
receives incremental updates to the evolving density and 
returns snapshots of the current density field. Future iterations
will see the implementation of various other actors responsible
for computing density contours, constructing GeoJSON representations
for ingestion by a frontend UI, etc.

## Data Model
`GridSpec` is a grid specification representing the real-space
grid bounds and cell sizing.
`DensityGrid` is an immutable representation of a density field
calculated at the centers of the real-space grid. The density
values are stored in a dense 1D Scala Vector for quick access.
`Delta` is a representation of an incremental update to the
density grid. It's density updates are held in a sparse Scala
Vector and used to update the density via message passing.
`Cell` represents a single cell of the grid in index space
`Point` represents an (x, y) point in real 2D space.
`Event` contains a location point and any other metadata about
an even that has occurred.
`Kernel` is currently a sealed Scala trait with three variations:
`Box`, `Gaussian`, and `Epanechnikov`. These contain methods for
computing the contribution to the incremental density updates.

## Concurrency and State Model


## Snapshot Semantics
## Numerical Context (Brief)
## What This Project Demonstrates
## Future Directions
