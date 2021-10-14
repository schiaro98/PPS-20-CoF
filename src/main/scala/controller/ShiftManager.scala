package controller

import model.{Animal, Area, Carnivore, Fertile, Habitat}
import utility.{Constants, Point}

import scala.annotation.tailrec
import scala.util.Random
import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.{ParMap, ParSet}

sealed trait ShiftManager {
  val habitat: Habitat
  var animalsDestinations: Map[Animal, Point]

  /**
   * Make all the animals walk
   */
  def walk(): Unit

  /**
   *
   * @return the animals in the shiftManager
   */
  def animals: ParSet[Animal]
}


object ShiftManager {
  def apply(habitat: Habitat, animalDestinations: (Animal, Point)*): ShiftManager =
    new ShiftManagerImpl(habitat, animalDestinations.toMap)

  def apply(habitat: Habitat, animalsDestinations: Map[Animal, Point]): ShiftManager =
    new ShiftManagerImpl(habitat: Habitat, animalsDestinations)


  private class ShiftManagerImpl(override val habitat: Habitat, override var animalsDestinations: Map[Animal, Point]) extends ShiftManager {

    val nonWalkableAreas: Seq[Area] = habitat.areas.filterNot(a => a.areaType == Fertile)
    val randShift = (x:Int) => Random.between(Constants.MinShift, x)

    /**
     *
     * @param p the Point to analyze
     * @return true if the animal can be in the point p
     */
    private def isLegal(p: Point): Boolean = !nonWalkableAreas.exists(a => a.contains(p))

    animalsDestinations.keySet.foreach(animal => require(nonWalkableAreas.count(a => a.contains(animal.position)) == 0))
    var mySupportAnimalsDestinations: ParMap[Animal, Seq[Point]] = initWalks(animalsDestinations.keySet.toSeq)

    override def animals: ParSet[Animal] = mySupportAnimalsDestinations.keySet

    override def walk(): Unit = mySupportAnimalsDestinations =
      mySupportAnimalsDestinations.filter(t => t._2.nonEmpty).map(t => t._1.shift(t._2.head) -> t._2.tail)

    /**
     *
     * @param animals the animals for which we want to find the destination
     * @return a parallelizable Map from animals to the Sequence of points that they have to do to arrive to dest
     *         (in this implementation they may not be able to arrive to dest)
     */
    private def initWalks(animals: Seq[Animal]): ParMap[Animal, Seq[Point]] = {
      @tailrec
      def _initWalks(animals: Seq[Animal], map: Map[Animal, Seq[Point]] = Map.empty): ParMap[Animal, Seq[Point]] = animals match {
        case h +: t => _initWalks(t, Map(h -> createPath(h, animalsDestinations(h))))
        case _ => map.par
      }

      _initWalks(animals)
    }

    /**
     *
     * @param animal the animal from that wants to go to dest
     * @param dest the destination point
     * @return the Sequence of Point that the animal will do
     */
    private def createPath(animal: Animal, dest: Point): Seq[Point] = {
      @tailrec
      def _createPath(from: Point,
                      dest: Point,
                      path: Seq[Point] = Seq.empty,
                      isCarnivore: Boolean = false,
                     ): Seq[Point] = path match {
        case _ if from == dest => path.reverse
        case _ =>
          val travelDistance =
            if (isCarnivore)
            (randShift(Constants.MaxShift + Constants.IncCarnivoreVelocity),randShift(Constants.MaxShift + Constants.IncCarnivoreVelocity))
            else (randShift(Constants.MaxShift), randShift(Constants.MaxShift))
          val topLeft = makeInBounds(from.x - travelDistance._1, from.y - travelDistance._2)
          val bottomRight = makeInBounds(from.x + travelDistance._1, from.y + travelDistance._2)
          val legalPoints = for (x <- topLeft.x to bottomRight.x;
                                 y <- topLeft.y to bottomRight.y if isLegal(Point(x, y)))
          yield Point(x, y)
          val closerPoint = findCloserPoint(legalPoints, dest)
          if (path.nonEmpty && path.contains(closerPoint)) path.reverse
          else _createPath(closerPoint, dest, closerPoint +: path)
      }

      _createPath(animal.position, dest, isCarnivore = animal.alimentationType == Carnivore)
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
    private def findCloserPoint(points: Seq[Point], dest: Point): Point = {
      @tailrec
      def _findCloserPoint(points: Seq[Point], dest: Point, closestP: Option[Point] = None, minDist: Double = Double.MaxValue): Point = points match {
        case h +: t =>
          val d = h.distance(dest)
          if (d < minDist) _findCloserPoint(t, dest, Some(h), d) else _findCloserPoint(t, dest, closestP, minDist)
        case _ => closestP.getOrElse(throw new RuntimeException("LIKE IN LIFE, THERE ISN'T A POINT"))
      }

      _findCloserPoint(points, dest)
    }
  }
}