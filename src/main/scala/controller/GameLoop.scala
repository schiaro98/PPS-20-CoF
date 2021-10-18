package controller

import model._
import utility.{AnimalUtils, Constants, Point}
import view.{SimulationGui, SimulationPanel}

/**
 * [[Runnable]] used to start the simulation, it contains the game loop which must be executed in a new thread.
 *
 * @param population all the [[Species]] with the number of animals to create at the beginning of the simulation.
 * @param habitat the [[Habitat]] where the simulation takes place.
 */
case class GameLoop(population: Map[Species, Int], habitat: Habitat) extends Runnable {

  var foodInMap: Seq[FoodInstance] = Seq.empty //TODO da prendere da resource manager
  var animalsInMap: Seq[Animal] = AnimalUtils.generateInitialAnimals(population, habitat)

  /**
   * Method that represents the core of the simulation, defines the actions that must be
   * carried out at each unit of time.
   */
  override def run(): Unit = {
    val shapePanel = new SimulationPanel(habitat.dimensions._1, habitat.dimensions._2)
    val simulationGui = new SimulationGui(habitat, shapePanel) { top.visible = true }
    var resourceManager = ResourceManager(habitat)

    simulationGui.updatePanel(animalsInMap, foodInMap)
    var previous: Long = System.currentTimeMillis()

    while (animalsInMap.lengthIs > 0) { //TODO pausa come fermare il gioco senza sprecare cpu?
      val current: Long = System.currentTimeMillis()

      val destinationManager: DestinationManager = DestinationManager(animalsInMap, foodInMap, habitat)
      val destinations: Map[Animal, Point] = destinationManager.calculateDestination()

      val shiftManager = ShiftManager(habitat, destinations)
      shiftManager.walk()
      animalsInMap = shiftManager.animals.toSeq

      //TODO far mangiare e bere gli animali che possono raggiungere le risorse
      // decrementare sete e fame da tutti gli animali

      val battleManager: BattleManager = BattleManager(animalsInMap)

      //resourceManager.takeInput(battleManager.battle()
      animalsInMap = battleManager.getAnimals
      //Resource manager dovrebbe prendere in input la carne risultando dal battleManager

      //Calcolo eventi inaspettati

      simulationGui.updatePanel(animalsInMap, foodInMap)

      resourceManager = resourceManager.grow() //TODO togliere foodinmap?

      //Contatore epoche che passano




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
    println("Time elapsed for the computation: " + dt)
    if (dt < Constants.Period) {
      Thread.sleep(Constants.Period - dt)
    }
  }
}


