package utility

import model.{Animal, Area, Big, Habitat, Medium, Small, Species}

import scala.util.Random

object AnimalUtils {


  /**
   * Method to create a number of animals for each species equal to the one in the Map.
   *
   * @param population all the [[Species]] with the number of animals to create at the beginning of the simulation.
   * @param habitat the [[Habitat]] where the simulation takes place.
   * @return the created animals.
   */
  def generateInitialAnimals(population: Map[Species, Int], habitat: Habitat): Seq[Animal] = {
    var animals = Seq.empty[Animal]
    population foreach (s => {
      for (_ <- 1 to s._2) {
        val point = AnimalUtils.placeAnimal(habitat, s._1)
        val animal = Animal(s._1, point)
        animals = animals :+ animal
      }
    })
    animals
  }

  /**
   * Method used to obtain a random permissible point where is possible to create an animal of a certain species.
   *
   * @param species the Species of the animal.
   * @return the [[Point]] (top left) where is possible to create the animal.
   */
  def placeAnimal(habitat: Habitat, species: Species): Point = {
    val (width, height) = habitat.dimensions
    val pixel = getPixelFromSize(species)
    //TODO usare val randomPoint = Point.getRandomPoint() ?
    var x = Random.nextInt(width - pixel)
    var y = Random.nextInt(height - pixel)
    while (areNotPlaceable(habitat.areas, Seq(Point(x, y), Point(x+pixel, y), Point(x, y+pixel), Point(x+pixel, y+pixel)))) {
      x = Random.nextInt(width - pixel)
      y = Random.nextInt(height - pixel)
    }
    Point(x, y)
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
   * Check if a sequence of point is in a non-walkable area.
   *
   * @param areas a set [[Area]] in which the [[Point]] can be placed.
   * @param points a set of [[Point]] whose positions are to be checked.
   * @return true if at least one point is not placeable because was in a non-walkable area, otherwise true.
   */
  def areNotPlaceable(areas: Seq[Area], points: Seq[Point]): Boolean = points.exists(point => isNotPlaceable(areas, point))

  /**
   * Check if a point is in a non-walkable area.
   *
   * @param areas a set [[Area]] in which the [[Point]] can be placed.
   * @param point the [[Point]] whose position is to be checked.
   * @return true if the point is not placeable because was in a non-walkable area, otherwise true.
   */
  def isNotPlaceable(areas: Seq[Area], point: Point): Boolean = {
    Constants.NonWalkableArea.contains(areas.find(a => a.area.contains(point)).getOrElse(return false).areaType)
  }

}
