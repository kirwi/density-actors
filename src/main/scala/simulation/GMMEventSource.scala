package simulation

import gmm.GMMGenerator
import domain.Event

final class GMMEventSource(gmm: GMMGenerator) extends EventSource:

  def next(): Event =
    Event(gmm.sample())

