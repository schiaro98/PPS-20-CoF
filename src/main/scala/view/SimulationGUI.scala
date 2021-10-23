package view

import model.animal.Animal
import model.food.Food
import model.habitat.Habitat
import utility.{Logger, Statistics}

import java.awt.Dimension
import javax.swing.{ImageIcon, WindowConstants}
import scala.reflect.io.{File, Path}
import scala.swing.BorderPanel.Position._
import scala.swing._
import scala.swing.event.ButtonClicked

/**
 * Class used to create the window where show the simulation.
 *
 * @param habitat         the [[Habitat]] of the simulation.
 * @param setPaused       handler method used to pause/unpause the simulation.
 * @param setSpeed        handler method used to speed up/slow down the simulation.
 * @param stop            handler method used to stop the simulation.
 */
class SimulationGUI(habitat: Habitat,
                    setPaused: Boolean => Unit,
                    setSpeed: Boolean => Unit,
                    stop: () => Unit) extends SimpleSwingApplication {

  val (width, height) = habitat.dimensions
  val simulationPanel = new SimulationPanel(habitat.dimensions)
  val textArea: TextArea = new TextArea("", 10, 10) { editable = false }
  val elapsedTimeLabel: Label = new Label("Time elapsed:")
  val elapsedTimeField: TextField = new TextField(Statistics.time.toString) { editable = false }
  val buttons: Seq[Button] = SimulationButton(setPaused, setSpeed, stop).buttons

  override def top: Frame = new Frame {
    this.resizable = false
    this.size = new Dimension(width, height)
    this.title = "Simulation"

    val logPanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
      contents addAll (List(elapsedTimeLabel, elapsedTimeField) appendedAll buttons)
    }
    contents = new BorderPanel() {
      layout(simulationPanel) = North
      layout(logPanel) = Center
      layout(textArea) = South
    }
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  }

  /**
   * Method use to delete the old element in the [[SimulationPanel]] and draw on the Panel the new state of the simulation.
   *
   * @param animals a Seq of [[Animal]] to draw on the [[SimulationPanel]].
   * @param food    the food to draw on the [[SimulationPanel]].
   */
  def updatePanel(animals: Seq[Animal], food: Seq[Food]): Unit = {
    simulationPanel.drawAll(habitat, animals, food)
    simulationPanel.revalidate()
    simulationPanel.repaint()
    updateLogger()
  }

  /**
   * Method to increment the counter of the elapsed time.
   */
  def updateElapsedTime(): Unit = {
    elapsedTimeField.text = Statistics.time.toString
  }

  /**
   * Disable all the button of the GUI (play, pause, speed down, speed up and stop).
   */
  def disableAllButton(): Unit = buttons.foreach(button => button.enabled = false)

  /**
   * Method to update the text in the [[TextArea]] with the latest events recorded in the logger.
   */
  private def updateLogger(): Unit = textArea.text = Logger.history.takeRight(10).mkString("\n")
}

/**
 * Class used to wrap the code of the creation of the button used to handle the simulation.
 *
 * @param setPaused handler method used to pause/unpause the simulation.
 * @param setSpeed  handler method used to speed up/slow down the simulation.
 * @param stop      handler method used to stop the simulation.
 */
case class SimulationButton(setPaused: Boolean => Unit, setSpeed: Boolean => Unit, stop: () => Unit) {

  //TODO mettere File.separator

  val playButton: Button = new Button() {
    icon = getImage("play-button.png")
    this.enabled = false
    reactions += {
      case _: ButtonClicked =>
        setPaused(false)
        this.enabled = false
        pauseButton.enabled = true
    }
  }
  val pauseButton: Button = new Button() {
    icon = getImage("pause.png")
    reactions += {
      case _: ButtonClicked =>
        setPaused(true)
        this.enabled = false
        playButton.enabled = true
    }
  }
  val speedDownButton: Button = new Button() {
    icon = getImage("rewind.png")
    this.enabled = false
    reactions += {
      case _: ButtonClicked =>
        setSpeed(false)
        this.enabled = false
        speedUpButton.enabled = true
    }
  }
  val speedUpButton: Button = new Button() {
    icon = getImage("fast-forward.png")
    reactions += {
      case _: ButtonClicked =>
        setSpeed(true)
        this.enabled = false
        speedDownButton.enabled = true
    }
  }
  val stopButton: Button = new Button() {
    icon = getImage("stop.png")
    reactions += {
      case _: ButtonClicked => stop()
    }
  }

  def getImage(fileName: String):ImageIcon = {
    val path = Path("src"+File.separator+"main"+File.separator+"resources"+File.separator+"images"+File.separator+fileName)
    val file = if (File(path).isFile){
      new ImageIcon(path.path)
    } else {
      new javax.swing.ImageIcon(getClass.getResource(File.separator+"images"+File.separator+fileName))
    }
    file
  }

  def buttons = Seq(playButton, pauseButton, speedDownButton, speedUpButton, stopButton)
}
