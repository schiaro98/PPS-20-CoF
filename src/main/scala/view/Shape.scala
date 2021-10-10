package view

import utility.Point

import java.awt.{Color, Graphics2D}

abstract class Shape(topLeft: Point, bottomRight: Point, val color: Color) {

  def draw(g: Graphics2D): Unit = {
    g.fillRect(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y)
  }
}