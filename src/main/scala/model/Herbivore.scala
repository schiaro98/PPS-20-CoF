package model

import utility.Constants._

/**
 * Trait that represent an herbivorous animal.
 */
trait Herbivore extends Animal {

  /**
   * Herbivores can eat only Vegetables.
   *
   * @param food the Food to eat.
   * @return a pair that contains the animal with the health restored and the remaining food, if there is still any.
   * @throws IllegalArgumentException if the food to eat isn't a Vegetable.
   */
  override def eat(food: FoodInstance): (Animal, Option[FoodInstance]) = food match {
    case _: Vegetable => super.eat(food)
    case _ => throw new IllegalArgumentException
  }
}

/**
 * Object that represent an herbivore animal.
 */
object Herbivore {

  /**
   * Apply method for an Herbivore.
   *
   * @param s         the species of the animal.
   * @param position  the location on the map, where the animal is.
   * @param direction the direction in which the animal is moving.
   * @param health    the parameter that indicates whether the animal is healthy.
   * @param thirst    the parameter that indicates whether the animal is thirsty.
   * @return a new implementation of Herbivore.
   */
  def apply(s: Species, position: (Int, Int), direction: (Int, Int), health: Int = MaxHealth, thirst: Int = MaxThirst): Herbivore =
    new HerbivoreImpl(s.icon, s.name, s.size, s.strength, s.sight, health, thirst, position, direction)

  private class HerbivoreImpl(override val icon: String,
                              override val name: String,
                              override val size: Size,
                              override val strength: Int,
                              override val sight: Int,
                              override val health: Int,
                              override val thirst: Int,
                              override val position: (Int, Int),
                              override val direction: (Int, Int)) extends Herbivore
}