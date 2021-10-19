package controller

import model._
import utility.{AnimalUtils, Constants}

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.ParMap
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
    private var mySupportAnimalsDestinations: ParMap[Animal, Seq[Point]] = initWalks(animalsDestinations.keySet.toSeq)

    override def animals: Set[Animal] = mySupportAnimalsDestinations.keySet.to(Set)

    override def walk(): Unit =
      mySupportAnimalsDestinations =
        mySupportAnimalsDestinations.filter(_._2.nonEmpty).map(t => t._1.shift(t._2.head) -> t._2.tail) ++
          mySupportAnimalsDestinations.filterNot(_._2.nonEmpty)


    /**
     *
     * @param animals the animals for which we want to find the destination
     * @return a parallelizable Map from animals to the Sequence of points that they have to do to arrive to dest
     *         (in this implementation they may not be able to arrive to dest)
     */
    private def initWalks(animals: Seq[Animal]): ParMap[Animal, Seq[Point]] = {
      @tailrec
      def _initWalks(animals: Seq[Animal], map: Map[Animal, Seq[Point]] = Map.empty): ParMap[Animal, Seq[Point]] = animals match {
        case h +: t => _initWalks(t, map ++ Map(h -> createPath(h, animalsDestinations(h))))
        case _ => map.par
      }

      _initWalks(animals)
    }

    /**
     *
     * @param animal the animal from that wants to go to dest
     * @param dest   the destination point
     * @return the Sequence of Point that the animal will do
     */
    private def createPath(animal: Animal, dest: Point): Seq[Point] = {
      @tailrec
      def _createPath(from: Point,
                      dest: Point,
                      species: Species,
                      path: Seq[Point] = Seq.empty,
                      isCarnivore: Boolean = false,
                     ): Seq[Point] = path match {
        case _ if from == dest => path.reverse
        case _ =>
          val travelDistance =
            if (isCarnivore)
              (randShift(Constants.MaxShift + Constants.IncCarnivoreVelocity), randShift(Constants.MaxShift + Constants.IncCarnivoreVelocity))
            else (randShift(Constants.MaxShift), randShift(Constants.MaxShift))
          val topLeft = makeInBounds(from.x - travelDistance._1, from.y - travelDistance._2)
          val bottomRight = makeInBounds(from.x + travelDistance._1, from.y + travelDistance._2)
          val legalPoints = for (x <- topLeft.x to bottomRight.x;
                                 y <- topLeft.y to bottomRight.y
                                 if areAllLegal( AnimalUtils.getCornersOfSpeciesInPoint(species, Point(x,y))))
          yield Point(x, y)
          val closerPoint = findClosestPoint(legalPoints, dest)
          if (path.nonEmpty && path.contains(closerPoint)) path.reverse
          else _createPath(closerPoint, dest, animal.species, closerPoint +: path)
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
      case h::t => if (isLegal(h)) areAllLegal(t) else false
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
        case _ => closestP.getOrElse(throw new RuntimeException("LIKE IN LIFE, THERE ISN'T A POINT"))
      }

      _findClosestPoint(points, dest)
    }
  }
}