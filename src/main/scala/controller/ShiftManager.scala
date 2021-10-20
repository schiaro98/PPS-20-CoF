package controller

import model._
import utility.{AnimalUtils, Constants}

import scala.annotation.tailrec
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
    private val randShift = (x: Int) => Random.between(Constants.MinShift, x)

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
      mySupportAnimalsDestinations = initWalks(mySupportAnimalsDestinations.toList)


    private def initWalks(animalsToDestinations: List[(Animal, Point)]): Map[Animal, Point] = {
      @tailrec
      def _initWalks(animalsToDestinations: List[(Animal, Point)], map: Map[Animal, Point] = Map.empty): Map[Animal, Point] =
        animalsToDestinations match {
          case (k, v) :: t => _initWalks(t, map ++ Map(k.shift(createPath(k, v)) -> v))
          case _ => map
        }

      _initWalks(animalsToDestinations)
    }


    private def createPath(animal: Animal, dest: Point): Point = {
      def _createPath(from: Point,
                      dest: Point,
                      species: Species,
                      isCarnivore: Boolean = false,
                     ): Point = {
        val travelDistance =
          if (isCarnivore)
            (randShift(Constants.MaxShift + Constants.IncCarnivoreVelocity), randShift(Constants.MaxShift + Constants.IncCarnivoreVelocity))
          else (randShift(Constants.MaxShift), randShift(Constants.MaxShift))
        val topLeft = makeInBounds(from.x - travelDistance._1, from.y - travelDistance._2)
        val bottomRight = makeInBounds(from.x + travelDistance._1, from.y + travelDistance._2)
        val legalPoints = for (x <- topLeft.x to bottomRight.x;
                               y <- topLeft.y to bottomRight.y
                               if areAllLegal(AnimalUtils.getCornersOfSpeciesInPoint(species, Point(x, y))))
        yield Point(x, y)
        findClosestPoint(legalPoints, dest)
      }

      _createPath(animal.position, dest, animal.species, isCarnivore = animal.alimentationType == Carnivore)
    }

    /**
     *
     * @param points the [[Seq]] of [[Point]] we want to test
     * @return true if all points are legal
     */
    @tailrec
    private def areAllLegal(points: Seq[Point]): Boolean = points match {
      case h :: t => if (isLegal(h)) areAllLegal(t) else false
      case _ => true
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
    private def findClosestPoint(points: Seq[Point], dest: Point): Point = {
      @tailrec
      def _findClosestPoint(points: Seq[Point], dest: Point, closestP: Option[Point] = None, minDist: Double = Double.MaxValue): Point = points match {
        case h +: t =>
          val d = h.distance(dest)
          if (d < minDist) _findClosestPoint(t, dest, Some(h), d) else _findClosestPoint(t, dest, closestP, minDist)
        case _ => closestP.getOrElse(throw new RuntimeException("No Point found, but there should be at least one"))
      }

      _findClosestPoint(points, dest)
    }
  }
}