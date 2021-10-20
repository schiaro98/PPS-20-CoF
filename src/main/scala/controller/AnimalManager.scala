package controller

import model.{Animal, FoodInstance, Habitat, Species}
import utility.{AnimalUtils, Constants, Logger}

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
   * @return a pair with the alive and updated [[Animal]]s, and the food released if any of these died.
   */
  def lifeCycleUpdate(): (Seq[Animal], Seq[FoodInstance])

  /**
   * Calculate the unexpected events that can kill some [[Animal]]s, based on the dangerousness of the [[Habitat]].
   *
   * @param habitat the [[Habitat]] where the simulation takes place.
   * @return a pair with the alive [[Animal]]s and the food released if any of these died.
   */
  def unexpectedEvents(habitat: Habitat): (Seq[Animal], Seq[FoodInstance])
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

    private val logger = Logger

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

    override def lifeCycleUpdate(): (Seq[Animal], Seq[FoodInstance]) =
      updateAnimalAndInfo(
        (animal: Animal) => animal.update(animal.health - Constants.healthDecrease, animal.thirst - Constants.thirstDecrease),
        " died for natural causes")

    override def unexpectedEvents(habitat: Habitat): (Seq[Animal], Seq[FoodInstance]) =
      updateAnimalAndInfo(
        (animal: Animal) => if (habitat.unexpectedEvents.calculate) animal.update(health = 0) else animal,
        " died for an unexpected event")

    /**
     * Method used to update the animals in a certain way that can also cause death.
     *
     * @param update how each [[Animal]] will be updated
     * @param reasonOfDeath the reason why an [[Animal]] dies
     * @return a pair with the alive and updated [[Animal]]s, and the food released if any of these died.
     */
    private def updateAnimalAndInfo(update: Animal => Animal, reasonOfDeath: String): (Seq[Animal], Seq[FoodInstance]) = {
      val updatedAnimals = animals.map(update)
      updatedAnimals.filter(!_.isAlive).foreach(animal => logger.info(animal.name + reasonOfDeath))
      (updatedAnimals.filter(_.isAlive), updatedAnimals.filter(!_.isAlive).map(a => a.die()))
    }
  }
}
