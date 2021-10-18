package view

import model.{Animal, FoodInstance, Habitat}
import utility.Logger

import javax.swing.WindowConstants
import scala.swing.BorderPanel.Position._
import scala.swing._

/**
 * Class used to create the window where show the simulation.
 *
 * @param habitat the [[Habitat]] of the simulation.
 * @param simulationPanel the [[SimulationPanel]] where to draw all the element of the simulation (areas, animals and food).
 */
class SimulationGui(habitat: Habitat, val simulationPanel: SimulationPanel) extends SimpleSwingApplication {

  var elapsedTime = 0
  val (width, height) = habitat.dimensions
  val textArea: TextArea = new TextArea("Welcome to Circe of Life Simulator!", 10, 10){
    editable = false
  }

  val elapsedTimeLabel = new Label("timeElapsed: ")
  val elapsedTimeField: TextField = new TextField(elapsedTime.toString){
    editable = false
  }

  override def top: Frame = new Frame {
    this.resizable = false
    this.size = new Dimension(width, height)
    this.title = "Simulation"

    val logPanel: BoxPanel =  new BoxPanel(Orientation.Vertical) {
      contents addAll List(elapsedTimeField, elapsedTimeField, textArea)
    }

    contents = new BorderPanel(){
      layout(simulationPanel) = North
      layout(logPanel) = South
    }

    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  }

  /**
   * Method use to delete the old element in the [[SimulationPanel]] and draw on the Panel the new state of the simulation.
   *
   * @param animals a Seq of [[Animal]] to draw on the [[SimulationPanel]].
   * @param food the food to draw on the [[SimulationPanel]].
   */
  def updatePanel(animals: Seq[Animal], food: Seq[FoodInstance]): Unit = {
    updateLogger()
    simulationPanel.peer.removeAll()
    simulationPanel.drawHabitat(habitat)
    simulationPanel.drawAnimals(animals)
    simulationPanel.drawFood(food)
    simulationPanel.revalidate()
    simulationPanel.repaint()
  }

  def updateLogger() : Unit = textArea.text = Logger.history.takeRight(10).mkString("\n")

  def updateElapsedTime(): Unit = {
    elapsedTime += 1
    elapsedTimeField.text = elapsedTime.toString
  }

}
