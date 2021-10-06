import model.Size._
import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants._
import utility.Point

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class AnimalsTest extends AnyFunSuite{

  val path = "res/images/tiger-icon.jpg"
  val img: BufferedImage = ImageIO.read(new File(path))
  val p: Point = Point(0,0)
  val a: Animal = Carnivorous(Species(path, "tiger", Medium, 10, 10), p)

  test("An animal is an entity with personal parameters and belong to a species") {
    assert(path == a.icon)
    assert("tiger" == a.name)
    assert(MaxHealth == a.health)
  }

  test("An animal with 0 of health (or thirst) is dead") {
    assert(a.isAlive)
    val a2 = a.update(0, MaxThirst)
    assert(!a2.isAlive)
    val a3 = a.update(MaxHealth, 0)
    assert(!a3.isAlive)
  }

  test("An animal that eats regains health") {
    val animal0 = a.update(MaxHealth, MaxThirst)
    val food1 = Vegetable(5, p, 10)
    assert((animal0, Some(food1)) == animal0.eat(food1))

    val animal1 = a.update(MaxHealth - 90, MaxThirst)
    val res1 = animal1.eat(food1)
    assert((MaxHealth - 40, None) == (res1._1.health, res1._2))

    val animal2 = a.update(MaxHealth - 90, MaxThirst)
    val food2 = Meat(10, p, 12)
    val res2 = animal2.eat(food2)
    assert((MaxHealth, 2) == (res2._1.health, res2._2.get.quantity))
  }
}
