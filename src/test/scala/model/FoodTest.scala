package model

import model.food.{Food, FoodType, Vegetable}
import model.position.Point
import org.scalatest.funsuite.AnyFunSuite

import java.awt.Color

class FoodTest extends AnyFunSuite {

  val color: Color = Color.black
  val p: Point = Point(0, 0)
  val quantity = 10
  val wheat: FoodType = FoodType(quantity, Vegetable, color)
  val food: Food = Food(wheat, p, 10)

  test("A food is a placeable element that contains a certain quantity of types of food") {
    assert(food.position == p)
    assert(food.foodType == wheat)
    assert(food.quantity == quantity)
    assert(food.foodType.color == color)
  }

  test("A food can be consumed") {
    val consumedFood = food.consume(quantity - 1)
    assert(consumedFood.quantity == 1)
  }

  test("A food can't be consumed more than his quantity") {
    assertThrows[IllegalArgumentException](food.consume(quantity+1))
  }
}
