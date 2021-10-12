package controller

import model._
import utility.Point

import java.util.concurrent.{ExecutorService, Executors}
import scala.util.Random

case class GameLoop(speciesInMap : Map[Species, Int], habitat: Habitat) extends Runnable {

  val pool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())
  val foodInMap: Seq[FoodInstance] = generateFood()
  val battleManager: BattleManager = BattleManager(getAnimalsInMap)
  val shiftManager: ShiftManager = ShiftManager(habitat, Map.empty )

  //TODO creare mappa animale -> Rettangolo che lo rappresenta
  //TODO pausa come fermare il gioco senza sprecare cpu?

  override def run(): Unit = {
    init()
    try {
      pool.execute(GameLoopHandler(getAnimalsInMap, battleManager, shiftManager))
    } finally {
      pool.shutdown()
    }
  }

  def init(): Unit = {
    generateFood()
  }

  def getAnimalsInMap: Seq[Animal] = {
    var animals = Seq.empty[Animal]
    speciesInMap foreach (s => {
      for (_ <- 1 to s._2) {
        animals = animals :+ Animal(s = s._1, placeAnimal(s._1))
      }
    })
    animals
  }

  /**
   * Given an habitat, place a species in a free space
   * @param species of the animals
   * @return
   */
  def placeAnimal(species: Species): Point = {
    val size = species.size match {
      case Size.Big => 12
      case Size.Medium => 9
      case Size.Small => 6
    }
    val (width, height) = habitat.dimensions
    val areas = habitat.areas
    var x = Random.nextInt(width - size)
    var y = Random.nextInt(height - size)
    while (isNotPlaceable(Point(x,y),areas) || isNotPlaceable(Point(x+size,y+size),areas)
      || isNotPlaceable(Point(x+size,y),areas) || isNotPlaceable(Point(x,y+size),areas)) {
      x = Random.nextInt(width - size)
      y = Random.nextInt(height - size)
    }
    Point(x,y)
  }

  def isNotPlaceable(p: Point, areas: Seq[Area]): Boolean = {
    areas.find(a => a.area.contains(p)).getOrElse(return false).areaType != Fertile
  }

  def generateFood(): Seq[FoodInstance] = Seq.empty

}


