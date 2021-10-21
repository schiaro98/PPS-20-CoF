package utility

import model._

import scala.annotation.tailrec

/**
 * Object containing some utility methods for [[Animal]]
 */
object AnimalUtils {

  /**
   * Method used to obtain a random permissible point where is possible to create an animal of a certain species.
   *
   * @param species the [[Species]] of the animal.
   * @return the [[Point]] (top left) where is possible to create the animal.
   */
  def placeAnimal(habitat: Habitat, species: Species): Point = {
    @tailrec
    def placeAnimal_(width: Int, height: Int, species: Species, pixel: Int): Point = {
      val point = Point.getRandomPoint((width - pixel, height - pixel))
      if (areNotPlaceable(habitat.areas, getSquareVertices(point, pixel))) {
        placeAnimal_(width, height, species, pixel)
      } else {
        point
      }
    }
    placeAnimal_(habitat.dimensions._1, habitat.dimensions._2, species, getPixelFromSize(species))
  }

  /**
   * Method to obtain the pixel used to calculate the dimension of an animal; the value returned is also used to
   * draw the animal because represent the side of the square.
   *
   * @param species the [[Species]] of the animal.
   * @return the dimension of the animal in pixel.
   */
  def getPixelFromSize(species: Species): Int = species.size match {
    case Big => Constants.PixelForBig
    case Medium => Constants.PixelForMedium
    case Small => Constants.PixelForSmall
  }

  /**
   * Method to obtain the vertices of the square that represents an [[Animal]].
   *
   * @param s the [[Species]] of the [[Animal]].
   * @param p the [[Point]] in which the [[Animal]] is or will be.
   * @return a [[Seq]] of [[Point]] corresponding to the vertices of the square.
   */
  def verticesOfAnimal(s: Species, p: Point):Seq[Point]= getSquareVertices(p, getPixelFromSize(s))

  /**
   * Method used to obtain the [[Point]]s corresponding to the vertices of a square.
   *
   * @param topLeft the top left vertice of the square.
   * @param side    the side of the square.
   * @return a Seq of [[Point]] that contains the vertices of the square.
   */
  private def getSquareVertices(topLeft: Point, side: Int): Seq[Point] =
    Seq(topLeft, Point(topLeft.x + side, topLeft.y), Point(topLeft.x, topLeft.y + side), Point(topLeft.x + side, topLeft.y + side))

  /**
   * Check if a sequence of point is in a non-walkable area.
   *
   * @param areas a set [[Area]] in which the [[Point]] can be placed.
   * @param points a set of [[Point]] whose positions are to be checked.
   * @return true if at least one point is not placeable because was in a non-walkable area, otherwise true.
   */
  private def areNotPlaceable(areas: Seq[Area], points: Seq[Point]): Boolean = points.exists(point => isNotPlaceable(areas, point))

  /**
   * Check if a point is in a non-walkable area.
   *
   * @param areas a set [[Area]] in which the [[Point]] can be placed.
   * @param point the [[Point]] whose position is to be checked.
   * @return true if the point is not placeable because was in a non-walkable area, otherwise true.
   */
  private def isNotPlaceable(areas: Seq[Area], point: Point): Boolean =
    areas.filterNot(_.areaType.walkable).exists(_.area.contains(point))
}
