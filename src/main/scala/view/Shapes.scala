package view

import java.awt.Graphics2D

class Rectangle(
                   xRectangle: Int, yRectangle: Int,
                   wRectangle: Int, hRectangle: Int)
    extends Shape(xRectangle, yRectangle, wRectangle, hRectangle) {

    override def draw(g: Graphics2D) {
      g.fillRect(x, y, width, height)
    }

}

class Circle(xCircle: Int, yCircle: Int, radius: Int)
  extends Shape(xCircle, yCircle, radius, radius) {

  override def draw(g: Graphics2D) {
    g.fillOval(x, y, width, height)
  }

}
