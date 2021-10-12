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

    val shapePanel = new ShapePanel(habitat.dimensions._1, habitat.dimensions._2, () => this.location)
    val loop: GameLoop = GameLoop(species, habitat)

    habitat.areas.foreach(area => {
      shapePanel.addShape(new Rectangle(area.area.topLeft, area.area.bottomRight, area.color))
    })
    shapePanel.addAnimals(loop.getAnimalsInMap)

    contents = shapePanel
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

    loop.run()
  }
}
