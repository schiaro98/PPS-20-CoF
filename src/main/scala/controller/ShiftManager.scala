package controller

import model.{Animal, Area, Fertile, Habitat}
import utility.{Constants, Point}

import scala.annotation.tailrec
import scala.util.Random

sealed trait ShiftManager {
  val habitat: Habitat
  var animalsDestinations: Map[Animal, Seq[Point]]

  def walk(): Unit

  def animals: collection.Set[Animal] = animalsDestinations.keySet
  //va verso qualcosa che gli e' stato ritornato dal prolog, sia acqua o cibo, se non ha bisogno di nulla non ha una destinazione
}


object ShiftManager {
  def apply(habitat: Habitat, animalDestinations: (Animal, Point)*):ShiftManager =
    new ShiftManagerImpl(habitat, animalDestinations.map(c => (c._1, Seq(c._2))).toMap)

  def apply(habitat: Habitat, animalsDestinations: Map[Animal, Seq[Point]]): ShiftManager =
    new ShiftManagerImpl(habitat: Habitat, animalsDestinations)


  private class ShiftManagerImpl(override val habitat: Habitat, override var animalsDestinations: Map[Animal, Seq[Point]]) extends ShiftManager {

    val nonWalkableAreas: Seq[Area] = habitat.areas.filterNot(a => a.areaType == Fertile)
    animals.foreach(animal => require(nonWalkableAreas.count(a => a.contains(animal.position)) == 0))

    val randShift: Unit => Int = _ => Random.between(Constants.MinShift, Constants.MaxShift)
    // TODO: try to do it with for yield
    //if I can't go to the first point create another, go to that point and when arrived go to the first one
    override def walk(): Unit = {
      val map = scala.collection.mutable.Map.empty[Animal, Seq[Point]]
      for (a <- animals) {
        val travelDistance: (Int, Int) = (randShift(), randShift())
        //animal has a destination
        if (animalsDestinations(a).nonEmpty) {
          val dest = animalsDestinations(a)
          //animal can arrive to destination in this iteration
          if (canTravel(a.position, dest.head, travelDistance)) {
            println(a.name, " is arriving")
            map += (a.shift(dest.head) -> dest.tail)
            //animal has to calc a new point closer to destination
          } else {
            val destinations = tryWalk(a.position, dest, travelDistance)
            if (!canTravel(a.position, destinations.head, travelDistance)) {
              println("fok")
            }
            map += a.shift(destinations.head) -> destinations.tail
          }
          //animal doesn't have a destination
          // TODO: implement this
        } else map += (a -> Seq.empty)
      }
      animalsDestinations = map.toMap
    }

    @tailrec
    private def tryWalk(position: Point, dest: Seq[Point], travelDistance: (Int, Int)): Seq[Point] = {
      val nextP = calcNextDest(position, dest.head, travelDistance)
      println(isLegal(nextP))
      if (isLegal(nextP)) nextP +: dest else tryWalk(position, calcLegalRandomPointInRectangle(dest.head, position) +: dest, travelDistance)
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
      travelDistance._1 - distance.x.abs >= 0 && travelDistance._2 - distance.y.abs >= 0
    }

    private def calcNextDest(from: Point, to: Point, travelDistance: (Int, Int)): Point = {
      //calculating potential points
      val x = if (to.isRight(from)) from.x + travelDistance._1 else from.x - travelDistance._1
      val y = if (to.isUnder(from)) from.y + travelDistance._2 else from.y - travelDistance._2
      makeInBounds(x, y)
    }

    /**
     * Make coordinates be inside of the habitat
     *
     * @param px the possible x
     * @param py the possible y
     * @return a Point inside the habitat
     */
    private def makeInBounds(px: Int, py: Int): Point = {
      val x = if (px < 0) 0 else if (px > habitat.dimensions._1) habitat.dimensions._1 else px
      val y = if (py < 0) 0 else if (py > habitat.dimensions._2) habitat.dimensions._2 else py
      Point(x, y)
    }

    @tailrec
    private def calcLegalRandomPointInRectangle(p1: Point, p2: Point, tries: Int = 0): Point = {
      val x = if (p1.x != p2.x) Random.between(Ordering.Int.min(p1.x, p2.x), Ordering.Int.max(p1.x, p2.x)) else p1.x
      val y = if (p1.y != p2.y) Random.between(Ordering.Int.min(p1.y, p2.y), Ordering.Int.max(p1.y, p2.y)) else p2.y
      val p = Point(x, y)
      if (isLegal(p)) p else calcLegalRandomPointInRectangle(p1, p2)
    }

    private def isLegal(p: Point): Boolean = !nonWalkableAreas.exists(a => a.contains(p))
  }

}