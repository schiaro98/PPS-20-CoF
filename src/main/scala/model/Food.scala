package model

import java.awt.Color
// TODO: we could add to Food a maxQuantity so that when we create a new FoodInstance we can use a random from 1 to maxQuantity
// TODO: add name to Food?

/**
 * Trait that represent a food.
 */
trait Food extends Visualizable {
  val energy: Int
}

// TODO: add type on food that says if it is vegetable or meat, rifare serializzatore
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
  def apply(color: Color, energy: Int): Food =
    FoodImpl(color, energy)

  private case class FoodImpl(override val color: Color,
                              override val energy: Int) extends Food
}
