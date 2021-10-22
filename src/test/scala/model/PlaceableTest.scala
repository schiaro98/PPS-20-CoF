package model

import model.animal.{Animal, Big, Carnivore, Species}
import model.food.{Food, FoodType, Meat}
import model.position.{Placeable, Point}
import org.scalatest.funsuite.AnyFunSuite

class PlaceableTest extends AnyFunSuite {

  val point: Point = Point(0,0)

  test("A placeable element is positioned in a point") {
    val placeable: Placeable = Food(FoodType(0, Meat), point, 0)
    assert(placeable.position == point)
  }

  test("An animal is a placeable with more information") {
    val species = Species("Lion", Big, 0, 0, Carnivore)
    val animal = Animal(species, point)
    assert(animal.position == point)
    assert(animal.species == species)
  }

  test("A food is a placeable with a food type and a quantity") {
    val foodType = FoodType(0, Meat)
    val food = Food(foodType, point, 0)
    assert(food.position == point)
    assert(food.foodType == foodType)
  }
}
