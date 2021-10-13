package model

import utility.{Constants, Point}

sealed trait FoodInstance extends Food with Placeable {
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

case class Meat(override val quantity: Int,
                override val position: Point,
                override val energy: Int = Constants.DefaultEnergyOfMeat,
                override val icon: String = Constants.DefaultIconOfMeat) extends FoodInstance {

  override def consume[F >: FoodInstance](amount: Int): F = {
    if (quantity > amount) Meat(quantity - amount, position, energy, icon) else throw new IllegalArgumentException
  }
}

case class Vegetable(override val quantity: Int,
                     override val position: Point,
                     override val energy: Int,
                     override val icon: String = Constants.DefaultIconOfVegetable) extends FoodInstance {

  override def consume[F >: FoodInstance](amount: Int): F = {
    if (quantity > amount) Vegetable(quantity - amount, position, energy, icon) else throw new IllegalArgumentException
  }
}
