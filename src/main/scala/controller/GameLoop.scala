package controller

import model.Area.FertileAreaGrowFood
import model._
import utility.{Constants, Point}
import view.{Rectangle, ShapePanel, SimulationGui}

import java.awt.Color
import scala.util.Random

/**
 * Runnable used to start the simulation, it contains the game loop which must be executed in a new thread
 * @param species the Species with the number of animals to create at the beginning of the simulation
 * @param habitat the Habitat where the simulation takes place
 */
case class GameLoop(species : Map[Species, Int], habitat: Habitat) extends Runnable {

  var animalsAndRectangles: Map[Animal, Rectangle] = Map.empty[Animal, Rectangle] //TODO lasciamo var?
  val animalsInMap: Seq[Animal] = generateInitialAnimals()
  val foodInMap: Seq[FoodInstance] = generateInitialFood()
  val battleManager: BattleManager = BattleManager(animalsInMap)
  val shiftManager: ShiftManager = ShiftManager(habitat, Map.empty[Animal, Seq[Point]])

  override def run(): Unit = {
    val shapePanel = new ShapePanel(habitat.dimensions._1, habitat.dimensions._2)
    val simulationGui = new SimulationGui(habitat, shapePanel) {top.visible = true }
    simulationGui.updatePanel(animalsAndRectangles)

    var previous: Long = System.currentTimeMillis()
    while(animalsInMap.lengthIs > 1) { //TODO pausa come fermare il gioco senza sprecare cpu?
      val current: Long = System.currentTimeMillis()
      val elapsed: Double = (current - previous) / Constants.MillisToSec

      //Prendo dallo shiftmanager il primo movimento per ogni animale
      //TODO shift manager dovrebbe gestire l'eat se l'animale finisce vicino al cibo e acqua??
      //Non credo sia proprio lo shift a dover vedere sta cosa ma vabbe
      battleManager.calculateBattles()
      //Calcolo eventi inaspettati


      //---- solo per vedere che la gui cambia
      animalsAndRectangles = Map.empty
      generateInitialAnimals()
      //---- solo per vedere che la gui cambia
      simulationGui.updatePanel(animalsAndRectangles)
      waitForNextFrame(current)
      previous = current
    }

    //TODO mostra la gui con il riassunto
  }

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

  def generateInitialAnimals(): Seq[Animal] = {
    //TODO fare in modo più funzionale (for yield ad esempio)
    var animals = Seq.empty[Animal]
    species foreach (s => {
      val color = new Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()) //TODO cambia colore hardcoded
      for (_ <- 1 to s._2) {
        val (x,y) = placeAnimal(s._1)
        val animal = Animal(s = s._1, x)
        animals = animals :+ animal
        animalsAndRectangles += (animal -> new Rectangle(x,y,color))
      }
    })
    animals
  }

  /**
   * Method used to obtain a random permissible point to create an animal of a certain species
   * @param species the Species of the animal
   * @return the Point (top left) to create the animal and the Point (bottom right) used to draw the rectangle
   */
  def placeAnimal(species: Species): (Point,Point) = {
    val (width, height) = habitat.dimensions
    val areas = habitat.areas
    val size = species.size match {
      case Size.Big => Constants.PixelForBig
      case Size.Medium => Constants.PixelForMedium
      case Size.Small => Constants.PixelForSmall
    }

    var x = Random.nextInt(width - size)
    var y = Random.nextInt(height - size)
    while (isNotPlaceable(Point(x,y),areas) || isNotPlaceable(Point(x+size,y+size),areas)
      || isNotPlaceable(Point(x+size,y),areas) || isNotPlaceable(Point(x,y+size),areas)) {
      x = Random.nextInt(width - size)
      y = Random.nextInt(height - size)
    }
    (Point(x,y), Point(x+size,y+size))
  }

  def isNotPlaceable(p: Point, areas: Seq[Area]): Boolean = {
    Constants.NonWalkableArea.contains(areas.find(a => a.area.contains(p)).getOrElse(return false).areaType)
  }
}


