package model.animal

import model.food.{Food, FoodType, Meat, Vegetable}
import model.position.{Placeable, Point}
import utility.Constants._
import utility.{Constants, Logger, Statistics}

import java.awt.Color
import scala.util.Random

/**
 * The possible age of an animal
 */
sealed trait Age
case object Young extends Age
case object Adult extends Age
case object Old extends Age

/**
 * Trait that represent an animal of a specific [[Species]].
 */
trait Animal extends Species with Placeable {

  val health: Int
  val thirst: Int
  val age: Age

  /**
   * Method used to modify some parameters of the [[Animal]].
   *
   * @param health   the new health.
   * @param thirst   the new thirst.
   * @param position the new position.
   * @param age      the new [[Age]].
   * @return a new animal, the same as before but with the updated parameters.
   */
  def update(health: Int = health, thirst: Int = thirst, position: Point = position, age: Age = age): Animal

  /**
   * Method used to get the [[Species]] of an [[Animal]].
   *
   * @return the [[Species]] of the [[Animal]].
   */
  def species:Species

  /**
   * Method to check if the [[Animal]] is alive.
   *
   * @return true if the [[Animal]] is alive, false otherwise.
   */
  def isAlive: Boolean

  /**
   * Method used to understand if the [[Animal]] can see a certain element on the map.
   *
   * @param element the element to see.
   * @return true if the [[Animal]] can see the element, false otherwise.
   */
  def canSee(element: Placeable): Boolean

  /**
   * Shift an [[Animal]] position.
   *
   * @param pos the new position of the animal.
   * @return the same [[Animal]] but in the specified position.
   */
  def shift(pos: Point): Animal

  /**
   * Method used to feed the [[Animal]] and increase his health.
   *
   * @param food the food that the [[Animal]] can eat.
   * @return a pair containing the same [[Animal]] with health increased and the remaining food,
   *         if there is, that it hasn't eaten.
   */
  def eat(food: Food): (Animal, Option[Food])

  /**
   * Method to quench an [[Animal]]'s thirst.
   *
   * @return a new [[Animal]], the same as before but with the parameter of thirst to the maximum.
   */
  def drink(): Animal

  /**
   * Method to obtain the meat that can be eaten from the carcass of the [[Animal]] once it is dead.
   *
   * @return a new food, the meat that can be eaten.
   */
  def die(): Food
}

/**
 * Object that represent an animal of a specific species.
 */
object Animal {

  def randomAge: Age = Random.shuffle(Set(Young, Adult, Old)).head

  /**
   * Apply method for [[Animal]].
   *
   * @param s        the species of the animal.
   * @param position the location on the map, where the [[Animal]] is.
   * @param health   the parameter that indicates whether the [[Animal]] is healthy.
   * @param thirst   the parameter that indicates whether the [[Animal]] is thirsty.
   * @param age      the [[Age]] of the [[Animal]].
   * @return a new implementation of [[Animal]].
   */
  def apply(s: Species, position: Point, health: Int = MaxHealth, thirst: Int = MaxThirst,
            age: Age = randomAge): Animal = AnimalImpl(
    s.color, s.name, s.size, s.strength, s.sight, health, thirst, position, s.alimentationType, age)


  private case class AnimalImpl(override val color: Color, override val name: String, override val size: Size,
                                override val strength: Int, override val sight: Int, override val health: Int,
                                override val thirst: Int, override val position: Point,
                                override val alimentationType: Type, override val age: Age) extends Animal {

    private val logger = Logger

    override def canSee(element: Placeable): Boolean = this.position.distance(element.position) <= this.sight

    override def update(health: Int, thirst: Int, position: Point, age: Age): Animal =
      Animal(Species(name, size, strength, sight, alimentationType, color), position, health, thirst, age)

    override def isAlive: Boolean = health > 0 && thirst > 0

    override def drink(): Animal = {
      logger.info(this.name + " drinked water")
      this.update(thirst = MaxThirst)
    }

    override def die(): Food = Food(FoodType(Constants.DefaultEnergyOfMeat, Meat), position, quantityFromDeath())

    override def shift(pos: Point): Animal = this.update(position = pos)

    override def eat(food: Food): (Animal, Option[Food]) = food.foodCategory match {
      case Meat if this.alimentationType == Carnivore => consume(food)
      case Vegetable if this.alimentationType == Herbivore => consume(food)
      case _ => throw new IllegalArgumentException("A carnivore trying to eat vegetable or a Herbivore trying to eat Meat")
    }

    override def toString: String = s"Animal: $name, Size: $size, Health: $health, Thirst: $thirst, $strength pos: ${(position.x, position.y)}"

    override def species: Species = Species(name, size, strength, sight, alimentationType, color)

    /**
     * Method to obtain the amount of meat it will produce once it is dead; it is based on the size of the animal.
     *
     * @return the quantity of food.
     */
    private def quantityFromDeath(): Int = size match {
      case Big => QuantityForBig
      case Medium => QuantityForMedium
      case Small => QuantityForSmall
    }

    /**
     * Method to restore health to an [[Animal]] by eating food.
     *
     * @param food The food to eat.
     * @return a pair that contains the [[Animal]] with the health increased and the remaining food, if there is still any.
     */
    private def consume(food: Food): (Animal, Option[Food]) = health match {
      case Constants.MaxHealth => (this, Some(food))
      case _ if MaxHealth - health > food.energy * food.quantity =>
        logger.info(this.name + s" eat all the ${food.foodCategory}")
        Statistics.update(foodEaten = food.quantity)
        (this.update(health = health + food.energy * food.quantity), None)
      case _ =>
        logger.info(this.name + s" eat some ${food.foodCategory}")
        val foodToEat = (MaxHealth - health) / food.energy
        Statistics.update(foodEaten = foodToEat)
        (this.update(health = health + food.energy * foodToEat), Some(food.consume(foodToEat)))
    }
  }
}
