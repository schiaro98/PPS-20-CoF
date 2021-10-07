package controller

import model.{Animal, Fertile, Habitat}
import utility.{Constants, Point}

import scala.annotation.tailrec
import scala.util.Random

sealed trait ShiftManager {
  val habitat: Habitat
  var animalsDestinations: Map[Animal, Seq[Point]]
  // TODO: find a way to require animals outside NonWalkableAreas

  def walk(): Unit

  def animals: collection.Set[Animal] = animalsDestinations.keySet
  //va verso qualcosa che gli e' stato ritornato dal prolog, sia acqua o cibo, se non ha bisogno di nulla non ha una destinazione
}


object ShiftManager {
  def apply(habitat: Habitat, animalsDestinations: Map[Animal, Seq[Point]]): ShiftManager =
    new ShiftManagerImpl(habitat: Habitat, animalsDestinations)

  private class ShiftManagerImpl(override val habitat: Habitat, override var animalsDestinations: Map[Animal, Seq[Point]]) extends ShiftManager {

    val randShift: Int => Int = (x: Int) => Random.nextInt(x)
    // TODO: try to do it with for yield

    //if I can't go to the first point create another, go to that point and when arrived go to the first one
    override def walk(): Unit = {

      val map = scala.collection.mutable.Map.empty[Animal, Seq[Point]]
      val travelDistance: (Int, Int) = (randShift(Constants.MaxShift), randShift(Constants.MaxShift))
      for (a <- animals) {
        //animal has a destination
        if (animalsDestinations(a).nonEmpty) {
          //          println("animal has a destination")
          val dest = animalsDestinations(a).head
          //animal can arrive to destination in this iteration
          if (canTravel(a.position, dest, travelDistance)) {
            println(a.name, " is arriving")
            map += (a.shift(dest) -> Seq.empty)
            //animal has to cal a new point closer to destination
          } else {
            val closerPoint = calcNewPoint(a.position, dest, travelDistance)
            //the point I found is inside an area in which i can't go
            if (habitat.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(closerPoint)) > 0) {
              println("point in a non walkable area")
            }
            map += a.shift(closerPoint) -> Seq(dest)
          }
          //animal doesn't have a destination
        } else map += (a -> Seq.empty)
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
    private def canTravel(from: Point, to: Point, travelDistance: (Int, Int)): Boolean = {
      val distance = to - from
      travelDistance._1 - distance.x.abs > 0 && travelDistance._2 - distance.y.abs > 0
    }

    // TODO: now simplifing saing that there are only walkableAreas

    // TODO: refactor calcNewPoint

    //cambiare nome a questi 2?
    private def calcNewPoint(from: Point, to: Point, travelDistance: (Int, Int)): Point = {
      //calculating potential points
      val x = if (to.isRight(from)) from.x + travelDistance._1 else from.x - travelDistance._1
      val y = if (to.isUnder(from)) from.y + travelDistance._2 else from.y - travelDistance._2
      makeInBounds(x, y)
    }

    /**
     * @param from point inside an area in which the animal can't walk
     * @param optP a possible point that could be out of the area of interest
     * @return a Point outside a NonWalkableArea
     */
    @tailrec
    private def circumnavigate(from: Point)(optP: Option[Point] = None): Point = optP match {
      case Some(value) => if (habitat.areas
        .filterNot(a => a.areaType == Fertile)
        .count(a => a.contains(value)) > 0) circumnavigate(value)(None) else value
      case None => circumnavigate(from)(Some(calcPointOutOfNonWalkableArea(from)))
    }

    private def calcPointOutOfNonWalkableArea(p: Point): Point = {
      val DefDist = 10
      val a = habitat.areas.filter(a => a.contains(p)).head.area
      val dl = p.fromX(a.topLeft)
      val dr = p.fromX(a.bottomRight)
      val du = p.fromY(a.topLeft)
      val db = p.fromY(a.bottomRight)
      //if left distance >right distance create point to the right, left otherwise
      // TODO: Can have problems with bounds?
      val x = if (dl > dr) p.x + dr + DefDist else p.x - dl - DefDist
      val y = if (du > db) p.y + db + DefDist else p.y - du - DefDist
      makeInBounds(x,y)
    }

    /**
     * Make coordinates be inside of the habitat
     * @param px the possible x
     * @param py the possible y
     * @return a Point inside the habitat
     */
    private def makeInBounds(px: Int, py: Int):Point = {
      val x = if (px < 0) 0 else if (px > habitat.dimensions._1) habitat.dimensions._1 else px
      val y = if (py < 0) 0 else if (py > habitat.dimensions._2) habitat.dimensions._2 else py
      Point(x,y)
    }
  }

}