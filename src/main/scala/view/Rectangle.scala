package view

import utility.Point

import java.awt.{Color, Graphics2D}

class Rectangle(val topLeft: Point, val bottomRigth: Point, color: Color) extends Shape(topLeft, bottomRigth, color) {

    override def draw(g: Graphics2D): Unit = {
      g.fillRect(topLeft.x, topLeft.y, bottomRigth.x - topLeft.x, bottomRigth.y - topLeft.y)
    }

}
