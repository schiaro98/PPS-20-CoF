package model

import java.awt.image.BufferedImage

/**
 * Trait that represent a food.
 */
trait Food extends Visualizable {
  val energy: Int
  var quantity: Int

  /**
   * Amount of health healed to an animal; the food will be eaten only in the quantity
   * necessary to totally cure the animal
   * @param missingHealth the maximum health that the animal can be healed
   * @return the amount of health to heal
   */
  def feed(missingHealth: Int): Int
}

/**
 * Object that represent a food.
 */
object Food {
  /**
   * Apply method for a Food.
   * @param icon the image to draw in the map.
   * @param energy the health it returns to an animal.
   * @param quantity the number of times it returns health before running out.
   * @return an implementation of Food
   */
  def apply(icon: BufferedImage, energy: Int, quantity: Int): Food = new FoodImpl(icon, energy, quantity)

  private class FoodImpl(override val icon: BufferedImage,
                         override val energy: Int,
                         override var quantity: Int) extends Food {

    override def feed(missingHealth: Int): Int = quantity match {
      case 0 => 0
      case _ if missingHealth / energy < quantity =>
        val temp = quantity
        quantity = 0 // TODO: destroy the food
        energy * temp
      case _ =>
        quantity -= missingHealth / energy
        energy * (missingHealth / energy)
    }
  }
}