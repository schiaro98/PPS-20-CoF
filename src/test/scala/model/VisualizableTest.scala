package model

import model.animal.{Carnivore, Medium, Species}
import model.food.{Food, FoodType, Vegetable}
import model.position.{Point, Visualizable}
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

import java.awt.Color

class VisualizableTest extends AnyFunSuite {

  val color: Color = Color.black

  test("A visualizable must provide an image") {
    val v: Visualizable = FoodType(0, Vegetable, color)
    assert(color == v.color)
  }

  test("A species is a visualizable with more information") {
    val s: Species = Species("tiger", Medium, 10, 10, Carnivore)
    assert(color == s.color)
    assert("tiger" == s.name )
  }

  test("A Vegetable is a visualizable with energy and quantity") {
    val f: Food =
      Food(FoodType(Constants.DefaultEnergyOfVegetable, Vegetable, color), Point(0,0), 10)
    assert(color == f.foodType.color)
    assert(10 == f.foodType.energy )
  }
}
