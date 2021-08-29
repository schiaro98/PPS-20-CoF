import model.Size.Medium
import model.{Food, Species, Vegetable, Visualizable}
import org.scalatest.funsuite.AnyFunSuite

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ModelTest extends AnyFunSuite {

  val path = "res/images/tiger-icon.jpg"
  val img: BufferedImage = ImageIO.read(new File(path))
//  val img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)

  test("A visualizable must provide an image") {
    val v: Visualizable = Visualizable(path)
    assert(path == v.icon)
  }

  test("A species is a visualizable with more information") {
    val s: Species = Species(path, "tiger", Medium, 10, 10)
    assert(path == s.icon)
    assert("tiger" == s.name )
  }

  test("A food is a visualizable with energy and quantity") {
    val f: Food = Vegetable(path, 10, 10, (0,0))
    assert(path == f.icon)
    assert(10 == f.energy )
  }
}
