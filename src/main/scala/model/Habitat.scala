package model

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
      for (area <- areas.filterNot(elem => elem == h)) {
        if (h.topLeft._1 > area.topLeft._1 && h.topLeft._1 < area.bottomRight._1 && h.topLeft._2 > area.topLeft._2 && h.topLeft._2 < area.bottomRight._2 ||
          h.bottomRight._1 > area.topLeft._1 && h.bottomRight._1 < area.bottomRight._1 && h.bottomRight._2 > area.topLeft._2 && h.bottomRight._2 < area.bottomRight._2 ||
          h.topLeft._1 == area.topLeft._1 && h.topLeft._2 == area.topLeft._2 || h.bottomRight._1 > area.bottomRight._1 && h.bottomRight._2 < area.bottomRight._2) {
          return false
        }
      }
      checkForOverlappingAreas(t)
    case _ => true
  }

  def checkDimensionsOfAreas(areas: Seq[Area]): Boolean = areas match {
    case h :: t =>
      if (h.topLeft._1 < 0 || h.topLeft._2 < 0 ||
        h.bottomRight._1 > this.dimensions._1 || h.bottomRight._2 > this.dimensions._2) {
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

  def apply(habitatType: HabitatType,
            unexpectedEvents: Probability,
            dimensions: (Int, Int),
            areas: Seq[Area]): Habitat = habitatType match {
    case EmptyHabitatType => EmptyHabitat(unexpectedEvents, dimensions, Seq.empty)
    case SimpleHabitatType => SimpleHabitat(unexpectedEvents, dimensions, areas)
    case RandomHabitatType => RandomHabitat(unexpectedEvents, dimensions, createRandomAreas(dimensions, 10))
    case GridHabitatType => GridHabitat(unexpectedEvents, dimensions, createGridArea(dimensions, 10))
  }

  def apply(unexpectedEvent: Probability, dimensions: (Int, Int)): Habitat =
    RandomHabitat(unexpectedEvent, dimensions, createRandomAreas(dimensions, 20))

  private case class SimpleHabitat(override val unexpectedEvents: Probability,
                                   override val dimensions: (Int, Int),
                                   override val areas: Seq[Area]) extends Habitat

  private case class EmptyHabitat(override val unexpectedEvents: Probability,
                                  override val dimensions: (Int, Int),
                                  override val areas: Seq[Area] ) extends Habitat

  private case class RandomHabitat(override val unexpectedEvents: Probability,
                                  override val dimensions: (Int, Int),
                                  override val areas: Seq[Area] ) extends Habitat

  private case class GridHabitat(override val unexpectedEvents: Probability,
                                   override val dimensions: (Int, Int),
                                   override val areas: Seq[Area] ) extends Habitat

  /**
   * This method create an habitat, of given dimension, with diven areas, arranged in a randow way
   * @param dimension dimension of the habitat
   * @param numberOfAreas num of areas in the grid
   * @return
   * TODO non viene controllato l'overlapping
   */
  def createRandomAreas(dimension: (Int, Int), numberOfAreas: Int) : Seq[Area] = {
    var grid = List.empty[Area]
    val habitatArea = dimension._1 * dimension._2
    require(dimension._1 * dimension._2 > numberOfAreas * 10)
    val maxAreaDimension = habitatArea / 2 * numberOfAreas

    for (_ <- 0 to numberOfAreas){
      val point = (Random.nextInt(dimension._1 - maxAreaDimension), Random.nextInt(dimension._2 - maxAreaDimension))
      val areaWidth = Random.shuffle(Range(1, maxAreaDimension).toList).head
      val areaHeigth = Random.shuffle(Range(1, maxAreaDimension).toList).head
      val newArea: Area = Area(Area.randomType, (point._1, point._2), (point._1 + areaWidth, point._2 + areaHeigth))

      grid = grid.::(newArea)
    }
    grid
  }

  /**
   * This method create an habitat, of given dimension, with diven areas, arranged in a grid
   * @param dimension dimension of the habitat
   * @param numberOfAreas num of areas in the grid
   * @return
   */
  def createGridArea(dimension: (Int, Int), numberOfAreas: Int) : Seq[Area] = {
    var grid = List.empty[Area]
    val habitatArea = dimension._1 * dimension._2
    require(dimension._1 * dimension._2 > numberOfAreas * 10)

    val maxAreaDimension = habitatArea / 2 * numberOfAreas

    for (i <- 0 to numberOfAreas/2){
      for(j <- 0 to numberOfAreas/2){
        val point = (i* maxAreaDimension, j* maxAreaDimension)
        val areaWidth = Random.shuffle(Range(1, maxAreaDimension).toList).head
        val areaHeight = Random.shuffle(Range(1, maxAreaDimension).toList).head
        val newArea: Area = Area(Area.randomType, (point._1, point._2), (point._1 + areaWidth, point._2 + areaHeight))
        grid = grid.::(newArea)
      }
    }
    println(grid)
    grid
  }

}




//creare degli enum e delle factory per avere mappe sempre diverse, o mappe statiche