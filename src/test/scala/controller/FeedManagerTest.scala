package controller

import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

class FeedManagerTest extends AnyFunSuite {

  val animalQuantity = 10
  val foodQuantity = 10
  val animals: Seq[Animal] = Seq.fill(10)(Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(0,0)))
  val foodsFar: Seq[FoodInstance] = Seq.fill(10)(FoodInstance(Food(10, Meat),Point(100, 100), 1))
  val foodsNear: Seq[FoodInstance] = Seq.fill(10)(FoodInstance(Food(10, Meat),Point(2, 2), 1))

  val feedManager: FeedManager = FeedManager(animals, foodsFar)

  test("Animals should not eat food that is far away"){
    val result = feedManager.consumeResources()
    val (animalsUpdated, foodsRemaining) = result

    assert(animalsUpdated.length == animalQuantity)
    assert(foodsRemaining.length == foodQuantity)

    animalsUpdated.foreach(animal => {
      assert(animal.health == Constants.MaxHealth)
    })
  }

  test("Animal should eat near food"){
      val damagedAnimals = animals.map(animal => animal.update(health = Constants.MaxHealth / 2))
      val feedManager: FeedManager = FeedManager(damagedAnimals, foodsNear)
      val result = feedManager.consumeResources()
      val (animalsUpdated, _) = result

      assert(animalsUpdated.length == animalQuantity)

      animalsUpdated.foreach(animal => {
        assert(animal.health == (Constants.MaxHealth / 2) + (foodsNear.head.energy * foodsNear.head.quantity))
      })
  }
}
