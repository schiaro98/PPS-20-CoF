package controller

import model._
import utility.{AnimalUtils, Constants}
import view.{SimulationGui, SimulationPanel}

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
  var animalsInMap: Seq[Animal] = AnimalUtils.generateInitialAnimals(population, habitat)

  /**
   * Method that represents the core of the simulation, defines the actions that must be
   * carried out at each unit of time.
   */
  override def run(): Unit = {
    val shapePanel = new SimulationPanel(habitat.dimensions._1, habitat.dimensions._2)
    val simulationGui = new SimulationGui(habitat, shapePanel, setPaused, setSpeed, stop) {top.visible = true}
    var resourceManager = ResourceManager(habitat, Constants.FoodsFilePath)
    simulationGui.updatePanel(animalsInMap, resourceManager.foodInstances)

    var previous: Long = System.currentTimeMillis()
    while (animalsInMap.lengthIs > 0 && !isStopped) {
      val current: Long = System.currentTimeMillis()
      if (!isPaused) {
        val destinationManager: DestinationManager = DestinationManager(animalsInMap, resourceManager.foodInstances, habitat)
        val destinations: Map[Animal, Point] = destinationManager.calculateDestination()

        val shiftManager = ShiftManager(habitat, destinations)
        shiftManager.walk()
        animalsInMap = shiftManager.animals.toSeq

        val feedManager = FeedManager(animalsInMap, resourceManager.foodInstances)

        println("Updated animals after shiftmanager", animalsInMap.length)
        val (_, remainedFood) = feedManager.consumeResources()
        animalsInMap = feedManager.lifeCycleUpdate()

        println("Updated animals after feedmanager" + animalsInMap.length)

        val battleManager: BattleManager = BattleManager(animalsInMap)
        val battleFood = battleManager.battle()

        resourceManager = resourceManager.foodInstances_(battleFood ++ remainedFood)

        println("Updated food after feed and battle", resourceManager.foodInstances.length)

        animalsInMap = battleManager.getAnimals
        println("Updated animals after battle", animalsInMap.length)

        //TODO Calcolo eventi inaspettati

        resourceManager = resourceManager.grow()

        simulationGui.updatePanel(animalsInMap, resourceManager.foodInstances)
        simulationGui.updateElapsedTime()
      }
      waitForNextStep(current)
      previous = current
    }

    println("Simulation finished")
    //TODO mostrare la gui con il riassunto
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


