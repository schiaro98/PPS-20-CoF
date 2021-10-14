package view

import utility.Point

import java.awt.{Color, Graphics2D}

abstract class Shape(topLeft: Point, val color: Color) {

  def draw(g: Graphics2D): Unit = {
    g.fillRect(topLeft.x, topLeft.y, topLeft.x, topLeft.y)
  }
}

class Rectangle(val topLeft: Point, val bottomRight: Point, color: Color) extends Shape(topLeft, color) {

  override def draw(g: Graphics2D): Unit = {
    g.fillRect(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y)
  }
}

class Circle(val topLeft: Point, val radius: Int, color: Color) extends Shape(topLeft, color) {

  override def draw(g: Graphics2D): Unit = {
    g.fillOval(topLeft.x, topLeft.y, radius, radius)
  }
}