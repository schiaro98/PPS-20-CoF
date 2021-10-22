package model.food

import model.position.{Placeable, Point}

/**
 * Trait that represents an instance of a particular [[FoodType]] of which it may contain
 * a certain quantity.
 */
sealed trait Food extends Placeable {

  val foodType: FoodType
  val quantity: Int

  /**
   * Method to decrease the quantity of the food.
   *
   * @param amount the quantity of food consumed.
   * @return a new food, the same as before but with de quantity decreased.
   * @throws IllegalArgumentException if the amount is greater than the quantity.
   */
  def consume[F >: Food](amount: Int): F
}

/**
 * Object that represents an instance of a particular [[FoodType]] of which it may contain
 * a certain quantity.
 */
object Food {

  /**
   * Apply method for [[Food]].
   *
   * @param foodType the [[FoodType]] to instantiate.
   * @param position the [[Point]] where the [[FoodType]] is.
   * @param quantity the quantity of [[FoodType]].
   * @return an implementation of [[Food]].
   */
  def apply(foodType: FoodType, position: Point, quantity: Int): Food =
    FoodImpl(quantity, position, foodType)

  private case class FoodImpl(override val quantity: Int,
                              override val position: Point,
                              override val foodType: FoodType
                             ) extends Food {

    override def consume[F >: Food](amount: Int): F =
      if (quantity >= amount) FoodImpl(quantity - amount, position, foodType)
      else throw new IllegalArgumentException("Trying to eat more food than existing")
  }
}
