package view

import model._

import javax.swing.WindowConstants
import scala.swing.{Dimension, Frame, SimpleSwingApplication}

class SimulationGui(val habitat: Habitat) extends SimpleSwingApplication {

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
    contents = shapePanel
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

  }

}
