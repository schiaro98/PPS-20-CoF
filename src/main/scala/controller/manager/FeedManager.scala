package controller.manager

import model.animal.{Animal, Carnivore, Herbivore}
import model.food.{Food, Meat, Vegetable}
import model.habitat.{Area, Habitat, Water}
import utility.Constants

import scala.annotation.tailrec

sealed trait FeedManager {

  /**
   * Consume the nearest resource of the animal in the map
   * It manage also the decrease of health and thirst
   * @return a pair of the sequence of the animals updated and of the food still eatable
   */
  def consumeResources(): (Seq[Animal],Seq[Food])
}

object FeedManager {
  def apply(animals: Seq[Animal], resources: Seq[Food], habitat: Habitat): FeedManager = SimpleFeedManager(animals, resources, habitat)

  private case class SimpleFeedManager(animals: Seq[Animal], resources: Seq[Food], habitat: Habitat) extends FeedManager {

    override def consumeResources():(Seq[Animal],Seq[Food]) = {

      def isEatable(food: Food, animal: Animal): Boolean = {
        (food.foodCategory == Meat && animal.alimentationType == Carnivore) ||
          (food.foodCategory == Vegetable && animal.alimentationType == Herbivore)
      }

      @tailrec
      def _consumeResources(animals: Seq[Animal],
                            resources: Seq[Food] = Seq.empty,
                            updatedAnimals: Seq[Animal] = Seq.empty) : (Seq[Animal],Seq[Food]) = animals match {
        case h :: t =>
          val myAnimal = if(isAnimalNearToWater(h, habitat)){
            h.drink()
          } else h

          val nearestResource = resources
            .filter(_.position.distance(myAnimal.position) < Constants.HitBox)
            .filter(isEatable(_, myAnimal))
            .minByOption(_.position.distance(myAnimal.position))

          if(nearestResource.isDefined) {
            nearestResource.get match {
              case x: Food =>
                val (updatedAnimal, remainedFood) = myAnimal.eat(x)

                if(remainedFood.isDefined) {
                  require(remainedFood.get.foodCategory == x.foodCategory, "Remaining food is not of same type of the original")
                  require(remainedFood.get.quantity <= x.quantity, "Remaining food quantity is greater than original")
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

    /**
     * Find if an [[Animal]] is near (or not) a water zone
     * @param animal who needs to drink
     * @param h the habitat
     * @return true if the animal is near the water zone, false otherwise
     */
    def isAnimalNearToWater(animal: Animal, h: Habitat): Boolean = {
      @tailrec
      def _isAnimalNearToWater(areas: Seq[Area]): Boolean = areas.filter(_.areaType == Water) match {
        case h :: t => h match {
          case h =>
            if( h.area.topLeft.distance(animal.position) < animal.sight ||
              h.area.bottomRight.distance(animal.position) < animal.sight){
              true
            } else {
              _isAnimalNearToWater(t)
            }
        }
        case _ => false
      }
      _isAnimalNearToWater(h.areas)
    }
  }
}