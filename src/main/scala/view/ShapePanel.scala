package view

import model.Species
import utility.Point

import scala.collection.mutable.ArrayBuffer
import java.awt.{Color, Dimension, Graphics2D}
import javax.swing._
import java.awt.event._
import scala.swing.Panel
import scala.util.Random

class ShapePanel(val width: Int, val height: Int) extends Panel {

  peer.setPreferredSize(new Dimension(width, height))

  opaque = true
  background = Color.white

  override def paint(g: Graphics2D): Unit = {
    g.clearRect(0, 0, width, height)

    for (s <- shapes) {
      g.setColor(s.color)
      s.draw(g)
    }
  }

  val shapes = new ArrayBuffer[Shape]

  new Timer(10, (_: ActionEvent) => {
    for (s <- shapes) {
      s.move(width, height)
    }
    repaint
  }) //.start() TODO valutare il farlo con più thread se serve il movimento, sicuramente senza timer

  def addShape(shape: Shape): Unit = {
    shapes.append(shape)
  }

  def addAllShapes(shapesSeq: Seq[Shape]): Unit = {
    shapesSeq.foreach(s => addShape(s))
  }

  def addAnimals(species: Map[Species, Int]): Unit = {
    species.foreach(v => {
      for (_ <- 1 to v._2) {
        val x = Random.nextInt(width)
        val y = Random.nextInt(height)
        shapes.append(new Rectangle(Point(x, y), Point(x+10, y+10), Color.black))
      }
    })
  }
}