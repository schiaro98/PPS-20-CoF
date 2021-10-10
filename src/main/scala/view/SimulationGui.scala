package view

import controller.GameLoop
import model._

import javax.swing.WindowConstants
import scala.swing.{Dimension, Frame, SimpleSwingApplication}

class SimulationGui(val habitat: Habitat, val species: Map[Species, Int]) extends SimpleSwingApplication {

  override def top: Frame = new Frame {
    this.resizable = false
    this.size = new Dimension(habitat.dimensions._1, habitat.dimensions._2)
    this.title = "Simulation"

//    println(this.location)
//    this.location = new Point(50,50)
//    println(this.location)

    /**
     * Map containing list of animals (String) and quantity
     */
    var areasRectangles = List.empty[Rectangle]

    habitat.areas.foreach(area => {
      areasRectangles = areasRectangles.::(new Rectangle(area.area.topLeft, area.area.bottomRight, area.color))
    })
    val loop: GameLoop = new GameLoop(species, habitat)
    val shapePanel = new ShapePanel(habitat.dimensions._1, habitat.dimensions._2, () => this.location)
    shapePanel.addAllShapes(areasRectangles)
    shapePanel.addAnimals(species, habitat.areas)
    contents = shapePanel
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    loop.run()
  }
}
