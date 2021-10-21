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

  test("A Carnivore should eat the meat"){
    val initialHealth = 50
    val species = Species("tiger", Medium, 10, 10, Carnivore)
    val animals = Seq(Animal(species, Point(0, 0), initialHealth), Animal(species, Point(100, 100), initialHealth))
    val meat = Food(10, Meat)
    val foods: Seq[FoodInstance] = Seq(FoodInstance(meat, Point(1, 1), 1), FoodInstance(meat, Point(101, 101), 1))

    val feedManager: FeedManager = FeedManager(animals, foods, habitat)
    val (animalAfterFeed, _) = feedManager.consumeResources()
    assert(animalAfterFeed.forall(_.health > initialHealth))
  }
}
