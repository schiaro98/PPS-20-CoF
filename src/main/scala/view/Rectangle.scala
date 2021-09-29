package view

import utility.Point

import java.awt.Graphics2D

class Rectangle(topLeft: Point, bottomRigth: Point) extends Shape(topLeft, bottomRigth) {

    override def draw(g: Graphics2D) {
      g.fillRect(topLeft.x, topLeft.y, bottomRigth.x - topLeft.x, bottomRigth.y - topLeft.y)
    }

}
