package model.habitat

import model.position.Point
import model.shape.RectangleArea
import model.{Probability, shape}
import utility.{Constants, OursMap}

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
 * Trait that represents an habitat.
 * Every habitat has a dimension (width x height), a [[Probability]] that some unexpected events happen and kill some
 * animal and a [[Seq]] of [[Area]] that can have different behaviours
 */
trait Habitat {
  val unexpectedEvents: Probability
  val dimensions: (Int, Int)
  val areas: Seq[Area]

  /**
   * Check if in the sequence exist overlapping areas
   * @param areas to be checked
   * @return true if any of the area overlap
   */
  def checkForOverlappingAreas(areas: Seq[Area]): Boolean = areas match {
    case h :: t =>
      for (a <- areas.filterNot(elem => elem == h)) {
        if (h.area.overlap(a.area)) return false
      }
      checkForOverlappingAreas(t)
    case _ => true
  }

  /**
   * Check if areas has got illegal points (exceed dimensions or are not rectangles)
   * @param areas to be checked
   * @return true if any of the areas is illegal
   */
  def checkDimensionsOfAreas(areas: Seq[Area]): Boolean = areas match {
    case h :: t =>
      if(h.area.topLeft.x < 0 || h.area.topLeft.y < 0 ||
        h.area.bottomRight.x > this.dimensions._1 || h.area.bottomRight.y > this.dimensions._2)
        false
      else checkDimensionsOfAreas(t)
    case _ => true
  }

  require(checkForOverlappingAreas(areas), "overlapping areas")
  require(checkDimensionsOfAreas(areas), "areas don't fit in current habitat")
}

/**
 * Object that represent an habitat
 */
object Habitat {

  /**
   * Apply method for [[Habitat]].
   *
   * @param unexpectedEvents the [[Probability]] that some event occur
   * @param dimensions the diemsion of the habitat expressed as [[(Int, Int)]]
   * @param areas the [[Seq]] of [[Area]] that are contained in the habitat
   * @return an implementation of [[Habitat]]
   */
  def apply(unexpectedEvents: Probability = Probability(0),
            dimensions: (Int, Int) = Constants.MainMapDimension,
            areas: Seq[Area] = OursMap.areas
           ): Habitat = SimpleHabitat(unexpectedEvents, dimensions, areas)

  /**
   * Apply method for [[Habitat]].
   *
   * @param habitatType can be [[EmptyHabitatType]], [[SimpleHabitatType]], [[RandomHabitatType]] or [[GridHabitatType]]
   * @param unexpectedEvents the [[Probability]] that some event occur
   * @param dimensions the diemsion of the habitat
   * @param areas the [[Seq]] of [[Area]] that are contained in the habitat
   * @return an implementation of [[Habitat]]
   */
  def apply(habitatType: HabitatType,
            unexpectedEvents: Probability,
            dimensions: (Int, Int),
            areas: Seq[Area],
           ): Habitat = habitatType match {
    case EmptyHabitatType => SimpleHabitat(unexpectedEvents, dimensions, Seq.empty)
    case SimpleHabitatType => SimpleHabitat(unexpectedEvents, dimensions, areas)
    case RandomHabitatType => SimpleHabitat(unexpectedEvents, dimensions, createRandomAreas(dimensions))
    case GridHabitatType => SimpleHabitat(unexpectedEvents, dimensions, createGridArea(dimensions, Constants.DefaultGridSize))
    case _ => throw new IllegalArgumentException("Habitat type error on method apply")
  }
  
  /**
   * Method used to obtain a random [[AreaType]]
   *
   * @return a random [[AreaType]]
   */
  private def randomAreaType: AreaType = Random.shuffle(Area.getAllTypeOfArea).head

  /**
   * Method used to obtain a safe random [[AreaType]]
   *
   * @return a safe random [[AreaType]]
   */
  private def safeRandomAreaType: AreaType = if (Random.between(0, 100) > 80) Water else Fertile


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
    require(dimension._1 * dimension._2 > numberOfAreas * 10)
    val r = RectangleArea(Point(0, 0), Point(1, 1))
    r.getIn4Quadrant(dimension).map(rectArea => Area(randomAreaType, rectArea, Probability(Random.between(1, 100))))
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
    val limit = Point(dimension._1, dimension._2)
    require(dimension._1 * dimension._2 > numberOfAreas * numberOfAreas, "Dimension of habitat are too small")
    val maxWidth = dimension._1 / numberOfAreas
    val maxHeigth = dimension._2 / numberOfAreas
    for (i <- 0 until dimension._1 by maxWidth) {
      for (j <- 0 until dimension._2 by maxHeigth) {
        val rectangle = shape.RectangleArea(Point(i,j), Point(i,j) + Point(maxWidth, maxHeigth))
        val newArea: Area = Area(safeRandomAreaType, rectangle, Probability(Random.between(1, 100)))
        grid = grid.::(newArea)
      }
    }
   grid.filter(area =>
        area.area.topLeft.x < limit.x &&
        area.area.topLeft.y < limit.y &&
        area.area.bottomRight.x < limit.x &&
        area.area.bottomRight.y < limit.y
    )
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