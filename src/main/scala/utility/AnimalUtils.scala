package utility

import model._

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
    val (width, height) = habitat.dimensions
    val pixel = getPixelFromSize(species)
    var p = Point.getRandomPoint((width - pixel, height - pixel))
    while (areNotPlaceable(habitat.areas, getSquareVertices(p, pixel))) {
      p = Point.getRandomPoint((width - pixel, height - pixel))
    }
    p
  }

  /**
   * Method to obtain the pixel used to calculate the dimension of an animal; the value returned is also used to
   * draw the animal because represent the side of the square
   *
   * @param species the [[Species]] of the animal
   * @return the dimension of the animal in pixel
   */
  def getPixelFromSize(species: Species): Int = species.size match {
    case Big => Constants.PixelForBig
    case Medium => Constants.PixelForMedium
    case Small => Constants.PixelForSmall
  }

  /**
   *
   * @param s the [[Species]] of which we want to know the corners
   * @param p the [[Point]] in which the [[Species]] is or will be
   * @return the [[Seq]] of [[Point]] corresponding to the corners
   */
  def getCornersOfSpeciesInPoint(s: Species, p: Point):Seq[Point]= {
    getSquareVertices(p, getPixelFromSize(s))
  }

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

  /**
   * @param topLeft the top left vertices of the square
   * @param side the side of the square
   * @return a Seq of [[Point]] that contains the vertices of the square
   */
  private def getSquareVertices(topLeft: Point, side: Int): Seq[Point] =
    Seq(topLeft, Point(topLeft.x+side, topLeft.y), Point(topLeft.x, topLeft.y+side), Point(topLeft.x+side, topLeft.y+side))
}
