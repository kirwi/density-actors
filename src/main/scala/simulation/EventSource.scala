package simulation

import domain.Event

trait EventSource:
  def next(): Event
