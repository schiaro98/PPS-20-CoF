package view

import model.{Animal, FoodInstance, Habitat}
import utility.{Constants, Point}

import java.awt.event.{MouseEvent, MouseMotionListener}
import java.awt.{Color, Dimension, Graphics2D}
import javax.swing.SwingUtilities
import scala.collection.mutable.ArrayBuffer
import scala.swing.Panel

/**
 * Panel used to draw the shapes of the areas, animals and food; it creates also the mouse listeners
 * that shows the [[AnimalPopup]].
 *
 * @param width  the width of the Frame (and also of the Habitat).
 * @param height the height of the Frame (and also of the Habitat).
 */
class SimulationPanel(val width: Int, val height: Int) extends Panel {

  var getLocation: () => swing.Point = () => new swing.Point(0, 0)
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

  /**
   * Method used to append a Shape to those to be drawn.
   *
   * @param shape the Shape to be drawn.
   */
  def addShape(shape: Shape): Unit = {
    shapes.append(shape)
  }

  /**
   * Method used to append a Seq of Shape to those to be drawn.
   *
   * @param shapesSeq the Seq of Shape to drawn.
   */
  def addAllShapes(shapesSeq: Seq[Shape]): Unit = {
    shapesSeq.foreach(s => addShape(s))
  }

  /**
   * Draw all the areas present in the habitat by creating specific rectangles.
   *
   * @param habitat the Habitat of the simulation.
   */
  def drawHabitat(habitat: Habitat): Unit = {
    habitat.areas.foreach(area => addShape(new Rectangle(area.area.topLeft, area.area.bottomRight, area.color)))
  }

  /**
   * After eliminate the old popups it draw the rectangle that represent each animals
   * and create the related popup with all his information.
   *
   * @param animalsAndRectangles a Map containing the info of the Animals and Rectangles used to draw them.
   */
  def drawAnimals(animalsAndRectangles: Map[Animal, Rectangle]): Unit = {
    eliminateOldPopup()
    animalsAndRectangles.foreach(v => {
      addShape(v._2)
      createPopupAndMouseListener(v._1, v._2)
    })
  }

  //todo scaladoc
  def drawFood(food: Seq[FoodInstance]): Unit = {

  }

  /**
   * Dispose all the [[AnimalPopup]] and remove all the old mouse listener.
   */
  def eliminateOldPopup(): Unit = {
    popups.foreach(p => p.deletePopup())
    popups = Seq.empty
    listener.foreach(l => peer.removeMouseMotionListener(l))
    listener = Seq.empty
  }

  /**
   * Create a popup with the information of the animal and a mouse listener which shows the popup when
   * the mouse is over that animal.
   *
   * @param animal    the animal with all his information.
   * @param rectangle the rectangle used to draw the animal.
   */
  def createPopupAndMouseListener(animal: Animal, rectangle: Rectangle): Unit = {
    val p = new AnimalPopup(animal, () => new java.awt.Point(
      rectangle.topLeft.x + Constants.OffsetX + SwingUtilities.getWindowAncestor(this.peer).getLocation().x,
      rectangle.topLeft.y + Constants.OffsetY + SwingUtilities.getWindowAncestor(this.peer).getLocation().y
    ))
    popups = popups :+ p

    val mouseListener = new MouseMotionListener {
      override def mouseDragged(e: MouseEvent): Unit = {}

      override def mouseMoved(e: MouseEvent): Unit = {
        if (Point(e.getX, e.getY).isInside(rectangle.topLeft, rectangle.bottomRight)) {
          p.setVisible(true)
        } else {
          p.setVisible(false)
        }
      }
    }
    //TODO questo dovrebbe esser fatto con reactions += come nelle altre classi di gui
    peer.addMouseMotionListener(mouseListener)
    listener = listener :+ mouseListener
  }
}