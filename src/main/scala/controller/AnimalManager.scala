package controller

import model.{Animal, FoodInstance, Habitat, Species}
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
   * Decrease thirst and health from all the [[Animal]]s; if an animal dies of hunger or thirst
   * it will release som meat into its position.
   *
   * @return a pair with the alive, updated [[Animal]]s and the food that was released.
   */
  def lifeCycleUpdate(): (Seq[Animal], Seq[FoodInstance])
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

    override def lifeCycleUpdate(): (Seq[Animal], Seq[FoodInstance]) = {
      val updatedAnimals = animals.map(animal => {
        animal.update(
          health = animal.health - Constants.healthDecrease,
          thirst = animal.thirst - Constants.thirstDecrease
        )
      })
      (updatedAnimals.filter(_.isAlive), updatedAnimals.filter(!_.isAlive).map(a => a.die()))
    }
  }
}
