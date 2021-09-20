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
