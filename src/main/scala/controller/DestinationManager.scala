package controller

import model._
import utility.Point

import scala.annotation.tailrec

sealed trait DestinationManager {
  /**
   * For any given animals, calculate the target of the movement
   * For an herbivore, it should point to the more near vegetables or a random location if it can't see anything
   * For a Carnivore, it should point to the more near herbivore animal, or a random location
   * @return
   */
  def calculateDestination() : Map[Animal, Point]

}

object DestinationManager {
  def apply(animals: Seq[Animal],
                            resources: Seq[FoodInstance],
                            habitat: Habitat) : DestinationManager =
    DestinationManagerImpl(animals, resources, habitat)

private case class DestinationManagerImpl(animals: Seq[Animal], food: Seq[FoodInstance], habitat: Habitat) extends DestinationManager {

  override def calculateDestination(): Map[Animal, Point] = {
    var destination: Map[Animal, Point] = Map.empty
    animals.foreach(animal => {
      if(animal.thirst < 50 && findNearestWaterZone(animal, habitat).isDefined){
        destination = destination + (animal -> findNearestWaterZone(animal, habitat).get)
      } else {
        val point = animal.alimentationType match {
          case Herbivore => findNearestResource(animal, food.filter(resource => resource.isInstanceOf[Vegetable]))
            .getOrElse(getLegalRandomPoint(habitat))
          case Carnivore =>
            findNearestResource(animal, animals.filter(animal => animal.alimentationType == Herbivore)).getOrElse(
              findNearestResource(animal,
                food.filter(resource => resource.isInstanceOf[Meat]))
                .getOrElse(getLegalRandomPoint(habitat)))
        }
        destination = destination + (animal -> point)
      }
    })
    destination
  }

   def findNearestResource[P <: Placeable](animal: Animal, resources: Seq[P]): Option[Point] = {
     resources
       .map(resource => resource.position)
       .filter(point => point.distance(animal.position) < animal.sight)
       .minByOption(point => point.distance(animal.position))
   }

  @tailrec
  final def getLegalRandomPoint(h: Habitat): Point = {
    val p = Point(0,0).getRandomPoint(h.dimensions)
    if (h.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(p)) == 0) p else getLegalRandomPoint(h)
  }

  def findNearestWaterZone(animal:Animal, h: Habitat) : Option[Point] = {
    h.areas
      .filter(area => area.areaType == Water)
      .map(area => area.area.topLeft) //TODO mettere punto random tra topl e bottr
      .filter(point => point.distance(animal.position) < animal.sight)
      .minByOption(point => point.distance(animal.position))
  }

}

}