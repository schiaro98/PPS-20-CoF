package view

import utility.Point

import java.awt.{Color, Graphics2D}

abstract class Shape(topLeft: Point, bottomRight: Point, val color: Color) {

  //protected var xSpeed = 3
  //protected var ySpeed = 3

  var x: Int = topLeft.x
  var y: Int = topLeft.y

  val width: Int = bottomRight.x - topLeft.x
  val height: Int = bottomRight.y - topLeft.y
  def draw(g: Graphics2D): Unit = {
    g.fillRect(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y)
  }

  /*
  def move(panelWidth: Int, panelHeight: Int): Unit = {
    if (x >= panelWidth - this.width || x <= 0) {
      xSpeed *= -1
    }
    if (y >= panelHeight - this.height || y <= 0) {
      ySpeed *= -1
    }

    x += xSpeed
    y += ySpeed
  }
  */
}