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
                            resources: Seq[FoodInstance] = Seq.empty,
                            updatedAnimals: Seq[Animal] = Seq.empty) : (Seq[Animal],Seq[FoodInstance]) = animals match {
        case h :: t =>
          val myAnimal = if(isAnimalNearToWater(h, habitat)){
            print("Ho trovato dell'acqua!!!\n\nYEEEE")
            h.drink()
          } else h

          val nearestResource = resources
            .filter(_.position.distance(myAnimal.position) < Constants.Hitbox)
            .filter(food => {
              if (myAnimal.alimentationType == Carnivore){
                food.foodType == Meat
              } else {
                food.foodType == Vegetable
              }
            })
            .minByOption(_.position.distance(myAnimal.position))

          if(nearestResource.isDefined) {
            nearestResource.get match {
              case x: FoodInstance =>
                val (updatedAnimal, remainedFood) = myAnimal.eat(x)

                if(remainedFood.isDefined) {
                  require(remainedFood.get.foodType == x.foodType, "Remaining food is not of same type of the original")
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

    def isAnimalNearToWater(animal: Animal, h: Habitat): Boolean = {
      @tailrec
      def _isAnimalNearToWater(areas: Seq[Area]): Boolean = areas.filter(_.areaType == Water) match {
        case h::t => h match {
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