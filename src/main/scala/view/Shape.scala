package view

import utility.Point

import java.awt.{Color, Graphics2D}

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
 * A rectangle
 *
 * @param topLeft     the top left point of the rectangle
 * @param bottomRight the top bottom right of the rectangle
 * @param color       the color that should be used to draw the rectangle.
 */
class Rectangle(override val topLeft: Point, val bottomRight: Point, override val color: Color) extends Shape {

  override def draw(graphics: Graphics2D): Unit = {
    graphics.fillRect(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y)
  }
}

/**
 * A circle
 *
 * @param topLeft the top left point of the square circumscribed to the circle
 * @param radius  the radius of the circle
 * @param color   the color that should be used to draw the rectangle.
 */
class Circle(override val topLeft: Point, val radius: Int, override val color: Color) extends Shape {

  override def draw(graphics: Graphics2D): Unit = {
    graphics.fillOval(topLeft.x, topLeft.y, radius, radius)
  }
}