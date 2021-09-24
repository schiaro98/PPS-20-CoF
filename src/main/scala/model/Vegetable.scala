package model

import utility.Constants

case class Vegetable(override val quantity: Int,
                     override val position: (Int, Int),
                     override val energy: Int,
                     override val icon: String = Constants.DefaultIconOfVegetable) extends FoodInstance {

  override def consume[F >: FoodInstance](amount: Int): F = if (quantity > amount) Vegetable(quantity - amount, position, energy, icon) else throw new IllegalArgumentException
}