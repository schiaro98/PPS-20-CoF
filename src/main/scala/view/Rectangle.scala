package view

import utility.Point

import java.awt.{Color, Graphics2D}

class Rectangle(val topLeft: Point, val bottomRight: Point, color: Color) extends Shape(topLeft, bottomRight, color) {

    override def draw(g: Graphics2D): Unit = {
      g.fillRect(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y)
    }

}
