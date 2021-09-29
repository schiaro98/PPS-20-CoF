package view

import utility.Point

import java.awt.Graphics2D

abstract class Shape(topLeft: Point, bottomRigth: Point) {

  protected var xSpeed = 3
  protected var ySpeed = 3

  var x: Int = topLeft.x
  var y: Int = topLeft.y

  val width: Int = bottomRigth.x - topLeft.x
  val height: Int = bottomRigth.y - topLeft.y
  def draw(g: Graphics2D): Unit = {
    g.fillRect(topLeft.x, topLeft.y, bottomRigth.x - topLeft.x, bottomRigth.y - topLeft.y)
  }

  /*
  TODO potrebbe non servire sto metodo
   */
  def move(panelWidth: Int, panelHeight: Int) {
    if (x >= panelWidth - this.width || x <= 0) {
      xSpeed *= -1
    }
    if (y >= panelHeight - this.height || y <= 0) {
      ySpeed *= -1
    }

    x += xSpeed
    y += ySpeed
  }
}