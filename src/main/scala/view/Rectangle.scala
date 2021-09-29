package view

import java.awt.{Color, Graphics2D}

class Rectangle(
                   xRectangle: Int, yRectangle: Int,
                   wRectangle: Int, hRectangle: Int,
                   color: Color)
    extends Shape(xRectangle, yRectangle, wRectangle, hRectangle, color) {

    override def draw(g: Graphics2D) {
      g.fillRect(x, y, width, height)
    }

}
