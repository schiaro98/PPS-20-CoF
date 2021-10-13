package controller

import model.Area.FertileAreaGrowFood
import model._
import utility.{Constants, Point}
import view.{Rectangle, ShapePanel, SimulationGui}

import java.awt.Color
import scala.util.Random

/**
 * Runnable used to start the simulation, it contains the game loop which must be executed in a new thread.
 *
 * @param species the Species with the number of animals to create at the beginning of the simulation.
 * @param habitat the Habitat where the simulation takes place.
 */
case class GameLoop(species: Map[Species, Int], habitat: Habitat) extends Runnable {

  var animalsAndRectangles: Map[Animal, Rectangle] = Map.empty[Animal, Rectangle] //TODO lasciamo var?
  val animalsInMap: Seq[Animal] = generateInitialAnimals()
  val foodInMap: Seq[FoodInstance] = generateInitialFood()
  val battleManager: BattleManager = BattleManager(animalsInMap)
  val shiftManager: ShiftManager = ShiftManager(habitat, Map.empty[Animal, Point])

  override def run(): Unit = {
    val shapePanel = new ShapePanel(habitat.dimensions._1, habitat.dimensions._2)
    val simulationGui = new SimulationGui(habitat, shapePanel) {
      top.visible = true
    }
    simulationGui.updatePanel(animalsAndRectangles)

    var previous: Long = System.currentTimeMillis()
    while (animalsInMap.lengthIs > 1) { //TODO pausa come fermare il gioco senza sprecare cpu?
      val current: Long = System.currentTimeMillis()
      val elapsed: Double = (current - previous) / Constants.MillisToSec

      //Prendo dallo shiftmanager il primo movimento per ogni animale
      //TODO shift manager dovrebbe gestire l'eat se l'animale finisce vicino al cibo e acqua??
      //Non credo sia proprio lo shift a dover vedere sta cosa ma vabbe
      battleManager.calculateBattles()
      //Calcolo eventi inaspettati
      //crescita casuale dei vegetali


      //---- solo per vedere che la gui cambia----|
      animalsAndRectangles = Map.empty //         |
      generateInitialAnimals() //                 |
      //---- solo per vedere che la gui cambia----|


      simulationGui.updatePanel(animalsAndRectangles)
      waitForNextFrame(current)
      previous = current
    }

    //TODO mostra la gui con il riassunto
  }

  /**
   * Method that permit to wait the current time to render the following frame.
   *
   * @param current time at the beginning of current frame.
   */
  def waitForNextFrame(current: Long): Unit = {
    val dt = System.currentTimeMillis() - current
    println("Time elapsed for the computation: " + dt)
    if (dt < Constants.Period) {
      Thread.sleep(Constants.Period - dt)
    }
  }

  def generateInitialFood(): Seq[FoodInstance] = {
    var food = Seq.empty[FoodInstance]
    //    habitat.areas.filter(a => a.isInstanceOf[FertileAreaGrowFood])
    food
  }

  /**
   * Method to create a number of animals for each species equal to the one in the Map.
   *
   * @return the created animals.
   */
  def generateInitialAnimals(): Seq[Animal] = {
    //TODO fare in modo più funzionale (for yield ad esempio)
    var animals = Seq.empty[Animal]
    species foreach (s => {
      val color = new Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()) //TODO cambia colore hardcoded
      for (_ <- 1 to s._2) {
        val (x, y) = placeAnimal(s._1)
        val animal = Animal(s = s._1, x)
        animals = animals :+ animal
        animalsAndRectangles += (animal -> new Rectangle(x, y, color))
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
  def placeAnimal(species: Species): (Point, Point) = {
    val (width, height) = habitat.dimensions
    val size = species.size match {
      case Big => Constants.PixelForBig
      case Medium => Constants.PixelForMedium
      case Small => Constants.PixelForSmall
    }
    var x = Random.nextInt(width - size)
    var y = Random.nextInt(height - size)
    while (areNotPlaceable(Seq(Point(x, y), Point(x + size, y + size), Point(x + size, y), Point(x, y + size)))) {
      x = Random.nextInt(width - size)
      y = Random.nextInt(height - size)
    }
    (Point(x, y), Point(x + size, y + size))
  }


  /**
   * Check if a sequence of point is in a non-walkable area.
   *
   * @param points the points whose positions are to be checked.
   * @return true if at least one point is not placeable because was in a non-walkable area, otherwise true.
   */
  def areNotPlaceable(points: Seq[Point]): Boolean = points.exists(p => isNotPlaceable(p))

  /**
   * Check if a point is in a non-walkable area.
   *
   * @param p the point whose position is to be checked.
   * @return true if the point is not placeable because was in a non-walkable area, otherwise true.
   */
  def isNotPlaceable(p: Point): Boolean = {
    Constants.NonWalkableArea.contains(habitat.areas.find(a => a.area.contains(p)).getOrElse(return false).areaType)
  }
}


