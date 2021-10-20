package model

import java.awt.Color

/**
 * Trait that represents an instance of a particular [[Food]] of which it may contain
 * a certain quantity.
 */
sealed trait FoodInstance extends Food with Placeable {

  val quantity: Int

  /**
   * Method to decrease the quantity of the food.
   *
   * @param amount the quantity of food consumed.
   * @return a new food, the same as before but with de quantity decreased.
   * @throws IllegalArgumentException if the amount is greater than the quantity.
   */
  def consume[F >: FoodInstance](amount: Int): F
}

/**
 * Object that represents an instance of a particular [[Food]] of which it may contain
 * a certain quantity.
 */
object FoodInstance {

  /**
   * Apply method for [[FoodInstance]].
   *
   * @param food     the [[Food]] to instantiate.
   * @param position the [[Point]] where the [[Food]] is.
   * @param quantity the quantity of [[Food]].
   * @return an implementation of [[FoodInstance]].
   */
  def apply(food: Food, position: Point, quantity: Int): FoodInstance =
    FoodInstanceImpl(quantity, position, food.energy, food.color, food.foodType)

  private case class FoodInstanceImpl(override val quantity: Int,
                                      override val position: Point,
                                      override val energy: Int,
                                      override val color: Color,
                                      override val foodType: FoodType,
                                     ) extends FoodInstance {

    override def consume[F >: FoodInstance](amount: Int): F =
      if (quantity >= amount) FoodInstanceImpl(quantity - amount, position, energy, color, foodType)
      else throw new IllegalArgumentException("Trying to eat more food than existing")
  }
}
