package controller

import model.{Animal, Carnivore, Habitat, Medium, Point, Probability, Species}
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

class AnimalManagerTest extends AnyFunSuite {

  val numberOfAnimals = 10
  val tiger: Species = Species("tiger", Medium, 10, 10, Carnivore)
  val animals: Seq[Animal] = Seq.fill(numberOfAnimals)(Animal(tiger, Point(0, 0)))

  test("A AnimalManager with a Seq of Animal can return that Seq") {
    val animalManager: AnimalManager = AnimalManager(animals)
    assert(animalManager.animals == animals)
  }

  test("At the beginning of the simulation there should be as many animals as specified by the user") {
    val habitat: Habitat = Habitat()
    val animalManager = AnimalManager().generateInitialAnimals(Map(tiger -> numberOfAnimals), habitat)
    assert(animalManager.animals.size == numberOfAnimals)
    animalManager.animals.foreach(animal => assert(animal.name == tiger.name))
  }

  test("Every round all the animal should have decreased health and thirst") {
    var animalManager: AnimalManager = AnimalManager(animals)
    for (i <- 1 to 10) {
      val animalsUpdated = animalManager.lifeCycleUpdate()._1
      animalsUpdated.foreach(animal => {
        assert(animal.health == Constants.MaxHealth - (Constants.healthDecrease * i))
        assert(animal.thirst == Constants.MaxThirst - (Constants.thirstDecrease * i))
      })
      animalManager = AnimalManager(animalsUpdated)
    }
  }

  test("In a non dangerous habitat no animal dies") {
    val habitat: Habitat = Habitat(Probability(0), (0, 0), Seq.empty)
    val animalManager: AnimalManager = AnimalManager(animals)
    for (_ <- 1 to 10)
      assert(animalManager.unexpectedEvents(habitat)._1.size == numberOfAnimals )
  }

  test("In a very dangerous habitat animals die") {
    val habitat: Habitat = Habitat(Probability(100), (0, 0), Seq.empty)
    val animalManager: AnimalManager = AnimalManager(animals)
    for (_ <- 1 to 10)
      assert(animalManager.unexpectedEvents(habitat)._1.size < numberOfAnimals )
  }
}
