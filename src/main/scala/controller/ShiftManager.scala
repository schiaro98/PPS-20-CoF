package controller

import model.Animal
import utility.{Constants, Point}

import scala.util.Random

sealed trait ShiftManager {
  val animalsDestinations: Map[Animal, Option[Point]]


  def walk(): Seq[Animal]

  def animals: Set[Animal] = animalsDestinations.keySet
  //va verso qualcosa che gli e' stato ritornato dal prolog, sia acqua o cibo, se non ha bisogno di nulla non ha una destinazione
}


object ShiftManager {
  def apply(animalsDestinations: Map[Animal, Option[Point]]): ShiftManager = new ShiftManagerImpl(animalsDestinations)

  private class ShiftManagerImpl(override val animalsDestinations: Map[Animal, Option[Point]]) extends ShiftManager {

    override def walk(): Seq[Animal] = {
      var seq = scala.collection.mutable.Seq.empty[Animal]
      val travelDistance: (Int, Int) = (Random.nextInt(Constants.MaxShift), Random.nextInt(Constants.MaxShift))
      for (a <- animals) {
        println(a.name, animalsDestinations.getOrElse(a, None) )
        //animal has a destination
        if (animalsDestinations(a).isDefined) {
          val dest = animalsDestinations(a).get
          if (canTravel(a.position, dest, travelDistance)) {
            seq :+= a.shift(dest)
          } else seq :+= a.shift(calcNewPoint(a.position, dest, travelDistance))
          //animal doesn't have a destination
        } else seq :+= a.shift(calcRandomPoint(a.position, travelDistance))
      }
      seq.toSeq
    }

    /**
     *
     * @param from           Point where the animal is
     * @param to             Point in which the animal wants to go
     * @param travelDistance distance that the animal can travel in this iteration
     * @return true if the animal can make it to the destination
     */
    def canTravel(from: Point, to: Point, travelDistance: (Int, Int)): Boolean = {
      val distance = to - from
      travelDistance._1 - distance.x.abs > 0 && travelDistance._2 - distance.y.abs > 0
    }

    //cambiare nome a questi 2?
    def calcNewPoint(from: Point, to: Point, travelDistance: (Int, Int)): Point = ???

    def calcRandomPoint(from: Point, travelDistance: (Int, Int)): Point = ???

  }
}

