package model.shape

import model.position.{Point, Visualizable}

import java.awt.{Color, Graphics2D}

/**
 * Trait that represent a geometric shape that can be drawn in two dimensions.
 */
trait Shape extends Visualizable {
  val topLeft: Point

  /**
   * Method used to draw the shape.
   *
   * @param graphics the graphics where to draw the shape.
   */
  def draw(graphics: Graphics2D): Unit
}

/**
 * A drawable rectangle.
 *
 * @param area  the [[RectangleArea]] which contains the top left and the bottom right [[Point]].
 * @param color the color that should be used to draw the rectangle.
 */
class Rectangle(area: RectangleArea, override val color: Color) extends RectangleArea(area.topLeft, area.bottomRight) with Shape {

  override def draw(graphics: Graphics2D): Unit = {
    graphics.setColor(color)
    graphics.fillRect(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y)
  }
}

/**
 * A drawable circle.
 *
 * @param topLeft the top left point of the square circumscribed to the circle.
 * @param radius  the radius of the circle.
 * @param color   the color that should be used to draw the rectangle.
 */
class Circle(override val topLeft: Point, override val color: Color, radius: Int) extends Shape {

  override def draw(graphics: Graphics2D): Unit = {
    graphics.setColor(color)
    graphics.fillOval(topLeft.x, topLeft.y, radius, radius)
  }
}