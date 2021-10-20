package controller

import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

class FeedManagerTest extends AnyFunSuite {

  val animalQuantity = 10
  val foodQuantity = 10
  val habitat: Habitat = Habitat()
  val animals: Seq[Animal] = Seq.fill(10)(Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(0,0)))
  val foodsFar: Seq[FoodInstance] = Seq.fill(10)(FoodInstance(Food(10, Meat),Point(100, 100), 1))
  val foodsNear: Seq[FoodInstance] = Seq.fill(10)(FoodInstance(Food(10, Meat),Point(2, 2), 1))
  val feedManager: FeedManager = FeedManager(animals, foodsFar, habitat)

  test("Animals should not eat food that is far away"){
    val (animalsUpdated, foodsRemaining) = feedManager.consumeResources()

    assert(animalsUpdated.length == animalQuantity)
    assert(foodsRemaining.length == foodQuantity)

    animalsUpdated.foreach(animal => {
      assert(animal.health == Constants.MaxHealth)
    })
  }

  test("Animal should eat near food"){
      val damagedAnimals = animals.map(animal => animal.update(health = 150))
      val feedManager: FeedManager = FeedManager(damagedAnimals, foodsNear, habitat)
      val (animalsUpdated, _) = feedManager.consumeResources()

      assert(animalsUpdated.forall(_.health < Constants.MaxHealth))
      feedManager.consumeResources()
  }

  test("Animal should eat the meat"){
    val animals = Seq(Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(0, 0)),
      Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(100, 100)))

    val foods: Seq[FoodInstance] = Seq(FoodInstance(Food(10, Meat),Point(1, 1), 1),
                      FoodInstance(Food(10, Meat),Point(101, 101), 1))

    val animalManager = AnimalManager(animals)
    val (animalsUpdated, food) = animalManager.lifeCycleUpdate()
    val feedManager: FeedManager = FeedManager(animalsUpdated, foods, habitat)
    val (animalsUpdated2, _) = feedManager.consumeResources()
    assert(animalsUpdated2.forall(_.health == Constants.MaxHealth))
  }
}
