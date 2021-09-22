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

  /*def checkForOverlappingAreas(areas: Seq[Area]): Boolean = areas match {
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
  }*/

  def checkForOverlappingAreas(areas: Seq[Area]): Boolean = areas match {
    case h :: t =>
      for (area <- areas.filterNot(elem => elem == h)) {
        if(h.topLeft._1 >= area.bottomRight._1 && h.bottomRight._1 <= area.bottomRight._1 && h.topLeft._2 >= area.bottomRight._2 && h.bottomRight._2 <= area.bottomRight._2){
          println(s"A with coordinates (${h.topLeft}, ${h.bottomRight} is overlapping with B ${area.topLeft}, ${area.bottomRight}")
          return true
        }
      }
      checkForOverlappingAreas(t)
    case _ => true
  }

  def checkDimensionsOfAreas(areas: Seq[Area]): Boolean = areas match {
    case h :: t =>

      if(h.topLeft._1 < 0 || h.topLeft._2 <0){
        println("Invalid area point, below 0")
        return false
      }
      if( h.bottomRight._1 > this.dimensions._1 || h.bottomRight._2 > this.dimensions._2){
        println(s"A with coordinates (${h.topLeft}, ${h.bottomRight} is exceeding limits of ${this.dimensions._1}, ${this.dimensions._2}")
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
    case RandomHabitatType => SimpleHabitat(unexpectedEvents, dimensions, createRandomAreas(dimensions, 10))
    case GridHabitatType => SimpleHabitat(unexpectedEvents, dimensions, createGridArea(dimensions, 100))
  }

  def apply(unexpectedEvent: Probability, dimensions: (Int, Int)): Habitat =
    SimpleHabitat(unexpectedEvent, dimensions, createRandomAreas(dimensions, 20))

  private case class SimpleHabitat(override val unexpectedEvents: Probability,
                                   override val dimensions: (Int, Int),
                                   override val areas: Seq[Area]) extends Habitat


  /**
   * This method create an habitat, of given dimension, with diven areas, arranged in a randow way
   * @param dimension dimension of the habitat
   * @param numberOfAreas num of areas in the grid
   * @return
   * TODO non viene controllato l'overlapping
   */
  def createRandomAreas(dimension: (Int, Int), numberOfAreas: Int) : Seq[Area] = {
    /*
    TODO per la creazione di habitat random, creaiamo una grid area molto fitta e cancelliamo randomicamente fino ad avere n aree
     */
    Seq.empty
  }

  /**
   * This method create an habitat, of given dimension, with diven areas, arranged in a grid
   * @param dimension dimension of the habitat
   * @param numberOfAreas num of areas in the grid
   * @return
   */
  def createGridArea(dimension: (Int, Int), numberOfAreas: Int) : Seq[Area] = {
    var grid = List.empty[Area]
    require(dimension._1 * dimension._2 > numberOfAreas * 10)

    /*
    IL magic number regola quanto grandi siano le singole aree
     */
    val maxWidth = dimension._1 / (numberOfAreas / 2)
    val maxHeigth = dimension._2 / (numberOfAreas / 2)

    for(i <- 100 until  dimension._1 by (dimension._1 / Math.round(Math.sqrt(numberOfAreas).toFloat))){
      for(j <- 100 until  dimension._2 by (dimension._2 / Math.round(Math.sqrt(numberOfAreas).toFloat))){
        val point = (i, j)
        val areaWidth = Random.shuffle(Range(maxWidth/2, maxWidth).toList).head
        val areaHeight = Random.shuffle(Range(maxHeigth/2, maxHeigth).toList).head
        val newArea: Area = Area(Area.randomType, (point._1, point._2), (point._1 + areaWidth, point._2 + areaHeight))
        grid = grid.::(newArea)
      }
    }
    println(grid)
    grid
  }

}




//creare degli enum e delle factory per avere mappe sempre diverse, o mappe statiche