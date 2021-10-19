package view

import model.Point
import utility.RectangleArea

import java.awt.{Color, Graphics2D}

//TODO aggiungere gli object così da non dover usare "new" ?

/**
 * Trait that represent a geometric shape that can be drawn in two dimensions
 */
trait Shape {
  val topLeft: Point
  val color: Color

  /**
   * Method used to draw the shape
   *
   * @param graphics the graphics where to draw the shape
   */
  def draw(graphics: Graphics2D): Unit
}

/**
 * A drawable rectangle
 *
 * @param rectangleArea the [[RectangleArea]] which contains the top left and the bottom right [[Point]]
 * @param color         the color that should be used to draw the rectangle.
 */
class Rectangle(rectangleArea: RectangleArea, override val color: Color) extends RectangleArea(rectangleArea.topLeft, rectangleArea.bottomRight) with Shape {

  override def draw(graphics: Graphics2D): Unit = {
    graphics.setColor(color)
    graphics.fillRect(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y)
  }
}

/**
 * A drawable circle
 *
 * @param topLeft the top left point of the square circumscribed to the circle
 * @param radius  the radius of the circle
 * @param color   the color that should be used to draw the rectangle.
 */
class Circle(override val topLeft: Point, override val color: Color, radius: Int) extends Shape {

  override def draw(graphics: Graphics2D): Unit = {
    graphics.setColor(color)
    graphics.fillOval(topLeft.x, topLeft.y, radius, radius)
  }
}