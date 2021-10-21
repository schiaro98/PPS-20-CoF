package controller

import model._
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

  //TODO scaladoc
  def compute(animalManager: AnimalManager, resourceManager: ResourceManager): (AnimalManager, ResourceManager) = {
    growVegetableAfter(
      calculateUnexpectedEventsAfter(
        battleOf(
          updateHealthAndThirstOf(
            shiftedAnimals(animalManager, resourceManager)))))
  }

  //TODO scaladoc
  private def shiftedAnimals(animalManager: AnimalManager, resourceManager: ResourceManager): (AnimalManager, ResourceManager) = {
    //find destination
    val destinationManager: DestinationManager =
      DestinationManager(animalManager.animals, resourceManager.someFoods, habitat)
    val destinations: Map[Animal, Point] = destinationManager.calculateDestination()

    //animals movement
    val shiftManager = ShiftManager(habitat, destinations)
    shiftManager.walk()
    (AnimalManager(shiftManager.animals.toSeq), resourceManager)
  }

  //TODO scaladoc
  private def updateHealthAndThirstOf(managers: (AnimalManager, ResourceManager)): (AnimalManager, ResourceManager) = {
    val (animalManager, resourceManager) = managers
    //animals eat and drink
    val feedManager = FeedManager(animalManager.animals, resourceManager.someFoods, habitat)
    val (animalAfterFeed, foodAfterFeed) = feedManager.consumeResources()
    val animalManager1 = AnimalManager(animalAfterFeed)
    val resourceManager1 = resourceManager.someFoods_(foodAfterFeed)

    //animals life cycle
    val (animalAfterLifeCycle, foodAfterLifeCycle) = animalManager1.lifeCycleUpdate()
    (AnimalManager(animalAfterLifeCycle), resourceManager1.someFoods_(resourceManager1.someFoods ++ foodAfterLifeCycle))
  }

  //TODO scaladoc
  private def battleOf(managers: (AnimalManager, ResourceManager)): (AnimalManager, ResourceManager) = {
    val (animalManager, resourceManager) = managers
    val battleManager: BattleManager = BattleManager(animalManager.animals)
    val (animalAfterBattle, foodAfterBattle) = battleManager.battle()
    (AnimalManager(animalAfterBattle),  resourceManager.someFoods_(resourceManager.someFoods ++ foodAfterBattle))
  }

  //TODO scaladoc
  private def calculateUnexpectedEventsAfter(managers: (AnimalManager, ResourceManager)): (AnimalManager, ResourceManager) = {
    val (animalManager, resourceManager) = managers
    val (animalAfterUnexpectedEvents, foodAfterUnexpectedEvents) = animalManager.unexpectedEvents(habitat)
    (AnimalManager(animalAfterUnexpectedEvents), resourceManager.someFoods_(resourceManager.someFoods ++ foodAfterUnexpectedEvents))
  }

  //TODO scaladoc
  private def growVegetableAfter(managers: (AnimalManager, ResourceManager)): (AnimalManager, ResourceManager) = {
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