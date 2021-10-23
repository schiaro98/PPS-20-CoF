package controller.manager

import model.Probability
import model.animal.{Adult, Age, Animal, Old, Species, Young}
import model.food.Food
import model.habitat.Habitat
import utility.{AnimalUtils, Constants, Logger, Statistics}

import scala.annotation.tailrec

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
  def lifeCycleUpdate(): (Seq[Animal], Seq[Food])

  /**
   * Calculate the unexpected events that can kill some [[Animal]]s, based on the dangerousness of the [[Habitat]].
   *
   * @param habitat the [[Habitat]] where the simulation takes place.
   * @return a pair with the alive [[Animal]]s and the food released if any of these died.
   */
  def unexpectedEvents(habitat: Habitat): (Seq[Animal], Seq[Food])
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


    override def lifeCycleUpdate(): (Seq[Animal], Seq[Food]) =
      updateAnimalAndInfo(
        (animal: Animal) => animal.update(animal.health - Constants.HealthDecrease, animal.thirst - Constants.ThirstDecrease),
        " died for natural causes")

    override def unexpectedEvents(habitat: Habitat): (Seq[Animal], Seq[Food]) =
      updateAnimalAndInfo(
        (animal: Animal) => if (probabilityBasedOnAge(habitat, animal).calculate) animal.update(health = 0) else animal,
        " died for an unexpected event")

    /**
     * Method used to update the animals in a certain way that can also cause death.
     *
     * @param update        how each [[Animal]] will be updated.
     * @param reasonOfDeath the reason why an [[Animal]] dies.
     * @return a pair with the alive and updated [[Animal]]s, and the food released if any of these died.
     */
    private def updateAnimalAndInfo(update: Animal => Animal, reasonOfDeath: String): (Seq[Animal], Seq[Food]) = {
      val updatedAnimals = animals.map(update)
      Statistics.update(deathForNaturalCause = updatedAnimals.count(!_.isAlive))
      updatedAnimals.filterNot(_.isAlive).foreach(animal => logger.info(animal.species.name + reasonOfDeath))
      (updatedAnimals.filter(_.isAlive), updatedAnimals.filterNot(_.isAlive).map(a => a.die()))
    }

    /**
     * Method used to obtain the probability that an unexpected event could happen could happen to an [[Animal]],
     * based on his [[Age]].
     *
     * @param animal the [[Animal]] to which an unexpected event could happen.
     * @return the [[Probability]] of the [[Animal]] to die.
     */
    private def probabilityBasedOnAge(habitat: Habitat, animal: Animal): Probability = {
      val increment = animal.age match {
        case Young => Constants.ProbabilityForYoung
        case Adult => Constants.ProbabilityForAdult
        case Old => Constants.ProbabilityForOld
      }
      habitat.unexpectedEvents.increase(increment)
    }

    override def generateInitialAnimals(population: Map[Species, Int], habitat: Habitat): AnimalManager = {
      @tailrec
      def _generateInitialAnimals(population: Seq[(Species, Int)], habitat: Habitat, animals: Seq[Animal] = Seq.empty): AnimalManager = population match {
        case (k, v) :: t =>
          _generateInitialAnimals(t, habitat, animals ++ _generateAnimalsInHabitat(k, v, habitat))
        case _ => AnimalManager(animals)
      }
      @tailrec
      def _generateAnimalsInHabitat(s: Species, quantity: Int, habitat: Habitat, animals: Seq[Animal] = Seq.empty): Seq[Animal] = quantity match {
        case x if x > 0 =>
          _generateAnimalsInHabitat(s, quantity - 1, habitat, animals :+ Animal(s, AnimalUtils.placeAnimal(habitat, s)))
        case _ => animals
      }
      _generateInitialAnimals(population.toSeq, habitat)
    }
  }
}
