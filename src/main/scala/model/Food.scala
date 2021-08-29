package model

/**
 * Trait that represent a food.
 */
trait Food extends Visualizable {
  val energy: Int
  val quantity: Int
  val position: (Int, Int) //todo fare una interfaccia con position implementata dagli elementi da disegnare?

  /**
   * Method to decrease the quantity of the food.
   *
   * @param amount the quantity of food consumed.
   * @return a new food, the same as before but with de quantity decreased.
   * @throws IllegalArgumentException if the amount is greater than the quantity
   */
  def consume(amount: Int): Food = if (quantity > amount) Food(icon, energy, quantity - amount, position) else throw new IllegalArgumentException
}

/**
 * Object that represent a food.
 */
private object Food {
  /**
   * Apply method for a Food.
   *
   * @param icon     the image to draw in the map.
   * @param energy   the health it returns to an animal.
   * @param quantity the number of times it returns the energy to an animal before running out.
   * @param position the location on the map, where the food is.
   * @return an implementation of Food.
   */
  def apply(icon: String, energy: Int, quantity: Int, position: (Int, Int)): Food =
    new FoodImpl(icon, energy, quantity, position)

  private class FoodImpl(override val icon: String,
                         override val energy: Int,
                         override val quantity: Int,
                         override val position: (Int, Int)) extends Food
}