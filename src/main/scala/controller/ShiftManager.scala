package controller

import model.{Animal, Area, Fertile, Habitat}
import utility.{Constants, Point}

import scala.annotation.tailrec
import scala.util.Random

sealed trait ShiftManager {
  val habitat: Habitat
  var animalsDestinations: Map[Animal, Point]

  def walk(): Unit

  def animals: collection.Set[Animal]
  //va verso qualcosa che gli e' stato ritornato dal prolog, sia acqua o cibo, se non ha bisogno di nulla non ha una destinazione
}


object ShiftManager {
  def apply(habitat: Habitat, animalDestinations: (Animal, Point)*): ShiftManager =
    new ShiftManagerImpl(habitat, animalDestinations.toMap)

  def apply(habitat: Habitat, animalsDestinations: Map[Animal, Point]): ShiftManager =
    new ShiftManagerImpl(habitat: Habitat, animalsDestinations)


  private class ShiftManagerImpl(override val habitat: Habitat, override var animalsDestinations: Map[Animal, Point]) extends ShiftManager {

    val nonWalkableAreas: Seq[Area] = habitat.areas.filterNot(a => a.areaType == Fertile)
    val randShift: Unit => Int = _ => Random.between(Constants.MinShift, Constants.MaxShift)
    private def isLegal(p: Point): Boolean = !nonWalkableAreas.exists(a => a.contains(p))
    animalsDestinations.keySet.foreach(animal => require(nonWalkableAreas.count(a => a.contains(animal.position)) == 0))
    var mySupportAnimalsDestinations: Map[Animal, Seq[Point]] = initWalks(animalsDestinations.keySet.toSeq)
    override def animals: collection.Set[Animal] = mySupportAnimalsDestinations.keySet


    override def walk(): Unit = {
      @tailrec
      def recWalk(animals: Seq[Animal], updatedAnimalsDestinations: Map[Animal, Seq[Point]] = Map.empty[Animal, Seq[Point]]): Map[Animal, Seq[Point]] = animals match {
        case a +: others =>
          if (mySupportAnimalsDestinations(a).nonEmpty) {
            recWalk(others, updatedAnimalsDestinations + (a.shift(mySupportAnimalsDestinations(a).head) -> mySupportAnimalsDestinations(a).tail))
          } else recWalk(others, updatedAnimalsDestinations)
        case _ => updatedAnimalsDestinations
      }
      mySupportAnimalsDestinations = recWalk(mySupportAnimalsDestinations.keySet.toSeq)
    }

    @tailrec
    private def initWalks(animals: Seq[Animal], map: Map[Animal, Seq[Point]] = Map.empty): Map[Animal, Seq[Point]] = animals match {
      case h +: t => initWalks(t, Map(h -> createPath(h.position, animalsDestinations(h))))
      case _ => map
    }

    @tailrec
    private def createPath(from: Point, dest: Point, path: Seq[Point] = Seq.empty): Seq[Point] = path match {
      case _ if from == dest => path.reverse
      case _ =>
        val travelDistance = (randShift(), randShift())
        //        println( "traveldistance ", travelDistance)
        val topLeft = makeInBounds(from.x - travelDistance._1, from.y - travelDistance._2)
        val bottomRight = makeInBounds(from.x + travelDistance._1, from.y + travelDistance._2)
        val legalPoints = for (x <- topLeft.x to bottomRight.x;
                               y <- topLeft.y to bottomRight.y if isLegal(Point(x, y)))
        yield Point(x, y)
        val closerPoint = findCloserPoint(legalPoints, dest)
        createPath(closerPoint, dest, closerPoint +: path)
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
    private def findCloserPoint(points: Seq[Point], dest: Point, closestP: Option[Point] = None, minDist: Double = Double.MaxValue): Point = points match {
      case h +: t =>
        val d = h.distance(dest)
        if (d < minDist) findCloserPoint(t, dest, Some(h), d) else findCloserPoint(t, dest, closestP, minDist)
      case _ => closestP.getOrElse(throw new RuntimeException("LIKE IN LIFE, THERE ISN'T A POINT"))
    }

  }
}