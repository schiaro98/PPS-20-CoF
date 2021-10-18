package controller

import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

import java.awt.Color

class FeedManagerTest extends AnyFunSuite{

  val animalQuantity = 10
  val foodQuantity = 10
  val animals: Seq[Animal] = Seq.fill(10)(Animal(Species("tiger", Medium, 10, 10, Carnivore, Color.WHITE), Point(0,0)))
  val foodsFar: Seq[FoodInstance] = Seq.fill(10)(FoodInstance(Food(Color.black, 10, Meat),Point(100, 100), 1))
  val foodsNear: Seq[FoodInstance] = Seq.fill(10)(FoodInstance(Food(Color.black, 10, Meat),Point(2, 2), 1))

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

  test("Animal should decreas health and thirst every round"){
    var animalsUpdated = animals
    for (i <- 1 to 10){
      val feedManager: FeedManager = FeedManager(animalsUpdated, foodsFar)
      animalsUpdated = feedManager.lifeCycleUpdate()
      animalsUpdated.foreach(animal => {
        assert(animal.health == Constants.MaxHealth - (Constants.healthDecrease * i))
        assert(animal.thirst == Constants.MaxThirst - (Constants.thirstDecrease * i))
      })
    }
  }

  test("Animal should eat near food"){
      val damagedAnimals = animals.map(animal => animal.update(health = Constants.MaxHealth / 2))
      val feedManager: FeedManager = FeedManager(damagedAnimals, foodsNear)
      val result = feedManager.consumeResources()
      val (animalsUpdated, foodsRemaining) = result

      assert(animalsUpdated.length == animalQuantity)

      animalsUpdated.foreach(animal => {
        assert(animal.health == (Constants.MaxHealth / 2) + (foodsNear.head.energy * foodsNear.head.quantity))
      })
  }
}
