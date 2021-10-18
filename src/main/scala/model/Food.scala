package model

import java.awt.Color

sealed trait FoodType
case object Meat extends FoodType
case object Vegetable extends FoodType

/**
 * Trait that represent a food.
 */
trait Food extends Visualizable {
  val energy: Int
  val foodType: FoodType
}

/**
 * Object that represent the a food.
 */
object Food {
  /**
   * Apply method for a Food.
   *
   * @param color  the color that should be used to draw the element.
   * @param energy the health it returns to an animal.
   * @return an implementation of Food.
   */
  def apply(color: Color, energy: Int, foodType: FoodType): Food =
    FoodImpl(color, energy, foodType)

  private case class FoodImpl(override val color: Color,
                              override val energy: Int,
                              override val foodType: FoodType,
                             ) extends Food
}
