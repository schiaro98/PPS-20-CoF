package controller

import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

class FeedManagerTest extends AnyFunSuite{

  val animalQuantity = 10
  val foodQuantity = 10
  val animals: Seq[Animal] = Seq.fill(10)(Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(0,0)))
  val foodsFar: Seq[FoodInstance] = Seq.fill(10)(FoodInstance(Food(10, Meat),Point(100, 100), 1))
  val foodsNear: Seq[FoodInstance] = Seq.fill(10)(FoodInstance(Food(10, Meat),Point(2, 2), 1))
  val feedManager: FeedManager = FeedManager(animals, foodsFar)

  test("Animals should not eat food that is far away"){
    val foodsRemaining = feedManager.consumeResources()
    val animalsUpdated = feedManager.getAnimals

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
      val damagedAnimals = animals.map(animal => animal.update(health = 150))
      val feedManager: FeedManager = FeedManager(damagedAnimals, foodsNear)

      feedManager.lifeCycleUpdate()
      assert(feedManager.getAnimals.forall(_.health < Constants.MaxHealth))
      feedManager.consumeResources()
  }

  test("Animal should eat the meat"){
    var animals = Seq(Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(0,0)),
      Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(100,100)))

    val foods: Seq[FoodInstance] = Seq(FoodInstance(Food(10, Meat),Point(1, 1), 1),
                      FoodInstance(Food(10, Meat),Point(101, 101), 1))

    val feedManager: FeedManager = FeedManager(animals, foods)

    animals = feedManager.lifeCycleUpdate()

    val feedManager2: FeedManager = FeedManager(animals, foods)
    assert(feedManager2.getAnimals.forall(_.health == (Constants.MaxHealth - Constants.healthDecrease)))

    feedManager2.consumeResources()

    assert(feedManager.getAnimals.forall(_.health == Constants.MaxHealth))
  }
}
