package view

import model.{Animal, Habitat}

import javax.swing.WindowConstants
import scala.swing.{Dimension, Frame, SimpleSwingApplication}

class SimulationGui(habitat: Habitat, val shapePanel: ShapePanel) extends SimpleSwingApplication {

  val (width, height) = habitat.dimensions

  override def top: Frame = new Frame {
    this.resizable = false
    this.size = new Dimension(width, height)
    this.title = "Simulation"
    contents = shapePanel
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  }

  def updatePanel(animalsAndRectangles: Map[Animal, Rectangle]): Unit = {
    shapePanel.peer.removeAll()
    shapePanel.drawHabitat(habitat)
    shapePanel.drawAnimals(animalsAndRectangles)
    shapePanel.revalidate()
    shapePanel.repaint()
  }
}
