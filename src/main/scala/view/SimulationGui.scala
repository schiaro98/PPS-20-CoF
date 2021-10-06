package view

import controller.{OfSpecies, Serializer}
import model._
import utility.Constants

import javax.swing.WindowConstants
import scala.swing.{Dimension, Frame, SimpleSwingApplication}

class SimulationGui(val habitat: Habitat, val species: Map[Species, Int]) extends SimpleSwingApplication {

  override def top: Frame = new Frame {
    this.resizable = false
    this.size = new Dimension(habitat.dimensions._1, habitat.dimensions._2)
    title = "Simulation"

    /**
     * Map containing list of animals (String) and quantity
     */
    var areasRectangles = List.empty[Rectangle]

    habitat.areas.foreach(area => {
      areasRectangles = areasRectangles.::(new Rectangle(area.area.topLeft, area.area.bottomRight, area.color))
    })

    val shapePanel = new ShapePanel(habitat.dimensions._1, habitat.dimensions._2)
    shapePanel.addAllShapes(areasRectangles)
    shapePanel.addAnimals(species)
    contents = shapePanel
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  }
}
