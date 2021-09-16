package view

import scala.swing.{Dimension, Frame, SimpleSwingApplication}

class SimulationGui(logic: LogicGui) extends SimpleSwingApplication {

  override def top: Frame = new Frame {

    title = "Simulation"
    preferredSize = new Dimension(800, 400)

    /**
     * Map containing list of animals (String) and quantity
     */
    val animals: Map[String, Int] = logic.species

    val shapePanel = new ShapePanel()
    //def rectangle = new Rectangle(140, 40, 100, 50)
    //def circle = new Circle(240, 100, 100)
    //shapePanel.addAllShapes(Seq(rectangle,circle))
    contents = shapePanel
  }
}
