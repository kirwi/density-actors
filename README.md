# Concurrent Spatial Density Estimation with Actors

## Overview
This project explores the use of an actor-based architecture to 
perform incremental kernel density estimation over a fixed spatial 
grid. The system is designed to accept a stream of update events, 
accumulate density values at grid-centers, and provide snapshots 
of the resulting density field for downstream processing 
(contour plots, GeoJSON representation, etc.)

## Motivation and Design Goals
The project began as a learning exercise to marry the more
unfamiliar actor model to the familiar kernel density
estimation problem. It has intentionally been kept simple
with a closed set of kernel variants, intentionally avoiding 
additional abstraction layers to keep the focus on 
coordination and state management. It features:

- incremental updates rather than batch update schemes (Spark)
- clear ownership of mutable state
- predictable concurrency semantics
- separation between the kde numerical model and coordination model.


## High-Level Architecture
There is currently only one main actor, `DensityGridActor`, that 
receives incremental updates to the evolving density and 
returns snapshots of the current density field. 

## Data Model
`GridSpec` is a grid specification representing the real-space
grid bounds and cell sizing.

`DensityGrid` is an immutable representation of a density field
calculated at the centers of the real-space grid. The density
values are stored in a dense 1D `Vector` for quick access.

`Delta` is a representation of an incremental update to the
density grid. Its density updates are held in a sparse
`Vector` and used to update the density via message passing.

`Cell` represents a single cell of the grid in index space.

`Point` represents an (x, y) point in real 2D space.

`Event` contains a location point and any other metadata about
an event that has occurred.

`Kernel` is currently a sealed Scala trait with three variations:
`Box`, `Gaussian`, and `Epanechnikov`. These contain methods for
computing the contribution to the incremental density updates.

## Concurrency and State Model
All mutable state in the system is owned by a single long-lived 
actor, DensityGridActor. This actor is responsible for 
maintaining the current DensityGrid and applying incoming Delta 
updates sequentially via message passing.

Because actors process messages one at a time, updates to the 
density field are serialized without requiring explicit locking 
or shared mutable state. Each update results in the creation of 
a new immutable DensityGrid instance, which replaces the actor’s 
internal reference.

## Snapshot Semantics
Snapshots represent point-in-time views of the accumulated 
density field. A snapshot request is handled by the same actor 
that processes update messages, ensuring that the returned 
DensityGrid reflects all updates processed prior to 
the snapshot request.

Snapshot retrieval does not block the processing of future 
updates, but it does provide a consistent view of the density 
field suitable for downstream consumers such as contour 
extraction or visualization.

## Numerical Context
Kernel density estimation is performed over a fixed spatial 
grid, with density values accumulated at grid cell centers. Each 
incoming event contributes to a sparse `Delta` based on the 
selected kernel function, which is then applied to the current 
`DensityGrid`.

The numerical aspects of the project are intentionally kept 
simple; the primary focus is on the coordination, evolution, 
and consistency of the density scalar field over time.

## What This Project Demonstrates
This project demonstrates:

- Actor-based concurrency and message-driven system design in Scala
- Modeling evolving system state using immutable data structures
- Reasoning about snapshot consistency in a concurrent system
- Separation of numerical computation from coordination concerns
- Pragmatic tradeoffs between abstraction, correctness, and complexity

## Future Directions
Future iterations will see the implementation of various other actors 
responsible for computing density contours, constructing GeoJSON 
representations for ingestion by a frontend UI, etc. Rather than coupling 
HTTP concerns directly into the actor system, this would be implemented 
as a thin adapter responsible for:

- requesting density snapshots,
- transforming them into GeoJSON representations,
- and exposing them via a simple HTTP API for consumption by a Leaflet-based UI.
