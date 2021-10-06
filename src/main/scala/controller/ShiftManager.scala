package controller

import model.{Animal, Habitat}
import utility.{Constants, Point}

import scala.util.Random

sealed trait ShiftManager {
  val habitat: Habitat
  var animalsDestinations: Map[Animal, Option[Point]]
  // TODO: find a way to require animals outside NonWalkableAreas

  def walk(): Unit

  def animals: collection.Set[Animal] = animalsDestinations.keySet
  //va verso qualcosa che gli e' stato ritornato dal prolog, sia acqua o cibo, se non ha bisogno di nulla non ha una destinazione
}


object ShiftManager {
  def apply(habitat: Habitat, animalsDestinations: Map[Animal, Option[Point]]): ShiftManager =
    new ShiftManagerImpl(habitat: Habitat, animalsDestinations)

  private class ShiftManagerImpl(override val habitat: Habitat, override var animalsDestinations: Map[Animal, Option[Point]]) extends ShiftManager {

    // TODO: try to do it with for yield
    override def walk(): Unit = {
      val map = scala.collection.mutable.Map.empty[Animal, Option[Point]]
      val travelDistance: (Int, Int) = (Random.nextInt(Constants.MaxShift), Random.nextInt(Constants.MaxShift))
      for (a <- animals) {
        //animal has a destination
        if (animalsDestinations(a).isDefined) {
//          println("animal has a destination")
          val dest = animalsDestinations(a).get
          if (canTravel(a.position, dest, travelDistance)) {
            println(a.name, " is arriving")
            map += (a.shift(dest) ->None)
          } else map += a.shift(calcNewPoint(a.position, dest, travelDistance))->Some(dest)
          //animal doesn't have a destination
        } else map += (a -> None)
      }
      animalsDestinations = map.toMap
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

    // TODO: now simplifing saing that there are only walkableAreas

    // TODO: refactor calcNewPoint

    //cambiare nome a questi 2?
    def calcNewPoint(from: Point, to: Point, travelDistance: (Int, Int)): Point = {
      //calculating potential points
      val px = if (to.isRight(from)) from.x + travelDistance._1 else from.x - travelDistance._1
      val py = if (to.isUnder(from)) from.y + travelDistance._2 else from.y - travelDistance._2

      val potP = Point(px,py)
      //if points are inside a NonWalkableArea
      if(habitat.areas.count(a => a.contains(potP)) > 0){
        println("you shouldn't be walking here")
//        trovo la distanza minore dalla x per aggirarlo
//        trovo la distanza minore dalla y per aggirarlo
      }

      //if everything is ok check that points are inside the habitat else make them inside
      val x = if(px < 0) 0 else if (px >habitat.dimensions._1) habitat.dimensions._1 else px
      val y = if(py <0) 0 else if (py> habitat.dimensions._2) habitat.dimensions._2 else py
//      println("new point is ", x, " ", y )
      //the real point
      Point(x,y)
    }

    def calcRandomPoint(from: Point, travelDistance: (Int, Int)): Point = ???
  }
}

