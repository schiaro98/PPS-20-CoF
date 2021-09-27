package controller

import model.Animal
import utility.{Constants, Point}

import scala.util.Random

sealed trait ShiftManager {
  val animalsDestinations: Map[Animal, Option[Point]]


  def walk(): Map[Animal, Option[Point]]

  def animals: Set[Animal] = animalsDestinations.keySet
  //va verso qualcosa che gli e' stato ritornato dal prolog, sia acqua o cibo, se non ha bisogno di nulla non ha una destinazione
}


object ShiftManager {
  def apply(animalsDestinations: Map[Animal, Option[Point]]): ShiftManager = new ShiftManagerImpl(animalsDestinations)

  private class ShiftManagerImpl(override val animalsDestinations: Map[Animal, Option[Point]]) extends ShiftManager {

    override def walk(): Map[Animal, Option[Point]] = {
      var map =  scala.collection.mutable.Map.empty[Animal, Option[Point]]
      for (a <- animals) {
        println(a.name)
        val travelDistance: (Int, Int) = (Random.nextInt(Constants.MaxShift),Random.nextInt(Constants.MaxShift))
        if (animalsDestinations(a).isDefined) {
          val dest = animalsDestinations(a).get

          val bool = canTravel(a.position, dest, travelDistance)
          println(bool)
          if (bool){
            map += a -> None
          }
        }
      }
      map.toMap
    }

    /**
     *
     * @param from Point where the animal is
     * @param to Point in which the animal wants to go
     * @param travelDistance distance that the animal can travel in this iteration
     * @return true if the animal can make it to the destination
     */
    def canTravel(from: Point, to : Point, travelDistance: (Int, Int) ): Boolean = {
      val distance = to - from
      travelDistance._1 - distance.x.abs > 0 && travelDistance._2 - distance.y.abs > 0
    }


  }
}

