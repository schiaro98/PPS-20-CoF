package controller

import model._
import utility.{Constants, Logger, Statistics}
import view.{SimulationGui, StatisticsGUI}

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
    //reusable manager
    var animalManager = AnimalManager().generateInitialAnimals(population, habitat)
    var resourceManager = ResourceManager(habitat, Constants.FoodsFilePath).fillHabitat()

    //GUI
    val simulationGui = new SimulationGui(habitat, setPaused, setSpeed, stop) { top.visible = true }
    simulationGui.updatePanel(animalManager.animals, resourceManager.foodInstances)

    var previous: Long = System.currentTimeMillis()
    while (animalManager.animals.lengthIs > 0 && !isStopped) {
      val current: Long = System.currentTimeMillis()
      if (!isPaused) {
        //find destination
        val destinationManager: DestinationManager =
          DestinationManager(animalManager.animals, resourceManager.foodInstances, habitat)
        val destinations: Map[Animal, Point] = destinationManager.calculateDestination()

        //animals movement
        val shiftManager = ShiftManager(habitat, destinations)
        shiftManager.walk()
        animalManager = AnimalManager(shiftManager.animals.toSeq)

        //animals eat and drink
        val feedManager = FeedManager(animalManager.animals, resourceManager.foodInstances, habitat)
        val feedResult = feedManager.consumeResources()
        animalManager = AnimalManager(feedResult._1)
        resourceManager = resourceManager.foodInstances_(feedResult._2)

        //animals life cycle
        val (animalAfterLifeCycle, foodAfterLifeCycle) = animalManager.lifeCycleUpdate()
        animalManager = AnimalManager(animalAfterLifeCycle)
        resourceManager = resourceManager.foodInstances_(resourceManager.foodInstances ++ foodAfterLifeCycle)

        //animals battle
        val battleManager: BattleManager = BattleManager(animalManager.animals)
        val result = battleManager.battle()
        animalManager = AnimalManager(result._1)
        resourceManager = resourceManager.foodInstances_(resourceManager.foodInstances ++ result._2)

        //animals killed by unexpected events
        val (animalAfterUnexpectedEvents, foodAfterUnexpectedEvents) = animalManager.unexpectedEvents(habitat)
        animalManager = AnimalManager(animalAfterUnexpectedEvents)
        resourceManager = resourceManager.foodInstances_(resourceManager.foodInstances ++ foodAfterUnexpectedEvents)

        //vegetable growth
        resourceManager = resourceManager.grow()

        //update
        simulationGui.updatePanel(animalManager.animals, resourceManager.foodInstances)
        simulationGui.updateElapsedTime()
      }
      waitForNextStep(current)
      previous = current
      Statistics.incTime()
    }
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


