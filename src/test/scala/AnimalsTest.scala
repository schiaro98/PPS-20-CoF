import model.Size.Medium
import model.{Animal, Carnivores, Meat, Species, Vegetable}
import org.scalatest.funsuite.AnyFunSuite

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class AnimalsTest extends AnyFunSuite{

  val img: BufferedImage = ImageIO.read(new File("res/images/tiger-icon.jpg"))
  val p: (Int, Int) = (0,0)
  val a: Animal = Carnivores(Species(img, "tiger", Medium, 10, 10), p, p)
  //todo è giusto passare la specie ad una classe che la estende?

  test("An animal is an entity with personal parameters and belong to a species") {
    assert(img == a.icon)
    assert(100 == a.health)
  }

  test("An animal with 0 of health (or thirst) is dead") {
    assert(a.isAlive)
    val a2 = a.update(0, 100, p, p)
    assert(!a2.isAlive)
    val a3 = a.update(100, 0, p, p)
    assert(!a3.isAlive)
  }

  test("An animal that eats regains health") {
    val animal0 = a.update(100, 100, p, p)
    val food1 = Vegetable(img, 10, 5, p)
    assert((animal0, Some(food1)) == animal0.eat(food1))

    val animal1 = a.update(10, 100, p, p)
    val res1 = animal1.eat(food1)
    assert((60, None) == (res1._1.health, res1._2))

    val animal2 = a.update(10, 100, p, p)
    val food2 = Meat(img, 12, 10, p)
    val res2 = animal2.eat(food2)
    assert((100, 2) == (res2._1.health, res2._2.get.quantity))
  }
}
