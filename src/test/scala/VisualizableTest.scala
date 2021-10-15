import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.Point

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class VisualizableTest extends AnyFunSuite {

  val path = "res/images/tiger-icon.jpg"
  val img: BufferedImage = ImageIO.read(new File(path))

  test("A visualizable must provide an image") {
    //todo mettere il costruttore di Visualizable private?
    val v: Visualizable = Visualizable(path)
    assert(path == v.icon)
  }

  test("A species is a visualizable with more information") {
    val s: Species = Species(path, "tiger", Medium, 10, 10, Carnivore)
    assert(path == s.icon)
    assert("tiger" == s.name )
  }

  test("A food is a visualizable with energy and quantity") {
    val f: Food = Vegetable(10, Point(0,0), 10, path)
    assert(path == f.icon)
    assert(10 == f.energy )
  }
}
