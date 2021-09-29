package model

import utility.{Constants, Point}

case class Vegetable(override val quantity: Int,
                     override val position: Point,
                     override val energy: Int,
                     override val icon: String = Constants.DefaultIconOfVegetable) extends FoodInstance {

  override def consume[F >: FoodInstance](amount: Int): F = if (quantity > amount) Vegetable(quantity - amount, position, energy, icon) else throw new IllegalArgumentException
}