package controller

import model.{Point, _}

import scala.annotation.tailrec

sealed trait DestinationManager {
  /**
   * For any given [[Animal]], calculate the target of the movement
   * For an [[Herbivore]], it should point to the more near vegetables or a random location if it can't see anything
   * For a [[Carnivore]], it should point to the more near herbivore animal, or a random location
   *
   * @return
   */
  def calculateDestination(): Map[Animal, Point]

}

object DestinationManager {
  def apply(animals: Seq[Animal],
            resources: Seq[FoodInstance],
            habitat: Habitat): DestinationManager =
    DestinationManagerImpl(animals, resources, habitat)

  private case class DestinationManagerImpl(animals: Seq[Animal], food: Seq[FoodInstance], habitat: Habitat) extends DestinationManager {

    override def calculateDestination(): Map[Animal, Point] = {
      var destination: Map[Animal, Point] = Map.empty
      animals.foreach(animal => {
        val neareastWaterZone = findNearestWaterZone(animal, habitat)
        if (animal.thirst < 50 && neareastWaterZone.isDefined) {
          destination = destination + (animal -> neareastWaterZone.get)
        } else {
          val point = animal.alimentationType match {
            case Herbivore => findNearestResource(animal, food.filter(_.foodType == Vegetable))
              .getOrElse(getLegalRandomPoint(habitat))
            case Carnivore =>
              findNearestResource(animal, animals.filter(_.alimentationType == Herbivore)).getOrElse(
                findNearestResource(animal,
                  food.filter(_.foodType == Meat))
                  .getOrElse(getLegalRandomPoint(habitat)))
          }
          destination = destination + (animal -> point)
        }
      })
      destination
    }

    /**
     * Find the nearest resource
     *
     * @param animal    animal target of the control
     * @param resources seq to be checked for find the nearest
     * @tparam P describe a Placeable that can be an animal or a foodIstance
     * @return
     */
    def findNearestResource[P <: Placeable](animal: Animal, resources: Seq[P]): Option[Point] = {
      resources
        .map(resource => resource.position)
        .filter(_.distance(animal.position) < animal.sight)
        .minByOption(_.distance(animal.position))
    }

    /**
     * Find a random legal point inside the habitat
     * Legal point are point not inside not walkable areas
     *
     * @param h the habitat
     * @return the random point
     */
    @tailrec
    final def getLegalRandomPoint(h: Habitat): Point = {
      val p = Point.getRandomPoint(h.dimensions)
      if (h.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(p)) == 0) p else getLegalRandomPoint(h)
    }

    /**
     * Find the nearest water zone
     *
     * @param animal that need water zone
     * @param h      the habitat
     * @return a random point inside the water zone
     */
    def findNearestWaterZone(animal: Animal, h: Habitat): Option[Point] = {
      h.areas
        .filter(_.areaType == Water)
        .map(rectangle => Point.getRandomPoint(rectangle.area.topLeft, rectangle.area.bottomRight))
        .filter(_.distance(animal.position) < animal.sight)
        .minByOption(_.distance(animal.position))
    }

  }

}