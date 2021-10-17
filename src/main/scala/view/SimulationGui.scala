package view

import model.{Animal, FoodInstance, Habitat}

import javax.swing.WindowConstants
import scala.swing.{Dimension, Frame, SimpleSwingApplication}

/**
 * Class used to create the window where show the simulation.
 *
 * @param habitat the [[Habitat]] of the simulation.
 * @param simulationPanel the [[SimulationPanel]] where to draw all the element of the simulation (areas, animals and food).
 */
class SimulationGui(habitat: Habitat, val simulationPanel: SimulationPanel) extends SimpleSwingApplication {

  val (width, height) = habitat.dimensions

  override def top: Frame = new Frame {
    this.resizable = false
    this.size = new Dimension(width, height)
    this.title = "Simulation"
    contents = simulationPanel
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  }

  /**
   * Method use to delete the old element in the [[SimulationPanel]] and draw on the Panel the new state of the simulation.
   *
   * @param animals a Seq of [[Animal]] to draw on the [[SimulationPanel]].
   * @param food the food to draw on the [[SimulationPanel]].
   */
  def updatePanel(animals: Seq[Animal], food: Seq[FoodInstance]): Unit = {
    simulationPanel.peer.removeAll()
    simulationPanel.drawHabitat(habitat)
    simulationPanel.drawAnimals(animals)
    simulationPanel.drawFood(food)
    simulationPanel.revalidate()
    simulationPanel.repaint()
  }
}
