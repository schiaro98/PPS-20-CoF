package view

import model.{Animal, FoodInstance, Habitat}
import utility.{Logger, Statistics}

import java.awt.Dimension
import javax.swing.{ImageIcon, WindowConstants}
import scala.swing.BorderPanel.Position._
import scala.swing._
import scala.swing.event.ButtonClicked

/**
 * Class used to create the window where show the simulation.
 *
 * @param habitat         the [[Habitat]] of the simulation.
 * @param simulationPanel the [[SimulationPanel]] where all the element of the simulation (areas, animals and food) can be drawn.
 * @param setPaused       handler method used to pause/unpause the simulation.
 * @param setSpeed        handler method used to speed up/slow down the simulation.
 * @param stop            handler method used to stop the simulation.
 */
class SimulationGui(habitat: Habitat,
                    simulationPanel: SimulationPanel,
                    setPaused: Boolean => Unit,
                    setSpeed: Boolean => Unit,
                    stop: () => Unit,
                   ) extends SimpleSwingApplication {
  val (width, height) = habitat.dimensions
  val textArea: TextArea = new TextArea("Welcome to Circe of Life Simulator!", 10, 10) { editable = false }

  val elapsedTimeLabel: Label = new Label("    Time elapsed:    ")
  val elapsedTimeField: TextField = new TextField(Statistics.time().toString) { editable = false }
  val playButton: Button = new Button() {
    icon = new ImageIcon("res/images/play-button.png")
    reactions += {
      case _: ButtonClicked => setPaused(false)
    }
  }
  val pauseButton: Button = new Button() {
    icon = new ImageIcon("res/images/pause.png")
    reactions += {
      case _: ButtonClicked => setPaused(true)
    }
  }
  val speedDownButton: Button = new Button() {
    icon = new ImageIcon("res/images/rewind.png")
    reactions += {
      case _: ButtonClicked => setSpeed(false)
    }
  }
  val speedUpButton: Button = new Button() {
    icon = new ImageIcon("res/images/fast-forward.png")
    reactions += {
      case _: ButtonClicked => setSpeed(true)
    }
  }
  val stopButton: Button = new Button() {
    icon = new ImageIcon("res/images/stop.png")
    reactions += {
      case _: ButtonClicked => stop()
    }
  }

  override def top: Frame = new Frame {
    this.resizable = false
    this.size = new Dimension(width, height)
    this.title = "Simulation"

    val logPanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
      contents addAll List(elapsedTimeLabel, elapsedTimeField, playButton, pauseButton, speedDownButton, speedUpButton, stopButton)
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
  def updatePanel(animals: Seq[Animal], food: Seq[FoodInstance]): Unit = {
    simulationPanel.drawAll(habitat, animals, food)
    simulationPanel.revalidate()
    simulationPanel.repaint()
    updateLogger()
  }

  /**
   * Method to increment the counter of the elapsed time.
   */
  def updateElapsedTime(): Unit = {
    Statistics.incTime()
    elapsedTimeField.text = Statistics.time().toString
  }

  /**
   * Method to update the text in the [[TextArea]] with the latest events recorded in the logger.
   */
  private def updateLogger(): Unit = textArea.text = Logger.history.takeRight(10).mkString("\n")
}
