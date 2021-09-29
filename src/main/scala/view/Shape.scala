package view

import java.awt.{Color, Graphics2D}

abstract class Shape(var x: Int, var y:Int, var width:Int, var height:Int, val color: Color) {

  protected var xSpeed = 3
  protected var ySpeed = 3

  def draw(g: Graphics2D) {
    g.fillRect(x, y, width, height)
  }

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