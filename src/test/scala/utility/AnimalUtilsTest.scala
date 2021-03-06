package utility

import model.{shape, _}
import model.animal.{Big, Carnivore, Herbivore, Medium, Species}
import model.habitat.{Area, Fertile, Habitat}
import model.position.Point
import model.shape.RectangleArea
import org.scalatest.funsuite.AnyFunSuite

class AnimalUtilsTest extends AnyFunSuite {

  val topLeft: Point = Point(0, 0)
  val bottomRight: Point = Point(100, 100)
  val rectangle: RectangleArea = shape.RectangleArea(topLeft, bottomRight)
  val tiger: Species = Species("tiger", Medium, 0, 0, Carnivore)

  test("An animal can be positioned in a walkable areas") {
    val area = Area(Fertile, rectangle)
    val habitat: Habitat = Habitat(dimensions = (100, 100), areas = Seq(area))
    val point = AnimalUtils.placeAnimal(habitat, tiger)
    assert(point.isInside(topLeft, bottomRight))
  }

  test("Species with different size have a different amount of pixel") {
    val pixelOfMedium = AnimalUtils.getPixelFromSize(tiger)
    val pixelOfBig = AnimalUtils.getPixelFromSize(Species("Elephant", Big, 0, 0, Herbivore))
    assert(pixelOfBig != pixelOfMedium)
  }

  test("An animal is represented with a square that have 4 vertices") {
    assert(AnimalUtils.verticesOfAnimal(tiger, topLeft).lengthCompare(4) == 0)
  }
}
