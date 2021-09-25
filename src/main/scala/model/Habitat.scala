package model

import model.Habitat.{SimpleHabitat, createGridArea, createRandomAreas}
import utility.Constants.{defaultGridSize, defaultRandomSize, defaultStartingX, defaultStartingY}
import utility.{Point, RectangleArea}

import scala.util.Random

sealed trait HabitatType

case object EmptyHabitatType extends HabitatType

case object SimpleHabitatType extends HabitatType

case object RandomHabitatType extends HabitatType

case object GridHabitatType extends HabitatType

trait Habitat {
  val unexpectedEvents: Probability
  val dimensions: (Int, Int)
  val areas: Seq[Area]

  def checkForOverlappingAreas(areas: Seq[Area]): Boolean = areas match {
    case h :: t =>
      for (a <- areas.filterNot(elem => elem == h)) {
        if(h.area.overlap(a.area)) return false
      }
      checkForOverlappingAreas(t)
    case _ => true
  }

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


object Habitat {

  def apply(unexpectedEvents: Probability,
            dimensions: (Int, Int),
            areas: Seq[Area]): Habitat = SimpleHabitat(unexpectedEvents, dimensions, areas)

  /*
  TODO sarebbe da mettere che un apply senza aree nel campo areas possa creare aree random/grid/ empty ma dia eccezione se si prova a creare
  un habitat simple, mentre togliere la possibilita di creare un habitat non semplice se si specifica areas
  Pensiamoci
   */
  def apply(habitatType: HabitatType,
            unexpectedEvents: Probability,
            dimensions: (Int, Int),
            areas: Seq[Area]): Habitat = habitatType match {
    case EmptyHabitatType => SimpleHabitat(unexpectedEvents, dimensions, Seq.empty)
    case SimpleHabitatType => SimpleHabitat(unexpectedEvents, dimensions, areas)
    case RandomHabitatType => SimpleHabitat(unexpectedEvents, dimensions, createRandomAreas(dimensions, defaultRandomSize))
    case GridHabitatType => SimpleHabitat(unexpectedEvents, dimensions, createGridArea(dimensions, defaultGridSize))
    case _ => throw new IllegalArgumentException("Habitat type error on method apply")
  }

      def apply(habitatType: HabitatType,
                unexpectedEvents: Probability,
                dimensions: (Int, Int),
                size: Int): Habitat = habitatType match {
        case RandomHabitatType => SimpleHabitat(unexpectedEvents, dimensions, createRandomAreas(dimensions, size))
        case GridHabitatType => SimpleHabitat(unexpectedEvents, dimensions, createGridArea(dimensions, size))
        case _ => throw new IllegalArgumentException("Habitat type error on method apply")
  }

  def apply(unexpectedEvent: Probability, dimensions: (Int, Int)): Habitat =
    SimpleHabitat(unexpectedEvent, dimensions, createRandomAreas(dimensions, 20))

  private case class SimpleHabitat(override val unexpectedEvents: Probability,
                                   override val dimensions: (Int, Int),
                                   override val areas: Seq[Area]) extends Habitat


  /**
   * This method create an habitat, of given dimension, with diven areas, arranged in a randow way
   *
   * @param dimension     dimension of the habitat
   * @param numberOfAreas num of areas in the grid
   * @return
   */
  def createRandomAreas(dimension: (Int, Int), numberOfAreas: Int): Seq[Area] = {
    var grid: Seq[Area] = createGridArea(dimension, numberOfAreas * 10)
    while (grid.size > numberOfAreas){
      val areaToRemove = grid(Random.nextInt(grid.size))
      grid = grid.filterNot(area => area == areaToRemove)
    }
    grid.foreach(grid => {
      grid.area.bottomRight + (100,100)
    })
    grid
  }

  /**
   * This method create an habitat, of given dimension, with diven areas, arranged in a grid
   *
   * @param dimension     dimension of the habitat
   * @param numberOfAreas num of areas in the grid
   * @return
   */
  def createGridArea(dimension: (Int, Int), numberOfAreas: Int): Seq[Area] = {
    var grid = List.empty[Area]
    require(dimension._1 * dimension._2 > numberOfAreas * 10)

    //Il magic number regola quanto grandi siano le singole aree
    val maxWidth = dimension._1 / (numberOfAreas / 2)
    val maxHeigth = dimension._2 / (numberOfAreas / 2)

    for (i <- defaultStartingX until dimension._1 by (dimension._1 / Math.round(Math.sqrt(numberOfAreas).toFloat))) {
      for (j <- defaultStartingY until dimension._2 by (dimension._2 / Math.round(Math.sqrt(numberOfAreas).toFloat))) {
        val startingPoint = Point(i, j)
        val areaWidth = Random.between(maxWidth/2, maxWidth)
        val areaHeight = Random.between(maxHeigth/2, maxHeigth)
        val rectangle = RectangleArea(startingPoint, Point(startingPoint.x + areaWidth, startingPoint.y + areaHeight))
        val newArea: Area = Area(Area.randomType, rectangle, Probability(Random.between(1,100)))
        grid = grid.::(newArea)
      }
    }
    grid
  }
}