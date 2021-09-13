package model

import utility.Constants._

/**
 * Trait that represent the meat that can be eaten by Carnivores.
 */
trait Meat extends FoodInstance

/**
 * Object that represent the meat that can be eaten by Carnivores.
 */
object Meat {
  /**
   * Apply method for a Meat.
   *
   * @param icon     the image to draw in the map.
   * @param energy   the health it returns to an animal.
   * @param quantity the number of times it returns health before running out.
   * @param position the location on the map, where the food is.
   * @return an implementation of Meat.
   */
  def apply(quantity: Int, position: (Int, Int), energy: Int = EnergyOfMeat, icon: String = IconOfMeat): Meat =
    new MeatImpl(icon, energy, quantity, position)

  private class MeatImpl(override val icon: String,
                         override val energy: Int,
                         override val quantity: Int,
                         override val position: (Int, Int)) extends Meat
}
