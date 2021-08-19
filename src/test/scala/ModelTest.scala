import model.Size.Medium
import model.{Food, Species, Visualizable}
import org.scalatest.funsuite.AnyFunSuite

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ModelTest extends AnyFunSuite {

  val img: BufferedImage = ImageIO.read(new File("src/main/resources/tiger-icon.jpg"))
  val f: Food = Food(img, 10, 10)

  test("A visualizable must provide an image") {
//    val img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
    val v: Visualizable = Visualizable(img)
    assert(img == v.icon)
  }

  test("A species is a visualizable with more information") {
    val s: Species = Species(img, "tiger", Medium, 10, 10)
    assert(img == s.icon)
    assert("tiger" == s.name )
  }

  test("A food is a visualizable with energy and quantity") {
    val f: Food = Food(img, 10, 10)
    assert(img == f.icon)
    assert(10 == f.energy )
  }

  test("A food heals you for a certain amount") {
    assert(100 == f.feed(100))
    assert(0 == f.feed(100))
    val f2: Food = Food(img, 5, 5)
    assert(20 == f2.feed(24))
    assert(1 == f2.quantity)
  }
}
