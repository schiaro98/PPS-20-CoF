package controller

import controller.manager._
import model.animal.{Animal, Species}
import model.habitat.Habitat
import model.position.Point
import utility.{Constants, Logger, Statistics}
import view.{SimulationGUI, StatisticsGUI}

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

  /**
   * Method that represents the core of the simulation, defines the actions that must be
   * carried out at each unit of time.
   */
  override def run(): Unit = {
    @tailrec
    def _loop(animalManager: AnimalManager, resourceManager: ResourceManager, simulationGui: SimulationGUI): Unit = {
      val current: Long = System.currentTimeMillis()
      isPaused match {
        case false =>
          val (newAnimalManager, newResourceManager) = compute(animalManager, resourceManager)
          update(newAnimalManager, newResourceManager, simulationGui)
          waitForNextStep(current)
          if (newAnimalManager.animals.lengthIs == 0 || isStopped) return
          _loop(newAnimalManager, newResourceManager, simulationGui)

        case true =>
          waitForNextStep(current)
          if (animalManager.animals.lengthIs == 0 || isStopped) return
          _loop(animalManager, resourceManager, simulationGui)
      }
    }

    val animalManager = AnimalManager().generateInitialAnimals(population, habitat)
    val resourceManager = ResourceManager(habitat, Constants.FoodsFilePath).fillHabitat()
    val simulationGui = new SimulationGUI(habitat, setPaused, setSpeed, stop) { top.visible = true }
    simulationGui.updatePanel(animalManager.animals, resourceManager.foods)

    _loop(animalManager, resourceManager, simulationGui)

    Logger.info("Simulation finished")
    simulationGui.disableAllButton()
    StatisticsGUI().show()
  }

  /**
   * Update the GUI of the simulation and increment the time.
   *
   * @param animalManager   the [[AnimalManager]] of this step.
   * @param resourceManager the [[ResourceManager]] of this step.
   * @param simulationGui   the [[SimulationGUI]].
   */
  private def update(animalManager: AnimalManager, resourceManager: ResourceManager, simulationGui: SimulationGUI): Unit = {
    simulationGui.updatePanel(animalManager.animals, resourceManager.foods)
    simulationGui.updateElapsedTime()
    Statistics.incTime()
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
   * @param animalManager   the [[AnimalManager]] of this step.
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
   * @param animalManager   the [[AnimalManager]] at the beginning of the simulation.
   * @param resourceManager the [[ResourceManager]] at the beginning of the simulation.
   * @return the [[AnimalManager]] with the shifted [[Animal]]s and the initial [[ResourceManager]].
   */
  private def shiftedAnimals(animalManager: AnimalManager, resourceManager: ResourceManager): (AnimalManager, ResourceManager) = {
    //find destination
    val destinationManager: DestinationManager = DestinationManager(animalManager.animals, resourceManager.foods, habitat)
    val destinations: Map[Animal, Point] = destinationManager.calculateDestination()
    //animals movement
    val shiftManager = ShiftManager(habitat, destinations).walk()
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
    val (animals, foods) = FeedManager(animalManager.animals, resourceManager.foods, habitat).consumeResources()
    val newAnimalManager = AnimalManager(animals)
    val newResourceManager = resourceManager.foods_(foods)
    //animals life cycle
    val (animalsUpdated, foodsUpdated) = newAnimalManager.lifeCycleUpdate()
    (AnimalManager(animalsUpdated), newResourceManager.foods_(newResourceManager.foods ++ foodsUpdated))
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
    (AnimalManager(animals), resourceManager.foods_(resourceManager.foods ++ foods))
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
    (AnimalManager(animals), resourceManager.foods_(resourceManager.foods ++ foods))
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
  private def setPaused(paused: Boolean): Unit = isPaused = paused

  /**
   * Method to speed up and slow down the simulation.
   *
   * @param speedUp true to speed up the simulation, false to slow down.
   */
  private def setSpeed(speedUp: Boolean): Unit = isSpeedUp = speedUp

  /**
   * Method used to stop the simulation.
   */
  private def stop(): Unit = isStopped = true
}