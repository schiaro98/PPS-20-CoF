package model
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


trait FoodInstance extends Food with Placeable {
  val quantity: Int
  // TODO: qui ritorno un foodInstance che non esiste altrove
  /**
   * Method to decrease the quantity of the food.
   *
   * @param amount the quantity of food consumed.
   * @return a new food, the same as before but with de quantity decreased.
   * @throws IllegalArgumentException if the amount is greater than the quantity
   */
  def consume[F >: FoodInstance](amount: Int): F
}
