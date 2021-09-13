import model.Size.Medium
import model.{Food, Species, Vegetable, Visualizable}
import org.scalatest.funsuite.AnyFunSuite

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class VisualizableTest extends AnyFunSuite {

  val img: BufferedImage = ImageIO.read(new File("res/images/tiger-icon.jpg"))
//  val img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)

  test("A visualizable must provide an image") {
    //todo mettere il costruttore di Visualizable private?
    val v: Visualizable = Visualizable(img)
    assert(img == v.icon)
  }

  test("A species is a visualizable with more information") {
    val s: Species = Species(img, "tiger", Medium, 10, 10)
    assert(img == s.icon)
    assert("tiger" == s.name )
  }

  test("A food is a visualizable with energy and quantity") {
    val f: Food = Vegetable(10, (0,0), 10, img)
    assert(img == f.icon)
    assert(10 == f.energy )
  }
}
