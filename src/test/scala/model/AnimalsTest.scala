package model

import org.scalatest.funsuite.AnyFunSuite
import utility.Constants._

import java.awt.Color

class AnimalsTest extends AnyFunSuite{

  val color: Color = Color.black
  val p: Point = Point(0,0)
  val a: Animal = Animal(Species("tiger", Medium, 10, 10, Carnivore, color), p)

  test("Test if two animals sees each other"){
    val a1: Animal = Animal(Species("tiger1", Medium, 10, 10, Carnivore), Point(0,0))
    val a2: Animal = Animal(Species("tiger2", Small, 10, 10, Carnivore), Point(1,1))
    val unreachableAnimal: Animal = Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(100,100))
    assert(a1 canSee  a2)
    assert(a2 canSee a1)
    assert(!(a1 canSee unreachableAnimal))
  }

  test("An animal is an entity with personal parameters and belong to a species") {
    assert(color == a.color)
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

  //TODO controlla test del cibo mangiato

  test("An animal that eats regains health") {
    val animal0 = a.update(MaxHealth, MaxThirst)
    val food1 = FoodInstance(Food(10,Meat), p, 5)
    assert((animal0, Option(food1)) == animal0.eat(food1))

    val animal1 = a.update(MaxHealth - 90, MaxThirst)
    val res1 = animal1.eat(food1)
    assert((MaxHealth - 40, None) == (res1._1.health, res1._2))

    val animal2 = a.update(MaxHealth - 90, MaxThirst)
    val food2 = FoodInstance(Food(12,Meat), p, 10)
    val res2 = animal2.eat(food2)
    assert((MaxHealth, 2) == (res2._1.health, res2._2.get.quantity))
  }
}
