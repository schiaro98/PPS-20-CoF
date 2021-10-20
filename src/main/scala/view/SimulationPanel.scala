package view

import model.{Animal, FoodInstance, Habitat, Point}
import utility.{AnimalUtils, Constants, RectangleArea}

import java.awt.event.{MouseEvent, MouseMotionListener}
import java.awt.{Color, Dimension, Graphics2D}
import javax.swing.SwingUtilities
import scala.collection.mutable.ArrayBuffer
import scala.swing.Panel

/**
 * [[Panel]] used to draw the shapes of the areas, animals and food; it creates also the mouse listeners
 * that shows the [[AnimalPopup]].
 *
 * @param dimension the width and the height of the Frame (and also of the Habitat).
 */
class SimulationPanel(dimension: (Int, Int)) extends Panel {

  val (width, height) = dimension
  val shapes = new ArrayBuffer[Shape]
  var popups: Seq[AnimalPopup] = Seq.empty
  var listener: Seq[MouseMotionListener] = Seq.empty

  peer.setPreferredSize(new Dimension(width, height))
  opaque = true
  background = Color.white

  override def paint(g: Graphics2D): Unit = {
    g.clearRect(0, 0, width, height)
    shapes.foreach(s => s.draw(g))
  }

  /**
   * Method used to append a [[Shape]] to those to be drawn.
   *
   * @param shape the [[Shape]] to be drawn.
   */
  def addShape(shape: Shape): Unit = shapes.append(shape)

  /**
   * Method used to append a set of [[Shape]] to those to be drawn.
   *
   * @param shapesSeq the Seq of [[Shape]] to drawn.
   */
  def addAllShapes(shapesSeq: Seq[Shape]): Unit = shapesSeq.foreach(s => addShape(s))

  /**
   * Draw all the areas present in the [[Habitat]] by creating specific rectangles.
   *
   * @param habitat the [[Habitat]] of the simulation.
   */
  def drawHabitat(habitat: Habitat): Unit = {
    addShape(new Rectangle(RectangleArea(Point(0, 0), Point(width, height)), Color.white))
    habitat.areas.foreach(a => addShape(new Rectangle(RectangleArea(a.area.topLeft, a.area.bottomRight), a.area.color)))
  }

  /**
   * After eliminate the old popups it draw the rectangle that represent each animals
   * and create the related popup with all his information.
   *
   * @param animals a Seq of [[Animal]] to draw.
   */
  def drawAnimals(animals: Seq[Animal]): Unit = {
    eliminateOldPopup()
    animals.foreach(a => {
      val bottomRight = Point(a.position.x + AnimalUtils.getPixelFromSize(a), a.position.y + AnimalUtils.getPixelFromSize(a))
      val rectangle = new Rectangle(RectangleArea(a.position, bottomRight), a.color)
      addShape(rectangle)
      createPopupAndMouseListener(a, rectangle)
    })
  }

  /**
   * Draw all the food present in the map by creating specific circle.
   *
   * @param food a Seq of [[FoodInstance]] to draw.
   */
  def drawFood(food: Seq[FoodInstance]): Unit = food.foreach(f => addShape(new Circle(f.position, f.color, Constants.PixelForFood)))

  /**
   * Method to draw all the areas present in the [[Habitat]], the animals and the food.
   *
   * @param habitat the [[Habitat]] of the simulation.
   * @param animals a Seq of [[Animal]] to draw.
   * @param food    a Seq of [[FoodInstance]] to draw.
   */
  def drawAll(habitat: Habitat, animals: Seq[Animal], food: Seq[FoodInstance]): Unit = {
    drawHabitat(habitat)
    drawAnimals(animals)
    drawFood(food)
  }

  /**
   * Dispose all the [[AnimalPopup]] and remove all the old mouse listener.
   */
  private def eliminateOldPopup(): Unit = {
    popups.foreach(p => p.deletePopup())
    popups = Seq.empty
    listener.foreach(l => peer.removeMouseMotionListener(l))
    listener = Seq.empty
  }

  /**
   * Create a popup with the information of the animal and a mouse listener which shows the popup when
   * the mouse is over that animal.
   * TODO questo metodo rallenta parecchio l'applicazione
   * @param animal    the animal with all his information.
   * @param rectangle the rectangle used to draw the animal.
   */
  private def createPopupAndMouseListener(animal: Animal, rectangle: Rectangle): Unit = {
    val p = new AnimalPopup(animal, () => new swing.Point(
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
    peer.addMouseMotionListener(mouseListener)
    listener = listener :+ mouseListener
  }
}