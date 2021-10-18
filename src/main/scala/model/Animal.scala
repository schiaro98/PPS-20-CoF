package model

import utility.{Constants, Logger, Point}
import utility.Constants._

import java.awt.Color

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

  /**
   * Method used to understand if the animal can see a certain element of the map.
   *
   * @param element the element to see.
   * @return true if the animal can see the element, false otherwise.
   */
  def canSee(element: Placeable): Boolean

  /**
   * Method to check if the animal is alive.
   *
   * @return a boolean, true if the animal is alive, false otherwise.
   */
  def isAlive: Boolean

  /**
   * Method that map an animal to a new animal to prepare the next iteration of the simulation.
   *
   * @param health   the new health updated after any clashes.
   * @param thirst   the new thirst.
   * @param position the new position after a possible move.
   * @return a new animal, the same as before but with the updated parameters for the next iteration of the simulation.
   */
  def update(health: Int = health, thirst: Int = thirst, position: Point = position): Animal

  /**
   * Method to quench an animal's thirst.
   *
   * @return a new animal, the same as before but with the parameter of thirst to the maximum.
   */
  def drink(): Animal

  /**
   * Method to obtain the meat that can be eaten from the carcass of the animal once it is dead.
   *
   * @return a new food, the meat that can be eaten.
   */
  def die(): Meat

  /**
   * Shift an animal position.
   *
   * @param pos the new position of the animal.
   * @return new Animal with the specified position.
   */
  def shift(pos: Point): Animal

  def eat(food: FoodInstance): (Animal, Option[FoodInstance])
}

/**
 * Object that represent an animal of a specific species.
 */
object Animal {

  /**
   * Apply method for [[Animal]].
   *
   * @param s        the species of the animal.
   * @param position the location on the map, where the animal is.
   * @param health   the parameter that indicates whether the animal is healthy.
   * @param thirst   the parameter that indicates whether the animal is thirsty.
   * @return a new implementation of Animal.
   */
  def apply(s: Species, position: Point, health: Int = MaxHealth, thirst: Int = MaxThirst): Animal = AnimalImpl(
    s.color, s.name, s.size, s.strength, s.sight, health, thirst, position, s.alimentationType)


  private case class AnimalImpl(override val color: Color, override val name: String, override val size: Size,
                                override val strength: Int, override val sight: Int, override val health: Int,
                                override val thirst: Int, override val position: Point,
                                override val alimentationType: Type) extends Animal {

    private val logger = Logger
    override def canSee(element: Placeable): Boolean = this.position.distance(element.position) <= this.sight

    override def update(health: Int, thirst: Int, position: Point): Animal =
      Animal(Species(name, size, strength, sight, alimentationType, color), position, health, thirst)

    override def isAlive: Boolean = health > 0 && thirst > 0

    override def drink(): Animal = this.update(thirst = MaxThirst)

    override def die(): Meat = Meat(quantityFromDeath(), position)

    override def shift(pos: Point): Animal = this.update(position = pos)

    override def eat(food: FoodInstance): (Animal, Option[FoodInstance]) = food match {
      case _: Meat if this.alimentationType == Carnivore => consume(food)
      case _: Vegetable if this.alimentationType == Herbivore => consume(food)
      case _ => throw new IllegalArgumentException("Illegal food type")
    }

    /**
     * Method to restore health to an animal by eating food.
     *
     * @param food The food to eat.
     * @return a pair that contains the animal with the health restored and the remaining food, if there is still any.
     */
    def consume(food: FoodInstance): (Animal, Option[FoodInstance]) = health match {
      case Constants.MaxHealth => (this, Some(food))
      case _ if MaxHealth - this.health > food.energy * food.quantity =>
        logger.info(this.name + "eat some food")
        (this.update(health = health + food.energy * food.quantity), None)
      case _ =>
        logger.info(this.name + "eat some food")
        val foodToEat = (MaxHealth - health) / food.energy + (if (MaxHealth - health % food.energy == 0) 0 else 1)
        (this.update(health = MaxHealth), Some(food.consume(foodToEat)))
    }

    /**
     * Method to obtain the amount of meat it will produce once it is dead; it is based on the size of the animal.
     *
     * @return the quantity of food.
     */
    def quantityFromDeath(): Int = size match {
      case Big => QuantityForBig
      case Medium => QuantityForMedium
      case Small => QuantityForSmall
    }

    override def toString: String = s"Animal: $name, $size $strength pos:($position)"
  }
}
