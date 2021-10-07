package view

import model.{Area, Fertile, Size, Species}
import utility.Point

import scala.collection.mutable.ArrayBuffer
import java.awt.{Color, Dimension, Graphics2D}
import scala.swing.Panel
import scala.util.Random

class ShapePanel(val width: Int, val height: Int) extends Panel {

  peer.setPreferredSize(new Dimension(width, height))

  opaque = true
  background = Color.white

  override def paint(g: Graphics2D): Unit = {
    g.clearRect(0, 0, width, height)

    for (s <- shapes) {
      g.setColor(s.color)
      s.draw(g)
    }
  }

  val shapes = new ArrayBuffer[Shape]

  /*
  new Timer(10, (_: ActionEvent) => {
    for (s <- shapes) {
      s.move(width, height)
    }
    repaint
  }) //.start()
  */
  def addShape(shape: Shape): Unit = {
    shapes.append(shape)
  }

  def addAllShapes(shapesSeq: Seq[Shape]): Unit = {
    shapesSeq.foreach(s => addShape(s))
  }

  def addAnimals(species: Map[Species, Int], areas: Seq[Area]): Unit = {
    species.foreach(v => {
      val color = new Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
      val size = v._1.size match {
        case Size.Big => 12
        case Size.Medium => 9
        case Size.Small => 6
      }

      for (_ <- 1 to v._2) {
        //TODO cercare un metodo più funzionale per farlo
        var x = Random.nextInt(width - size)
        var y = Random.nextInt(height - size)
        while (isNotPlaceable(Point(x,y),areas) || isNotPlaceable(Point(x+size,y+size),areas)) {
          x = Random.nextInt(width - size)
          y = Random.nextInt(height - size)
        }
        shapes.append(new Rectangle(Point(x, y), Point(x+size, y+size), color))
      }
    })
  }

  def isNotPlaceable(p: Point, areas: Seq[Area]): Boolean = {
    areas.find(a => a.area.contains(p)).getOrElse(return false).areaType != Fertile
  }
}