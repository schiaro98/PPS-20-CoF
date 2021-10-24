package controller

import controller.manager.AnimalManager
import model.animal.{Animal, Carnivore, Medium, Species}
import model.habitat.Habitat
import model.position.Point
import model.{Probability, animal}
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

class AnimalManagerTest extends AnyFunSuite {

  val numberOfAnimals = 10
  val tiger: Species = Species("tiger", Medium, 10, 10, Carnivore)
  val animals: Seq[Animal] = Seq.fill(numberOfAnimals)(animal.Animal(tiger, Point(0, 0)))

  test("A AnimalManager with a Seq of Animal can return that Seq") {
    val animalManager: AnimalManager = AnimalManager(animals)
    assert(animalManager.animals == animals)
  }

  test("At the beginning of the simulation there should be as many animals as specified by the user") {
    val habitat: Habitat = Habitat()
    val animalManager = AnimalManager().generateInitialAnimals(Map(tiger -> numberOfAnimals), habitat)
    assert(animalManager.animals.lengthCompare(numberOfAnimals) == 0)
    animalManager.animals.foreach(animal => assert(animal.species.name == tiger.name))
  }

  test("Every round all the animal should have decreased health and thirst") {
    var animalManager: AnimalManager = AnimalManager(animals)
    for (i <- 1 to 10) {
      val animalsUpdated = animalManager.lifeCycleUpdate()._1
      animalsUpdated.foreach(animal => {
        assert(animal.health == Constants.MaxHealth - (Constants.HealthDecrease * i))
        assert(animal.thirst == Constants.MaxThirst - (Constants.ThirstDecrease * i))
      })
      animalManager = AnimalManager(animalsUpdated)
    }
  }

  test("In a non dangerous habitat no animal dies") {
    val habitat: Habitat = model.habitat.Habitat(Probability(0), (0, 0), Seq.empty)
    val animalManager: AnimalManager = AnimalManager(animals)
    for (_ <- 1 to 10)
      assert(animalManager.unexpectedEvents(habitat)._1.lengthCompare(numberOfAnimals) == 0 )
  }

  test("In a very dangerous habitat animals die") {
    val habitat: Habitat = model.habitat.Habitat(Probability(100), (0, 0), Seq.empty)
    val animalManager: AnimalManager = AnimalManager(animals)
    for (_ <- 1 to 10)
      assert(animalManager.unexpectedEvents(habitat)._1.lengthCompare(numberOfAnimals) < 0 )
  }
}
