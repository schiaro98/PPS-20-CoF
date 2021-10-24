package model.food

import model.position.Visualizable
import utility.Constants

import java.awt.Color

/**
 * The possible types of food.
 */
sealed trait FoodCategory
case object Meat extends FoodCategory
case object Vegetable extends FoodCategory

/**
 * Trait that represent a food.
 */
sealed trait FoodType extends Visualizable {
  val energy: Int
  val foodCategory: FoodCategory
}

/**
 * Object that represent the a food.
 */
object FoodType {

  /**
   * Apply method for a [[FoodType]].
   *
   * @param energy       the health it returns to an animal.
   * @param foodCategory the [[FoodCategory]] of this food.
   * @return an implementation of [[FoodType]].
   */
  def apply(energy: Int, foodCategory: FoodCategory): FoodType = foodCategory match {
    case Meat => FoodTypeImpl(Constants.DefaultColorOfMeat, energy, foodCategory)
    case Vegetable => FoodTypeImpl(Constants.DefaultColorOfVegetable, energy, foodCategory)
  }

  /**
   * Apply method for a [[FoodType]].
   *
   * @param energy       the health it returns to an animal.
   * @param foodCategory the [[FoodCategory]] of this food.
   * @param color        the [[Color]] that should be used to draw the element.
   * @return an implementation of [[FoodType]].
   */
  def apply(energy: Int, foodCategory: FoodCategory, color: Color): FoodType = FoodTypeImpl(color, energy, foodCategory)

  private case class FoodTypeImpl(override val color: Color,
                                  override val energy: Int,
                                  override val foodCategory: FoodCategory,
                                 ) extends FoodType
}
