package model

import utility.{Constants, Point}
// TODO: we could add to Food a maxQuantity so that when we create a new FoodInstance we can use a random from 1 to maxQuantity
// TODO: add name to Food?
/**
 * Trait that represent a food.
 */
trait Food extends Visualizable {
  val energy: Int
}

/**
 * Object that represent the a food.
 */
object Food {
  /**
   * Apply method for a Food.
   *
   * @param icon   the image to draw in the map.
   * @param energy the health it returns to an animal.
   * @return an implementation of Food.
   */
  def apply(icon: String, energy: Int): Food =
    FoodImpl(icon, energy)

  private case class FoodImpl(override val icon: String,
                              override val energy: Int) extends Food
}
