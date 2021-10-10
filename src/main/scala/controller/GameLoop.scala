package controller

import model.{Animal, Area, Fertile, FoodInstance, Habitat, Size, Species}
import utility.{Constants, Point}

import java.lang.Thread.sleep
import scala.util.Random

class GameLoop(val speciesInMap : Map[Species, Int],  val habitat: Habitat) extends Runnable {

  val animalsInMap: Seq[Animal] = getAnimalsInMap(speciesInMap)
  val condition: () => Boolean = () => animalsInMap.lengthIs > 1


  val foodInMap: Seq[FoodInstance] = generateFood()
  //TODO creare mappa animale -> Rettangolo che lo rappresenta
  //TODO pausa come fermare il gioco senza sprecare cpu?

  override def run(): Unit = {
    init()
    while(condition()) {
      val timeStart = System.currentTimeMillis()
      //Prendo dallo shiftmanager il primo movimento per ogni animale
      //TODO shift manager dovrebbe gestire l'eat se l'animale finisce vicino al cibo e acqua??
      //Battle manager calcola possibili
      //Calcolo eventi inaspettati
      //new ShapePanel
      val timeEnd = System.currentTimeMillis()
      sleep(timeStart + Constants.tickTime - timeEnd)
      //sleep
    }
  }

  def init(): Unit = {
    //prendo la mappa di animali, che è stata inizializzata in simulationGui e chiamo su ShapePanel addAllAnimals.
  }

  def getAnimalsInMap(species: Map[Species, Int]): Seq[Animal] = {
    val animals = Seq.empty
    species.foreach(s => for (_ <- 1 to s._2) animals.+:(Animal(s = s._1, placeAnimal(s._1))))
    animals
  }

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
