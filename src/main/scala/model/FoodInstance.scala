package model

import utility.{Constants, Point}

import java.awt.Color

//TODO scegliere insieme come gestire Vegetable e Meat (a livello logico è più Food che FoodInstance)

/**
 * Trait that represents an instance of a particular [[Food]] of which it may contain
 * a certain quantity
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

// TODO: Mi sembra che si ripeta del codice

//TODO scaladoc
case class Meat(override val quantity: Int,
                override val position: Point,
                override val energy: Int = Constants.DefaultEnergyOfMeat,
                override val color: Color = Constants.DefaultColorOfMeat,
                override val foodType: FoodType = MeatType,
               ) extends FoodInstance {

  override def consume[F >: FoodInstance](amount: Int): F = {
    if (quantity > amount) Meat(quantity - amount, position, energy, color) else throw new IllegalArgumentException
  }
}

//TODO scaladoc
case class Vegetable(override val quantity: Int,
                     override val position: Point,
                     override val energy: Int,
                     override val color: Color = Constants.DefaultColorOfVegetable,
                     override val foodType: FoodType = VegetableType,
                    ) extends FoodInstance {

  override def consume[F >: FoodInstance](amount: Int): F = {
    if (quantity > amount) Vegetable(quantity - amount, position, energy, color) else throw new IllegalArgumentException
  }
}
