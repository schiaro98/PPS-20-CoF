package model

import model.animal.{Carnivore, Medium, Species}
import model.food.{FoodType, Vegetable}
import model.position.Visualizable
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import utility.Constants

import java.awt.Color

class VisualizableTest extends AnyFlatSpec with should.Matchers {

  val color: Color = Color.black

  "A visualizable" should  "provide a color" in {
    val v: Visualizable = FoodType(0, Vegetable, color)
    color should be (v.color)
  }

  "A Species is a visualizable that" should "have other information" in {
    val name = "tiger"
    val s: Species = Species(name, Medium, 0, 0, Carnivore)
    s.color should be (color)
    s.name should be (name)
  }

  "A FoodType is a visualizable that" should "have energy and a category" in {
    val energy = 10
    val foodType: FoodType = FoodType(energy, Vegetable, color)
    foodType.color should be (color)
    foodType.energy should be (energy)
  }
}
