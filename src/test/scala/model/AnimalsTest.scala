package model

import model.animal.{Adult, Animal, Carnivore, Medium, Old, Small, Species, Young}
import model.food.{Food, FoodType, Meat}
import model.position.Point
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

import java.awt.Color

class AnimalsTest extends AnyFunSuite {

  val color: Color = Color.black
  val p: Point = Point(0, 0)
  val sight = 10
  val species: Species = Species("tiger", Medium, 0, sight, Carnivore, color)
  val animal: Animal = Animal(species, p)
  val food: Food = Food(FoodType(10, Meat), p, 5)
  val decrement = 90

  test("An animal is an entity with personal parameters and belong to a species") {
    assert(animal.color == color)
    assert(animal.species.name == species.name)
    assert(animal.position == p)
  }

  test("A default animal is created with the max health and thirst anda random age") {
    assert(animal.health == Constants.MaxHealth)
    assert(animal.thirst == Constants.MaxThirst)
    assert(Seq(Young, Adult, Old).contains(animal.age))
  }

  test("An animal with 0 of health (or thirst) is dead") {
    assert(animal.isAlive)
    val a2 = animal.update(0, Constants.MaxThirst)
    assert(!a2.isAlive)
    val a3 = animal.update(Constants.MaxHealth, 0)
    assert(!a3.isAlive)
  }

  test("An animal can see only things within its visual range") {
    val a1: Animal = Animal(Species("tiger1", Medium, 10, 10, Carnivore), Point(0, 0))
    val a2: Animal = Animal(Species("tiger2", Small, 10, 10, Carnivore), Point(1, 1))
    val unreachableAnimal: Animal = Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(100, 100))
    assert(a1 canSee a2)
    assert(a2 canSee a1)
    assert(!(a1 canSee unreachableAnimal))
  }

  test("An animal can be shifted in a new position") {
    val newPosition = Point(100, 100)
    val shiftedAnimal = animal.shift(newPosition)
    assert(shiftedAnimal.position == newPosition)
  }

  test("A sated animal doesn't eat") {
    val satedAnimal = animal.update(Constants.MaxHealth)
    assert(satedAnimal.eat(food) == (satedAnimal, Option(food)))
  }

  test("An hungry animal eats all the food") {
    val hungryAnimal = animal.update(Constants.MaxHealth - decrement)
    val (animalAfterEat, foodAfterEat) = hungryAnimal.eat(food)
    assert(animalAfterEat.health == Constants.MaxHealth - decrement + food.quantity * food.energy)
    assert(foodAfterEat.isEmpty)
  }

  test("An animal doesn't eat all the food if it does not need it") {
    val notSoHungryAnimal = animal.update(Constants.MaxHealth - decrement)
    val moreFood = Food(FoodType(12, Meat), p, 10)
    val (animalAfterEat, foodAfterEat) = notSoHungryAnimal.eat(moreFood)
    assert(animalAfterEat.health == Constants.MaxHealth - decrement + 7 * moreFood.energy)
    assert(foodAfterEat.get.quantity == 3)
  }

  test("An animal that drink quench its thirst") {
    val thirstyAnimal = animal.update(thirst = Constants.MaxThirst / 2)
    val quenchedAnimal = thirstyAnimal.drink()
    assert(quenchedAnimal.thirst == Constants.MaxThirst)
  }

  test("An dying animal releases some meat") {
    assert(animal.die().foodCategory == Meat)
  }
}
