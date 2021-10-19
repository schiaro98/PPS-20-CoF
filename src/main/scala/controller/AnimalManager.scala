package controller

import model.{Animal, Habitat, Species}
import utility.{AnimalUtils, Constants}

/**
 * A controller of the [[Animal]]s present in the simulation.
 */
sealed trait AnimalManager {

  /**
   * The [[Animal]]s alive in the simulation.
   *
   * @return a Seq of [[Animal]].
   */
  def animals: Seq[Animal]

  /**
   * Method to create a number of animals for each species equal to the amount present in the [[Map]] received.
   *
   * @param population a Map containing all the [[Species]] with the number of animals to create at the beginning
   *                   of the simulation.
   * @param habitat    the [[Habitat]] where the simulation takes place.
   * @return the created animals.
   */
  def generateInitialAnimals(population: Map[Species, Int], habitat: Habitat): AnimalManager

  /**
   * Decrease thirst and health from all the animals
   *
   * @return a new [[AnimalManager]] with the updated values.
   */
  def lifeCycleUpdate(): AnimalManager //TODO aggiornare scaladoc dopo cambiamenti: far tornare una pair (AnimalManager,FoodInstance)
                                       // o solo FoodInstance e poi fare dal game un nuovo AnimalManager con i nuovi animali
}

/**
 * The object for the controller of the [[Animal]]s present in the simulation.
 */
object AnimalManager {

  /**
   * Apply method for [[AnimalManager]].
   *
   * @param animals the [[Animal]]s alive in the simulation.
   * @return a new implementation of [[AnimalManager]].
   */
  def apply(animals: Seq[Animal] = Seq.empty): AnimalManager = AnimalManagerImpl(animals)

  private case class AnimalManagerImpl(animals: Seq[Animal]) extends AnimalManager {

    override def generateInitialAnimals(population: Map[Species, Int], habitat: Habitat): AnimalManager = {
      var animals = Seq.empty[Animal]
      population foreach (s => {
        for (_ <- 1 to s._2) {
          val point = AnimalUtils.placeAnimal(habitat, s._1)
          val animal = Animal(s._1, point)
          animals = animals :+ animal
        }
      })
      AnimalManager(animals)
    }

    override def lifeCycleUpdate(): AnimalManager = {
      //TODO gestire le morti e ritornare anche la carne
      AnimalManager(animals.map(animal => animal.update(
        health = animal.health - Constants.healthDecrease,
        thirst = animal.thirst - Constants.thirstDecrease,
        position = animal.position
      )
      ))
    }
  }
}
