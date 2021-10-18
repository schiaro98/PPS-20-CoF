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

  var animalsInMap: Seq[Animal] = AnimalUtils.generateInitialAnimals(population, habitat)

  /**
   * Method that represents the core of the simulation, defines the actions that must be
   * carried out at each unit of time.
   */
  override def run(): Unit = {
    val shapePanel = new SimulationPanel(habitat.dimensions._1, habitat.dimensions._2)
    val simulationGui = new SimulationGui(habitat, shapePanel) {
      top.visible = true
    }
    var resourceManager = ResourceManager(habitat, Constants.FoodsFilePath)


    simulationGui.updatePanel(animalsInMap, resourceManager.foodInstances)
    var previous: Long = System.currentTimeMillis()

    while (animalsInMap.lengthIs > 0) { //TODO pausa come fermare il gioco senza sprecare cpu?
      val current: Long = System.currentTimeMillis()

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

      //Calcolo eventi inaspettati

      simulationGui.updatePanel(animalsInMap, resourceManager.foodInstances)

      resourceManager = resourceManager.grow()

      simulationGui.updateElapsedTime()
      //Contatore epoche che passano
      waitForNextFrame(current)
      previous = current
    }

    println("All the animals are dead")
    //mostrare la gui con il riassunto ?
  }

  /**
   * Method that permit to wait the current time to render the following frame.
   *
   * @param current time at the beginning of current frame.
   */
  private def waitForNextFrame(current: Long): Unit = {
    val dt = System.currentTimeMillis() - current
    if (dt < Constants.Period) {
      Thread.sleep(Constants.Period - dt)
    }
  }
}


