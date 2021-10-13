package view

import model._
import utility.{Constants, Point}

import java.awt.event.{MouseEvent, MouseMotionListener}
import java.awt.{Color, Dimension, Graphics2D}
import scala.collection.mutable.ArrayBuffer
import scala.swing.Panel
import scala.util.Random

class ShapePanel(val width: Int, val height: Int, location: () => swing.Point) extends Panel {

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

  /*
  new Timer(10, (_: ActionEvent) => {
    for (s <- shapes) {
      s.move(width, height)
    }
    repaint
  }) //.start()
  */
  def addShape(shape: Shape): Unit = {
    shapes.append(shape)
  }

  def addAllShapes(shapesSeq: Seq[Shape]): Unit = {
    shapesSeq.foreach(s => addShape(s))
  }

  def addAnimals(animals: Seq[Animal]): Unit = {
    animals.foreach(animal => {
      val color = new Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
      val point = animal.position
      val size = animal.size match {
        case Big => 12
        case Medium => 9
        case Small => 6
      }
      shapes.append(new Rectangle(point, point + (size, size), color))
      val p = new AnimalPopup(
        s"Species: ${animal.name}\nSize: ${animal.size}\nStrength: ${animal.strength}\nSight: ${animal.sight}",
        () => new java.awt.Point(point.x + location().x + Constants.OffsetX, point.y + location().y + Constants.OffsetY)
      )
      /*
      TODO questo dovrebbe esser fatto con reactions += come nelle altre classi di gui
       */
      peer.addMouseMotionListener(new MouseMotionListener {
        override def mouseDragged(e: MouseEvent): Unit = {}
        override def mouseMoved(e: MouseEvent): Unit = {
          val mx = e.getX; val my = e.getY
          if (mx > point.x && mx < point.x+size && my > point.y && my < point.y+size) {
            p.setVisible(true)
          } else {
            p.setVisible(false)
          }
        }
      })
    })
  }

  def isNotPlaceable(p: Point, areas: Seq[Area]): Boolean = {
    areas.find(a => a.area.contains(p)).getOrElse(return false).areaType != Fertile
  }
}