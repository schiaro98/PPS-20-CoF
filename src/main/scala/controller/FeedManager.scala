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
  def consumeResources(): (Seq[Animal], Seq[FoodInstance])

  /**
   * Update (decrease) the current thirst and health
   */
  def lifeCycleUpdate(): Seq[Animal]
}

object FeedManager {
  def apply(animals: Seq[Animal], resources: Seq[FoodInstance]): FeedManager = SimpleFeedManager(animals, resources)

  private case class SimpleFeedManager(animals: Seq[Animal], resources: Seq[FoodInstance]) extends FeedManager {

    override def consumeResources(): (Seq[Animal], Seq[FoodInstance]) = {
      @tailrec
      def _consumeResources(animals: Seq[Animal],
                            remainingFood: Seq[FoodInstance] = Seq.empty,
                            updatedAnimals: Seq[Animal] = Seq.empty) : (Seq[Animal], Seq[FoodInstance]) = animals match {
        case h :: t =>

          val nearestResource = resources
            .filter(_.position.distance(h.position) < Constants.hitbox)
            .minByOption(_.position.distance(h.position))

          if(nearestResource.isDefined) {
            nearestResource.get match {
              case x: FoodInstance if x.foodType == Meat && h.alimentationType == Carnivore || x.foodType == Vegetable && h.alimentationType == Herbivore =>
                val res = h.eat(x)
                if(res._2.isDefined) {
                  val remainedFood: FoodInstance = res._2.get
                  _consumeResources(t, (remainingFood diff Seq(x)) :+ remainedFood , updatedAnimals :+ res._1)
                } else {
                  _consumeResources(t, remainingFood diff Seq(x), updatedAnimals :+ res._1)
                }
              case _ => throw new IllegalArgumentException("FeedManager error")
            }
          } else {
            _consumeResources(t, remainingFood, updatedAnimals :+ h)
          }
        case _ => (updatedAnimals, remainingFood)
      }
      _consumeResources(animals, resources)
    }

    /**
     * Update (decrease) the current thirst and health
     */
    override def lifeCycleUpdate(): Seq[Animal] = {
      animals.map(animal => {
        animal.update(
          health = animal.health - Constants.healthDecrease,
          thirst = animal.thirst - Constants.thirstDecrease,
          position = animal.position
        )
      })
    }
  }

}