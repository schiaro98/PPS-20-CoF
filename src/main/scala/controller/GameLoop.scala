package controller

import controller.manager.{AnimalManager, BattleManager, DestinationManager, FeedManager, ResourceManager, ShiftManager}
import model.animal.{Animal, Species}
import model.habitat.Habitat
import model.position.Point
import utility.{Constants, Logger, Statistics}
import view.{SimulationGui, StatisticsGUI}

import scala.annotation.tailrec

/**
 * [[Runnable]] used to start the simulation, it contains the game loop which must be executed in a new thread.
 *
 * @param population all the [[Species]] with the number of animals to create at the beginning of the simulation.
 * @param habitat    the [[Habitat]] where the simulation takes place.
 */
case class GameLoop(population: Map[Species, Int], habitat: Habitat) extends Runnable {

  var isPaused: Boolean = false
  var isSpeedUp: Boolean = false
  var isStopped: Boolean = false
  private val logger = Logger

  /**
   * Method that represents the core of the simulation, defines the actions that must be
   * carried out at each unit of time.
   */
  override def run(): Unit = {
    @tailrec
    def loop(animalManager: AnimalManager, resourceManager: ResourceManager, simulationGui: SimulationGui): Unit = {
      val current: Long = System.currentTimeMillis()
      if (!isPaused) {
        val (newAnimalManager, newResourceManager) = compute(animalManager, resourceManager)

        simulationGui.updatePanel(newAnimalManager.animals, newResourceManager.someFoods)
        simulationGui.updateElapsedTime()

        waitForNextStep(current)
        Statistics.incTime()
        if (newAnimalManager.animals.lengthIs == 0 || isStopped) return
        loop(newAnimalManager, newResourceManager, simulationGui)
      } else {
        waitForNextStep(current)
        if (animalManager.animals.lengthIs == 0 || isStopped) return
        loop(animalManager, resourceManager, simulationGui)
      }
    }

    val animalManager = AnimalManager().generateInitialAnimals(population, habitat)
    val resourceManager = ResourceManager(habitat, Constants.FoodsFilePath).fillHabitat()
    val simulationGui = new SimulationGui(habitat, setPaused, setSpeed, stop) { top.visible = true }
    simulationGui.updatePanel(animalManager.animals, resourceManager.someFoods)

    loop(animalManager, resourceManager, simulationGui)

    logger.info("Simulation finished")
    simulationGui.disableAllButton()
    val f = new StatisticsGUI
    f.top
  }

  /**
   * Method that permit to wait the time required before running the cycle again.
   *
   * @param current time at the beginning of the cycle.
   */
  private def waitForNextStep(current: Long): Unit = {
    val dt = System.currentTimeMillis() - current
    val period = if (isSpeedUp) Constants.SpeedUpPeriod else Constants.Period
    if (dt < period) {
      Thread.sleep(Constants.Period - dt)
    }
  }

  /**
   * Method that contains all the calculations that are performed in one step of the simulation.
   *
   * @param animalManager the [[AnimalManager]] of this step.
   * @param resourceManager the [[ResourceManager]] of this step.
   * @return the [[AnimalManager]] and the [[ResourceManager]] for the next step.
   */
  def compute(animalManager: AnimalManager, resourceManager: ResourceManager): (AnimalManager, ResourceManager) = {
    growFoodAfter(
      calculateUnexpectedEventsAfter(
        battlesOf(
          updateHealthAndThirstOf(
            shiftedAnimals(animalManager, resourceManager)))))
  }

  /**
   * Method used to find a destination for the [[Animal]]s of the simulation and move them towards it.
   *
   * @param animalManager the [[AnimalManager]] at the beginning of the simulation.
   * @param resourceManager the [[ResourceManager]] at the beginning of the simulation.
   * @return the [[AnimalManager]] with the shifted [[Animal]]s and the initial [[ResourceManager]].
   */
  private def shiftedAnimals(animalManager: AnimalManager, resourceManager: ResourceManager): (AnimalManager, ResourceManager) = {
    //find destination
    val destinationManager: DestinationManager = DestinationManager(animalManager.animals, resourceManager.someFoods, habitat)
    val destinations: Map[Animal, Point] = destinationManager.calculateDestination()
    //animals movement
    val shiftManager = ShiftManager(habitat, destinations)
    shiftManager.walk()
    (AnimalManager(shiftManager.animals.toSeq), resourceManager)
  }

  /**
   * Method used to make [[Animal]]s eat and drink and update their parameters due to the cycle of life.
   *
   * @param managers the [[AnimalManager]] and the [[ResourceManager]] after the animal shift.
   * @return the [[AnimalManager]] with the updated [[Animal]]s and a [[ResourceManager]] in which the meat of the
   *         dead [[Animal]]s was added.
   */
  private def updateHealthAndThirstOf(managers: (AnimalManager, ResourceManager)): (AnimalManager, ResourceManager) = {
    val (animalManager, resourceManager) = managers
    //animals eat and drink
    val feedManager = FeedManager(animalManager.animals, resourceManager.someFoods, habitat)
    val (animals, foods) = feedManager.consumeResources()
    val newAnimalManager = AnimalManager(animals)
    val newResourceManager = resourceManager.someFoods_(foods)
    //animals life cycle
    val (animalsUpdated, foodsUpdated) = newAnimalManager.lifeCycleUpdate()
    (AnimalManager(animalsUpdated), newResourceManager.someFoods_(newResourceManager.someFoods ++ foodsUpdated))
  }

  /**
   * Method used to make [[Animal]]s battle.
   *
   * @param managers the [[AnimalManager]] and the [[ResourceManager]] after the health and thirst update.
   * @return the [[AnimalManager]] with the alive [[Animal]]s and a [[ResourceManager]] in which the meat of the
   *         dead [[Animal]]s was added.
   */
  private def battlesOf(managers: (AnimalManager, ResourceManager)): (AnimalManager, ResourceManager) = {
    val (animalManager, resourceManager) = managers
    val battleManager: BattleManager = BattleManager(animalManager.animals)
    val (animals, foods) = battleManager.battle()
    (AnimalManager(animals),  resourceManager.someFoods_(resourceManager.someFoods ++ foods))
  }

  /**
   * Method that calculate some unexpected events that can kill some [[Animal]]s.
   *
   * @param managers the [[AnimalManager]] and the [[ResourceManager]] after the battles.
   * @return the [[AnimalManager]] with the alive [[Animal]]s and a [[ResourceManager]] in which the meat of the
   *         dead [[Animal]]s was added.
   */
  private def calculateUnexpectedEventsAfter(managers: (AnimalManager, ResourceManager)): (AnimalManager, ResourceManager) = {
    val (animalManager, resourceManager) = managers
    val (animals, foods) = animalManager.unexpectedEvents(habitat)
    (AnimalManager(animals), resourceManager.someFoods_(resourceManager.someFoods ++ foods))
  }

  /**
   * Method used to grow food in the [[Habitat]].
   *
   * @param managers the [[AnimalManager]] and the [[ResourceManager]] after the unexpected events.
   * @return the [[AnimalManager]] with the alive [[Animal]]s of this step and a [[ResourceManager]] in which the
   *         food grown in this step was added.
   */
  private def growFoodAfter(managers: (AnimalManager, ResourceManager)): (AnimalManager, ResourceManager) = {
    val (animalManager, resourceManager) = managers
    (animalManager, resourceManager.grow())
  }

  /**
   * Method to pause/unpause the simulation.
   *
   * @param paused true to pause the simulation, false to unpause.
   */
  def setPaused(paused: Boolean): Unit = isPaused = paused

  /**
   * Method to speed up and slow down the simulation.
   *
   * @param speedUp true to speed up the simulation, false to slow down.
   */
  def setSpeed(speedUp: Boolean): Unit = isSpeedUp = speedUp

  /**
   * Method used to stop the simulation.
   */
  def stop(): Unit = isStopped = true
}