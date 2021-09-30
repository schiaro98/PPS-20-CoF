package controller

import model.Habitat

sealed trait SimulationController {

}

object SimulationController {
  def apply(habitat: Habitat): SimulationController = SimpleSimulationController(habitat)
}

private case class SimpleSimulationController(habitat: Habitat) extends SimulationController {

}
