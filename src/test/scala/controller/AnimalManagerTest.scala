package controller

import model.{Animal, Carnivore, Habitat, Medium, Point, Species}
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

class AnimalManagerTest extends AnyFunSuite {

  val tiger: Species = Species("tiger", Medium, 10, 10, Carnivore)
  val animals: Seq[Animal] = Seq.fill(10)(Animal(tiger, Point(0,0)))
  val habitat: Habitat = Habitat()

  //TODO aggiungi i test

  test("At the beginning of the simulation there should be as many animals as specified by the user") {
    val animalManager = AnimalManager().generateInitialAnimals(Map(tiger -> 10), habitat)
    assert(animalManager.animals.size == 10)
    animalManager.animals.foreach(animal => assert(animal.name == tiger.name))
  }

  test("Every round all the animal should have decreased health and thirst") {
    var animalManager: AnimalManager = AnimalManager(animals)
    for (i <- 1 to 10){
      val animalsUpdated = animalManager.lifeCycleUpdate()._1
      animalsUpdated.foreach(animal => {
        assert(animal.health == Constants.MaxHealth - (Constants.healthDecrease * i))
        assert(animal.thirst == Constants.MaxThirst - (Constants.thirstDecrease * i))
      })
      animalManager = AnimalManager(animalsUpdated)
    }
  }
}
