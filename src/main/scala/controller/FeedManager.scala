package controller

import model._
import utility.Constants

sealed trait FeedManager {

  /**
   * Consume the nearest resource of the animal in the map
   * It manage also the decrease of health and thirst
   * @return a pair of the sequence of the animals updated and of the food still eatable
   */
  def consumeResources(): (Seq[Animal], Seq[FoodInstance])
}

object FeedManager {
  def apply(animals: Seq[Animal], resources: Seq[FoodInstance]): FeedManager = SimpleFeedManager(animals, resources)

  private case class SimpleFeedManager(animals: Seq[Animal], resources: Seq[FoodInstance]) extends FeedManager {

    override def consumeResources(): (Seq[Animal], Seq[FoodInstance]) = {
      val results: (Seq[Animal], Seq[FoodInstance]) = (Seq.empty, Seq.empty)

      animals.foreach(animal => {
        resources
          .filter(food => food.position.distance(animal.position) < Constants.hitbox)
          .minByOption(food => food.position.distance(animal.position)).get match {
          case x: Meat if animal.alimentationType == Carnivore => results +: Seq(animal.eat(x))
          case x: Vegetable if animal.alimentationType == Herbivore => results +: Seq(animal.eat(x))
          case _ =>
        }
      })

      results._1.map(animal => {
        animal.update(
            health = animal.health - Constants.healthDecrease,
            thirst = animal.thirst - Constants.thirstDecrease,
          position = animal.position
        )
      })
      results
    }
  }
}

