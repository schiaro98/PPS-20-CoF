package model

import utility.Constants._
import utility.Point

/**
 * Trait that represent a carnivorous animal.
 */
trait Carnivorous extends Animal {

  /**
   * Carnivores can eat only Meat.
   *
   * @param food the Food to eat.
   * @return a pair that contains the animal with the health restored and the remaining food, if there is still any.
   * @throws IllegalArgumentException if the food to eat isn't Meat.
   */
  override def eat(food: FoodInstance): (Animal, Option[FoodInstance]) = food match {
    case _: Meat => super.eat(food)
    case _ => throw new IllegalArgumentException
  }
}

/**
 * Object that represent a carnivorous animal.
 */
object Carnivorous {

  /**
   * Apply method for a Carnivorous.
   *
   * @param s         the species of the animal.
   * @param position  the location on the map, where the animal is.
   * @param health    the parameter that indicates whether the animal is healthy.
   * @param thirst    the parameter that indicates whether the animal is thirsty.
   * @return a new implementation of Carnivorous.
   */
  def apply(s: Species, position: Point, health: Int = MaxHealth, thirst: Int = MaxThirst): Carnivorous =
    new CarnivorousImpl(s.icon, s.name, s.size, s.strength, s.sight, health, thirst, position)

  private class CarnivorousImpl(override val icon: String,
                                override val name: String,
                                override val size: Size,
                                override val strength: Int,
                                override val sight: Int,
                                override val health: Int,
                                override val thirst: Int,
                                override val position: Point) extends Carnivorous
}