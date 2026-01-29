package actors

import domain.DensityGrid
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object DisplayActor:
  def apply(): Behavior[DensityGrid] = Behaviors.receive { (ctx, msg) =>
    ctx.log.info(msg.density.toString())
    Behaviors.same
  }
