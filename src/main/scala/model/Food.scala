package model
// TODO: we could add to Food a maxQuantity so that when we create a new FoodInstance we can use a random from 1 to maxQuantity

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

  /**
   * Method to decrease the quantity of the food.
   *
   * @param amount the quantity of food consumed.
   * @return a new food, the same as before but with de quantity decreased.
   * @throws IllegalArgumentException if the amount is greater than the quantity
   */
  def consume(amount: Int): FoodInstance = if (quantity > amount) FoodInstance(icon, energy, quantity - amount, position) else throw new IllegalArgumentException
}

/**
 * Object that represent the instance of a food.
 */
object FoodInstance {
  /**
   * Apply method for a Food; it's private because it must be impossible to instantiate a general food,
   * it must be a meat or a vegetable
   *
   * @param icon     the image to draw in the map.
   * @param energy   the health it returns to an animal.
   * @param quantity the number of times it returns the energy to an animal before running out.
   * @param position the location on the map, where the food is.
   * @return an implementation of FoodInstance.
   */
  private def apply(icon: String, energy: Int, quantity: Int, position: (Int, Int)): FoodInstance =
    new FoodInstanceImpl(icon, energy, quantity, position)

  //todo serve il secondo apply?
  private def apply(food: Food, quantity: Int, position: (Int, Int)): FoodInstance =
    new FoodInstanceImpl(food.icon, food.energy, quantity, position)

  private class FoodInstanceImpl(override val icon: String,
                                 override val energy: Int,
                                 override val quantity: Int,
                                 override val position: (Int, Int)) extends FoodInstance
}