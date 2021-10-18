package model

import utility.Constants.{DefaultGridSize, DefaultStartingX, DefaultStartingY}
import utility.{Constants, OfArea, Point, RectangleArea, Serializer}

import scala.util.Random

/**
 * The possible types of habitat.
 */
sealed trait HabitatType
case object EmptyHabitatType extends HabitatType
case object SimpleHabitatType extends HabitatType
case object RandomHabitatType extends HabitatType
case object GridHabitatType extends HabitatType

/**
 * Trait that rappresent an habitat
 * TODO completare
 */
trait Habitat {
  val unexpectedEvents: Probability
  val dimensions: (Int, Int)
  val areas: Seq[Area]

  //todo scaladoc
  def checkForOverlappingAreas(areas: Seq[Area]): Boolean = areas match {
    case h :: t =>
      for (a <- areas.filterNot(elem => elem == h)) {
        if (h.area.overlap(a.area)) return false
      }
      checkForOverlappingAreas(t)
    case _ => true
  }

  //todo scaladoc
  def checkDimensionsOfAreas(areas: Seq[Area]): Boolean = areas match {
    case h :: t =>

      if (h.area.topLeft.x < 0 || h.area.topLeft.y < 0) {
        println("Invalid area point, below 0")
        return false
      }
      if (h.area.bottomRight.x > this.dimensions._1 || h.area.bottomRight.y > this.dimensions._2) {
        println(s"A with coordinates (${h.area.topLeft}, ${h.area.bottomRight} is exceeding limits of ${this.dimensions._1}, ${this.dimensions._2}")
        return false
      }
      checkDimensionsOfAreas(t)
    case _ => true
  }

  require(checkForOverlappingAreas(areas), "overlapping areas")
  require(checkDimensionsOfAreas(areas), "areas don't fit in current habitat")
}

//todo scaladoc
object Habitat {

  def apply(unexpectedEvents: Probability = Probability(0),
            dimensions: (Int, Int) = Constants.MainMapDimension,
            areas: Seq[Area] = Serializer(OfArea).deserializeManyFromFile(Constants.MainMap)(classOf[Area]),
           ): Habitat = SimpleHabitat(unexpectedEvents, dimensions, areas)

  def apply(habitatType: HabitatType,
            unexpectedEvents: Probability,
            dimensions: (Int, Int),
            areas: Seq[Area],
           ): Habitat = habitatType match {
    case EmptyHabitatType => SimpleHabitat(unexpectedEvents, dimensions, Seq.empty)
    case SimpleHabitatType => SimpleHabitat(unexpectedEvents, dimensions, areas)
    case RandomHabitatType => SimpleHabitat(unexpectedEvents, dimensions, createRandomAreas(dimensions))
    case GridHabitatType => SimpleHabitat(unexpectedEvents, dimensions, createGridArea(dimensions, DefaultGridSize))
    case _ => throw new IllegalArgumentException("Habitat type error on method apply")
  }
  
  /**
   * Method used to obtain a random [[AreaType]]
   *
   * @return a random [[AreaType]]
   */
  private def randomAreaType: AreaType = {
    val choices: Seq[AreaType] = Seq(Fertile, Water, Rock, Volcano) //todo se si aggiunge un nuovo tipo bisogna modificare manualmente questa seq; alternative? ( ad es. un metodo getAllTypeOfHabitat? )
    Random.shuffle(choices).head
  }

  // TODO: Refactor createRandomAreas 
  /**
   * This method create an habitat, of given dimension, with diven areas, arranged in a randow way
   *
   * @param dimension dimension of the habitat
   * @return
   */
  private def createRandomAreas(dimension: (Int, Int)): Seq[Area] = {
    createRandomAreas(dimension, 4)
  }

  /**
   * This method create an habitat, of given dimension, with a specific number of areas
   *
   * @param dimension     Dimension of habitat
   * @param numberOfAreas Number of areas in the habitat
   * @return
   */
  private def createRandomAreas(dimension: (Int, Int), numberOfAreas: Int): Seq[Area] = {
    var grid = List[Area]()
    require(dimension._1 * dimension._2 > numberOfAreas * 10)
    val r = RectangleArea(Point(0, 0), Point(1, 1))
    r.getIn4Quadrant(dimension).foreach(rectangle => grid = grid.::(Area(randomAreaType, rectangle, Probability(Random.between(1, 100)))))
    grid
  }

  /**
   * This method create an habitat, of given dimension, with diven areas, arranged in a grid
   *
   * @param dimension     dimension of the habitat
   * @param numberOfAreas num of areas in the grid
   * @return
   */
  private def createGridArea(dimension: (Int, Int), numberOfAreas: Int): Seq[Area] = {
    var grid = List[Area]()
    require(dimension._1 * dimension._2 > numberOfAreas * 10)

    val maxWidth = dimension._1 / (numberOfAreas / 2)
    val maxHeight = dimension._2 / (numberOfAreas / 2)

    for (i <- DefaultStartingX until dimension._1 by (dimension._1 / Math.round(Math.sqrt(numberOfAreas).toFloat))) {
      for (j <- DefaultStartingY until dimension._2 by (dimension._2 / Math.round(Math.sqrt(numberOfAreas).toFloat))) {
        val startingPoint = Point(i, j)
        val areaWidth = Random.between(maxWidth / 2, maxWidth)
        val areaHeight = Random.between(maxHeight / 2, maxHeight)
        val rectangle = RectangleArea(startingPoint, Point(startingPoint.x + areaWidth, startingPoint.y + areaHeight))
        val newArea: Area = Area(randomAreaType, rectangle, Probability(Random.between(1, 100)))
        grid = grid.::(newArea)
      }
    }
    grid
  }

  /**
   * A simple implementation of an habitat
   * @param unexpectedEvents a probability of which some unexpected event could occur
   * @param dimensions the dimensions of the area
   * @param areas the areas inside the Habitat
   */
  private case class SimpleHabitat(override val unexpectedEvents: Probability,
                                   override val dimensions: (Int, Int),
                                   override val areas: Seq[Area],
                                  ) extends Habitat
  
}