package controller

import model.{Animal, Carnivore, Medium, Point, Species}
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

class AnimalManagerTest extends AnyFunSuite {

  val animals: Seq[Animal] = Seq.fill(10)(Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(0,0)))

  //TODO aggiungi i test

  test("Animal should decreas health and thirst every round"){
    var animalsUpdated = animals
    for (i <- 1 to 10){
      val animalManager: AnimalManager = AnimalManager()
      animalsUpdated = animalManager.lifeCycleUpdate()._1
      animalsUpdated.foreach(animal => {
        assert(animal.health == Constants.MaxHealth - (Constants.healthDecrease * i))
        assert(animal.thirst == Constants.MaxThirst - (Constants.thirstDecrease * i))
      })
    }
  }
}
