package view

import model.{Habitat, Probability, RandomHabitatType}
import utility.Constants.defaultSimulationDimension

import scala.swing.{Frame, SimpleSwingApplication}

class SimulationGui() extends SimpleSwingApplication {

  override def top: Frame = new Frame {

    title = "Simulation"
    preferredSize = defaultSimulationDimension

    /**
     * Map containing list of animals (String) and quantity
     */
    var areasRectangles = List.empty[Rectangle]
    val habitat: Habitat = Habitat( RandomHabitatType, Probability(1), (1000, 1000), 10)
    habitat.areas.foreach(area => {
      areasRectangles = areasRectangles.::(new Rectangle(area.area.topLeft, area.area.bottomRight))
    })

    val shapePanel = new ShapePanel()
    shapePanel.addAllShapes(areasRectangles)
    contents = shapePanel
  }

}
