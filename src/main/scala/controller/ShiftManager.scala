package controller

import model._
import utility.{AnimalUtils, Constants}

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.util.Random

sealed trait ShiftManager {
  /**
   * Make all the animals walk
   */
  def walk(): Unit

  /**
   *
   * @return the animals in the shiftManager
   */
  def animals: Set[Animal]
}


object ShiftManager {
  /**
   * The apply for [[ShiftManager]]
   *
   * @param habitat             the [[Habitat]] in which shifts occur
   * @param animalsDestinations a vararg of  [[(Animal, Point)]]
   * @return an Implementation of [[ShiftManager]]
   */
  def apply(habitat: Habitat, animalsDestinations: (Animal, Point)*): ShiftManager =
    new ShiftManagerImpl(habitat, animalsDestinations.toMap)

  /**
   * The apply for [[ShiftManager]]
   *
   * @param habitat             the [[Habitat]] in which shifts occur
   * @param animalsDestinations a [[Map]] with [[Animal]] as key and the [[Point]] as destination
   * @return an Implementation of [[ShiftManager]]
   */
  def apply(habitat: Habitat, animalsDestinations: Map[Animal, Point]): ShiftManager =
    new ShiftManagerImpl(habitat: Habitat, animalsDestinations)


  private class ShiftManagerImpl(val habitat: Habitat, val animalsDestinations: Map[Animal, Point]) extends ShiftManager {

    private val nonWalkableAreas: Seq[Area] = habitat.areas.filterNot(_.areaType == Fertile)
    //lambda returning an Int for potential shifts
    private val randShift = (x: Int) => (Random.between(Constants.MinShift, x),Random.between(Constants.MinShift, x))

    /**
     *
     * @param p the Point to analyze
     * @return true if the animal can be in the point p
     */
    private def isLegal(p: Point): Boolean = !nonWalkableAreas.exists(_.contains(p))

    //require that on creation no animal is inside a nonWalkableArea
    animalsDestinations.keySet.foreach(animal => require(nonWalkableAreas.count(_.contains(animal.position)) == 0))
    private var mySupportAnimalsDestinations: Map[Animal, Point] = animalsDestinations

    override def animals: Set[Animal] = mySupportAnimalsDestinations.keySet.to(Set)

    override def walk(): Unit =
      mySupportAnimalsDestinations = mySupportAnimalsDestinations.par.map(t => (t._1.shift(createPath(t._1, t._2)),t._2)).to(Map)


    private def createPath(a: Animal, d: Point): Point = {
      val f = a.position
      val td =
        if (a.species.alimentationType == Carnivore)
          randShift(Constants.MaxShiftForCarnivore)
        else
          randShift(Constants.MaxShiftForHerbivore)
      val topLeft = makeInBounds(f.x - td._1, f.y - td._2)
      val bottomRight = makeInBounds(f.x + td._1, f.y + td._2)

      val legalPoints = for (x <- topLeft.x to bottomRight.x;
                             y <- topLeft.y to bottomRight.y
                             //all corners of the animal must be legal
                             if AnimalUtils.getCornersOfSpeciesInPoint(a.species, Point(x, y)).count(!isLegal(_)) == 0)
      yield Point(x, y)
      findClosestPoint(legalPoints, d).getOrElse(a.position)
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

    /**
     *
     * @param points the Sequence of point to analyze
     * @param dest   the destination
     * @return the closest Point to dest
     */
    private def findClosestPoint(points: Seq[Point], dest: Point): Option[Point] = {
      @tailrec
      def _findClosestPoint(points: Seq[Point], dest: Point, closestP: Option[Point] = None, minDist: Double = Double.MaxValue): Option[Point] = points match {
        case h +: t =>
          val d = h.distance(dest)
          if (d < minDist) _findClosestPoint(t, dest, Some(h), d) else _findClosestPoint(t, dest, closestP, minDist)
        case _ => closestP
      }

      _findClosestPoint(points, dest)
    }
  }
}