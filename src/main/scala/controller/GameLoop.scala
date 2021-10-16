package controller

import model._
import utility.{AnimalUtils, Constants, Point}
import view.{Rectangle, SimulationGui, SimulationPanel}

import scala.util.Random

/**
 * Runnable used to start the simulation, it contains the game loop which must be executed in a new thread.
 *
 * @param species the [[Species]] with the number of animals to create at the beginning of the simulation.
 * @param habitat the [[Habitat]] where the simulation takes place.
 */
case class GameLoop(species: Map[Species, Int], habitat: Habitat) extends Runnable {

  var animalsAndRectangles: Map[Animal, Rectangle] = Map.empty[Animal, Rectangle] //TODO Point al posto di Rectangle ?
  var foodInMap: Seq[FoodInstance] = generateInitialFood()
  val animalsInMap: Seq[Animal] = generateInitialAnimals()
  val battleManager: BattleManager = BattleManager(animalsInMap)
  val shiftManager: ShiftManager = ShiftManager(habitat, Map.empty[Animal, Point])

  /**
   * Method that represents the core of the simulation, defines the actions that must be
   * carried out at each unit of time.
   */
  override def run(): Unit = {
    val shapePanel = new SimulationPanel(habitat.dimensions._1, habitat.dimensions._2)
    val simulationGui = new SimulationGui(habitat, shapePanel) { top.visible = true }
    simulationGui.updatePanel(animalsAndRectangles, foodInMap)

    var previous: Long = System.currentTimeMillis()
    while (animalsInMap.lengthIs > 1) { //TODO pausa come fermare il gioco senza sprecare cpu?
      val current: Long = System.currentTimeMillis()
      val elapsed: Double = (current - previous).toDouble / Constants.MillisToSec

      //Prendo dallo shiftmanager il primo movimento per ogni animale
      //TODO shift manager dovrebbe gestire l'eat se l'animale finisce vicino al cibo e acqua??
      //Non credo sia proprio lo shift a dover vedere sta cosa ma vabbe
      battleManager.calculateBattles()
      //Calcolo eventi inaspettati
      //crescita casuale dei vegetali



      simulationGui.updatePanel(animalsAndRectangles, foodInMap)



      // ---- solo per vedere che la gui cambia----|
      animalsAndRectangles = Map.empty //          |
      generateInitialAnimals() //                  |
      foodInMap = Seq.empty //                     |
      foodInMap = generateInitialFood() //         |
      // ---- solo per vedere che la gui cambia----|



      waitForNextFrame(current)
      previous = current
    }

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

  private def generateInitialFood(): Seq[FoodInstance] = {
    Seq.empty
  }

  /**
   * Method to create a number of animals for each species equal to the one in the Map.
   *
   * @return the created animals.
   */
  private def generateInitialAnimals(): Seq[Animal] = {
    //TODO fare in modo più funzionale (for yield ad esempio)
    // lasciare il metodo qui nel gameloop?
    var animals = Seq.empty[Animal]
    species foreach (s => {
      for (_ <- 1 to s._2) {
        val (topLeft, bottomRight) = placeAnimal(s._1)
        val animal = Animal(s._1, topLeft)
        animals = animals :+ animal
        animalsAndRectangles += (animal -> new Rectangle(topLeft, bottomRight, animal.color))
      }
    })
    animals
  }

  /**
   * Method used to obtain a random permissible point to create an animal of a certain species.
   *
   * @param species the Species of the animal.
   * @return the Point (top left) to create the animal and the Point (bottom right) used to draw the rectangle.
   */
  private def placeAnimal(species: Species): (Point, Point) = {
    val (width, height) = habitat.dimensions
    val size = species.size match {
      case Big => Constants.PixelForBig
      case Medium => Constants.PixelForMedium
      case Small => Constants.PixelForSmall
    }
    var x = Random.nextInt(width - size)
    var y = Random.nextInt(height - size)
    while (AnimalUtils.areNotPlaceable(habitat.areas, Seq(Point(x, y), Point(x + size, y + size), Point(x + size, y), Point(x, y + size)))) {
      x = Random.nextInt(width - size)
      y = Random.nextInt(height - size)
    }
    (Point(x, y), Point(x + size, y + size))
  }
}


