package model

import utility.{Constants, Point}

case class Meat(override val quantity: Int,
                override val position: Point,
                override val energy: Int = Constants.DefaultEnergyOfMeat,
                override val icon: String = Constants.DefaultIconOfMeat) extends FoodInstance {

  override def consume[F >: FoodInstance](amount: Int): F = if (quantity > amount) Meat(quantity - amount, position, energy, icon) else throw new IllegalArgumentException
}