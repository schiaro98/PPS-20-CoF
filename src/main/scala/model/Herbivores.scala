package model

import java.awt.image.BufferedImage

trait Herbivores extends Animal {

  /**
   * Herbivores can eat only Vegetables.
   *
   * @param food the Food to eat.
   * @return a pair that contains the animal with the health restored and the remaining food, if there is still any.
   * @throws IllegalArgumentException if the food to eat isn't a Vegetable.
   */
  override def eat(food: Food): (Animal, Option[Food]) = if (food.isInstanceOf[Vegetable]) super.eat(food) else throw new IllegalArgumentException
}

object Herbivores {

  /**
   * Apply method for an Herbivores.
   *
   * @param s         the species of the animal.
   * @param position  the location on the map, where the animal is.
   * @param direction the direction in which the animal is moving.
   * @param health    the parameter that indicates whether the animal is healthy.
   * @param thirst    the parameter that indicates whether the animal is thirsty.
   * @return a new implementation of Herbivores.
   */
  def apply(s: Species, position: (Int, Int), direction: (Int, Int), health: Int = 100, thirst: Int = 100): Herbivores =
    new HerbivoresImpl(s.icon, s.name, s.size, s.strength, s.sight, health, thirst, position, direction)

  private class HerbivoresImpl(override val icon: BufferedImage,
                           override val name: String,
                           override val size: Size,
                           override val strength: Int,
                           override val sight: Int,
                           override val health: Int,
                           override val thirst: Int,
                           override val position: (Int, Int),
                           override val direction: (Int, Int)) extends Herbivores
}