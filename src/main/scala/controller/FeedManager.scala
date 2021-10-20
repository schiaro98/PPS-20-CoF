package controller

import model._
import utility.Constants

import scala.annotation.tailrec

sealed trait FeedManager {

  /**
   * Consume the nearest resource of the animal in the map
   * It manage also the decrease of health and thirst
   * @return a pair of the sequence of the animals updated and of the food still eatable
   */
  def consumeResources(): (Seq[Animal],Seq[FoodInstance])
}

object FeedManager {
  def apply(animals: Seq[Animal], resources: Seq[FoodInstance], habitat: Habitat): FeedManager = SimpleFeedManager(animals, resources, habitat)

  private case class SimpleFeedManager(animals: Seq[Animal], resources: Seq[FoodInstance], habitat: Habitat) extends FeedManager {

    override def consumeResources():(Seq[Animal],Seq[FoodInstance]) = {

      @tailrec
      def _consumeResources(animals: Seq[Animal],
                            resources: Seq[FoodInstance],
                            updatedAnimals: Seq[Animal] = Seq.empty) : (Seq[Animal],Seq[FoodInstance]) = animals match {
        case h :: t =>

          val nearestWaterArea = findNearestWaterZone(h, habitat)
          val myAnimal = if(nearestWaterArea.isDefined){
            h.drink()
          } else h

          val nearestResource = resources
            .filter(_.position.distance(myAnimal.position) < Constants.hitbox)
            .minByOption(_.position.distance(myAnimal.position))

          if(nearestResource.isDefined) {
            nearestResource.get match {
              case x: FoodInstance if x.foodType == Meat && myAnimal.alimentationType == Carnivore || x.foodType == Vegetable && myAnimal.alimentationType == Herbivore =>
                val (updatedAnimal, remainedFood) = myAnimal.eat(x)

                if(remainedFood.isDefined) {
                  _consumeResources(t, resources.filterNot(_ == x) :+ remainedFood.get , updatedAnimals :+ updatedAnimal)
                } else {
                  _consumeResources(t, resources.filterNot(_ == x), updatedAnimals :+ updatedAnimal)
                }
              case _ => _consumeResources(t, resources, updatedAnimals :+ myAnimal)

            }
          } else _consumeResources(t, resources, updatedAnimals :+ myAnimal)
        case _ => (updatedAnimals, resources)

      }
      _consumeResources(animals, resources)
    }

    def findNearestWaterZone(animal: Animal, h: Habitat): Option[Point] = {
      h.areas
        .filter(_.areaType == Water)
        .map(rectangle => rectangle.area.topLeft)
        .filter(_.distance(animal.position) < Constants.hitbox)
        .minByOption(_.distance(animal.position))
    }
  }
}