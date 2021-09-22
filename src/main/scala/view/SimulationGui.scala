package view

import model.{GridHabitatType, Habitat, Probability}
import utility.Constants.defaultSimulationDimension

import scala.swing.{Dimension, Frame, SimpleSwingApplication}

class SimulationGui() extends SimpleSwingApplication {

  override def top: Frame = new Frame {

    title = "Simulation"
    preferredSize = defaultSimulationDimension

    /**
     * Map containing list of animals (String) and quantity
     */
    var areasRectangles = List.empty[Rectangle]
    val habitat: Habitat = Habitat( GridHabitatType, Probability(1), (1000, 1000), Seq.empty)
    habitat.areas.foreach(area => {
      val tl = area.topLeft
      val br = area.bottomRight
      val rect = new Rectangle(tl._1, tl._2, br._1 - tl._1, br._2 - tl._2)
      areasRectangles = areasRectangles.::(rect)
    })
    val shapePanel = new ShapePanel()
    shapePanel.addAllShapes(areasRectangles)
    contents = shapePanel
  }
}
