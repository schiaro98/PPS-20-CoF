package model

/**
 * Trait that represent a vegetable.
 */
trait Vegetable extends Food

/**
 * Object that represent a food.
 */
object Vegetable {
  /**
   * Apply method for a Vegetable.
   *
   * @param icon     the image to draw in the map.
   * @param energy   the health it returns to an animal.
   * @param quantity the number of times it returns health before running out.
   * @param position the location on the map, where the food is.
   * @return an implementation of Vegetable
   */
  def apply(icon: String, energy: Int, quantity: Int, position: (Int, Int)): Vegetable =
    new VegetableImpl(icon, energy, quantity, position)

  private class VegetableImpl(override val icon: String,
                              override val energy: Int,
                              override val quantity: Int,
                              override val position: (Int, Int)) extends Vegetable
}