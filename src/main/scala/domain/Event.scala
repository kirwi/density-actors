package domain

// At current Events wrap a Point. In practice, they would contain
// metadata about the event as well.
case class Event(location: Point)
