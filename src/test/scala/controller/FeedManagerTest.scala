package controller

import controller.manager.FeedManager
import model.{animal, _}
import model.animal.{Animal, Carnivore, Medium, Species}
import model.food.{Food, FoodType, Meat}
import model.habitat.Habitat
import model.position.Point
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

class FeedManagerTest extends AnyFunSuite {

  val animalQuantity = 10
  val foodQuantity = 10
  val habitat: Habitat = Habitat()
  val animals: Seq[Animal] = Seq.fill(10)(animal.Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(0,0)))
  val foodsFar: Seq[Food] = Seq.fill(10)(Food(FoodType(10, Meat),Point(100, 100), 1))
  val foodsNear: Seq[Food] = Seq.fill(10)(Food(FoodType(10, Meat),Point(2, 2), 1))
  val feedManager: FeedManager = FeedManager(animals, foodsFar, habitat)

  test("Animals should not eat food that is far away"){
    val (animalsUpdated, foodsRemaining) = feedManager.consumeResources()

    assert(animalsUpdated.lengthCompare(animalQuantity) == 0)
    assert(foodsRemaining.lengthCompare(foodQuantity) == 0)

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
    val animals = Seq(animal.Animal(species, Point(0, 0), initialHealth), animal.Animal(species, Point(100, 100), initialHealth))
    val meat = FoodType(10, Meat)
    val foods: Seq[Food] = Seq(Food(meat, Point(1, 1), 1), Food(meat, Point(101, 101), 1))

    val feedManager: FeedManager = FeedManager(animals, foods, habitat)
    val (animalAfterFeed, _) = feedManager.consumeResources()
    assert(animalAfterFeed.forall(_.health > initialHealth))
  }
}
