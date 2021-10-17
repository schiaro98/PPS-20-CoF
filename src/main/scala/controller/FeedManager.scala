package controller

import model._
import utility.Constants

sealed trait FeedManager {
  def consumeResources(): Unit
}

object FeedManager {
  def apply(animals: Seq[Animal], resources: Seq[FoodInstance]): FeedManager = SimpleFeedManager(animals, resources)

  private case class SimpleFeedManager(animals: Seq[Animal], resources: Seq[FoodInstance]) extends FeedManager {

    override def consumeResources(): Unit = {
      animals.foreach(animal => {
        resources
          .filter(food => food.position.distance(animal.position) < Constants.hitbox)
          .minByOption(food => food.position.distance(animal.position)).get match {
          case x: Meat if animal.alimentationType == Carnivore => animal.eat(x)
          case x: Vegetable if animal.alimentationType == Herbivore => animal.eat(x)
          case _ =>
        }
      })
    }
  }
}

