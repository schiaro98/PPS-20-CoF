package view

import model.{Animal, Habitat}
import utility.Constants

import java.awt.event.{MouseEvent, MouseMotionListener}
import java.awt.{Color, Dimension, Graphics2D, Point}
import javax.swing.SwingUtilities
import scala.collection.mutable.ArrayBuffer
import scala.swing.Panel

class ShapePanel(val width: Int, val height: Int) extends Panel {

  var getLocation: () => swing.Point = () => new swing.Point(0,0)
  val shapes = new ArrayBuffer[Shape]
  var popups: Seq[AnimalPopup] = Seq.empty //TODO lasciare var?
  var listener: Seq[MouseMotionListener] = Seq.empty //TODO lasciare var?

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

  def addShape(shape: Shape): Unit = {
    shapes.append(shape)
  }

  def addAllShapes(shapesSeq: Seq[Shape]): Unit = {
    shapesSeq.foreach(s => addShape(s))
  }

  def drawHabitat(habitat: Habitat): Unit = {
    habitat.areas.foreach(area => {
      addShape(new Rectangle(area.area.topLeft, area.area.bottomRight, area.color))
    })
  }

  def drawAnimals(animalsAndRectangles: Map[Animal, Rectangle]): Unit = {
    eliminateOldPopup()
    animalsAndRectangles.foreach(v => {
      val animal = v._1
      val rectangle = v._2
      shapes.append(rectangle)

      val p = new AnimalPopup(textToDisplay(animal), () => new Point(
        rectangle.topLeft.x + Constants.OffsetX + SwingUtilities.getWindowAncestor(this.peer).getLocation().x,
        rectangle.topLeft.y + Constants.OffsetY + SwingUtilities.getWindowAncestor(this.peer).getLocation().y
      ))
      val mouseListener = new MouseMotionListener {
        override def mouseDragged(e: MouseEvent): Unit = {}
        override def mouseMoved(e: MouseEvent): Unit = {
          val xMouse = e.getX
          val yMouse = e.getY
          if (xMouse > v._2.topLeft.x && xMouse < v._2.bottomRigth.x && yMouse > v._2.topLeft.y && yMouse < v._2.bottomRigth.y) {
            p.setVisible(true)
          } else {
            p.setVisible(false)
          }
        }
      }
      //TODO questo dovrebbe esser fatto con reactions += come nelle altre classi di gui
      peer.addMouseMotionListener(mouseListener)
      popups = popups :+ p
      listener = listener :+ mouseListener
    })
  }

  def textToDisplay(animal: Animal): String = {
    s"Species: ${animal.name}\nHealth: ${animal.health}\nThirst: ${animal.thirst}\nStrength: ${animal.strength}\nSight: ${animal.sight}\nSize: ${animal.size}"
  }

  def eliminateOldPopup(): Unit = {
    popups.foreach(p => p.deletePopup())
    popups = Seq.empty
    listener.foreach(l => peer.removeMouseMotionListener(l))
    listener = Seq.empty
  }

}