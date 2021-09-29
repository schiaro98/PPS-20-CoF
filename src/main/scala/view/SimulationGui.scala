package view

import model._
import scala.swing.{Frame, SimpleSwingApplication}

class SimulationGui(val habitat: Habitat) extends SimpleSwingApplication {

  override def top: Frame = new Frame {

    title = "Simulation"

    // preferredSize e la dimensione del Panel non coincidono anche se si mette lo stesso valore
//    preferredSize = defaultSimulationDimension

    /**
     * Map containing list of animals (String) and quantity
     */
    var areasRectangles = List.empty[Rectangle]

    habitat.areas.foreach(area => {
      val tl = area.area.topLeft
      val br = area.area.bottomRight
      val rect = new Rectangle(tl.x, tl.y, br.x - tl.x, br.y - tl.y, area.color)
      areasRectangles = areasRectangles.::(rect)
    })

    val shapePanel = new ShapePanel(habitat.dimensions._1, habitat.dimensions._2)
    shapePanel.addAllShapes(areasRectangles)
    contents = shapePanel
  }
}
