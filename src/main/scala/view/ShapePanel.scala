package view

import scala.collection.mutable.ArrayBuffer
import java.awt.{Color, Dimension, Graphics2D}
import javax.swing._
import java.awt.event._
import scala.swing.Panel

class ShapePanel(val width: Int, val height: Int) extends Panel {

  preferredSize = new Dimension(width, height)
  opaque = true
  background = Color.white

  override def paint(g: Graphics2D) {
    g.clearRect(0, 0, width, height)

    for(s <- shapes) {
      g.setColor(s.color)
      s.draw(g)
    }
  }

  var shapes = new ArrayBuffer[Shape]

  new Timer(10, (_: ActionEvent) => {
    for (s <- shapes) {
      s.move(width, height)
    }
    repaint
  })//.start() TODO valutare il farlo con più thread se serve il movimento, sicuramente senza timer

  def addShape(shape: Shape) {
    shapes.append(shape)
  }

  def addAllShapes(shapesSeq: Seq[Shape]): Unit ={
    shapesSeq.foreach(s => addShape(s))
  }
}