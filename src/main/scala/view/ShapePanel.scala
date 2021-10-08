package view

import model.{Area, Fertile, Size, Species}
import utility.{Constants, Point}

import java.awt.event.{MouseEvent, MouseMotionListener}
import scala.collection.mutable.ArrayBuffer
import java.awt.{Color, Dimension, Graphics2D}
import scala.swing.Panel
import scala.util.Random

class ShapePanel(val width: Int, val height: Int, location: () => swing.Point) extends Panel {

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
        while (isNotPlaceable(Point(x,y),areas) || isNotPlaceable(Point(x+size,y+size),areas)
          || isNotPlaceable(Point(x+size,y),areas) || isNotPlaceable(Point(x,y+size),areas)) {
          x = Random.nextInt(width - size)
          y = Random.nextInt(height - size)
        }
        shapes.append(new Rectangle(Point(x, y), Point(x+size, y+size), color))

        val p = new AnimalPopup(
          s"Species: ${v._1.name}\nSize: ${v._1.size}\nStrength: ${v._1.strength}\nSight: ${v._1.sight}",
          () => new java.awt.Point(x + location().x + Constants.OffsetX, y + location().y + Constants.OffsetY)
        )
        peer.addMouseMotionListener(new MouseMotionListener {
          override def mouseDragged(e: MouseEvent): Unit = {}
          override def mouseMoved(e: MouseEvent): Unit = {
            val mx = e.getX; val my = e.getY
            if (mx > x && mx < x+size && my > y && my < y+size) {
              p.setVisible(true)
            } else {
              p.setVisible(false)
            }
          }
        })
      }
    })
  }

  def isNotPlaceable(p: Point, areas: Seq[Area]): Boolean = {
    areas.find(a => a.area.contains(p)).getOrElse(return false).areaType != Fertile
  }
}