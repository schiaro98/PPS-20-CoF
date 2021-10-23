package model.animal

import model.food.{Food, FoodType, Meat, Vegetable}
import model.position.{Placeable, Point}
import utility.{Constants, Logger, Statistics}

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
sealed trait Animal extends Placeable {
  val species: Species
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

  /**
   * Method used to obtain a random [[Age]] for an [[Animal]].
   *
   * @return a random [[Age]].
   */
  private def randomAge: Age = Random.shuffle(Set(Young, Adult, Old)).head

  /**
   * Apply method for [[Animal]].
   *
   * @param species  the species of the animal.
   * @param position the location on the map, where the [[Animal]] is.
   * @param health   the parameter that indicates whether the [[Animal]] is healthy.
   * @param thirst   the parameter that indicates whether the [[Animal]] is thirsty.
   * @param age      the [[Age]] of the [[Animal]].
   * @return a new implementation of [[Animal]].
   */
  def apply(species: Species, position: Point, health: Int = Constants.MaxHealth, thirst: Int = Constants.MaxThirst,
            age: Age = randomAge): Animal = AnimalImpl(species, health, thirst, position, age)


  private case class AnimalImpl(override val species: Species,
                                override val health: Int,
                                override val thirst: Int,
                                override val position: Point,
                                override val age: Age) extends Animal {

    override def canSee(element: Placeable): Boolean = this.position.distance(element.position) <= species.sight

    override def update(health: Int, thirst: Int, position: Point, age: Age): Animal =
      Animal(species, position, health, thirst, age)

    override def isAlive: Boolean = health > 0 && thirst > 0

    override def drink(): Animal = {
      Logger.info(species.name + " drunk some water")
      this.update(thirst = Constants.MaxThirst)
    }

    override def die(): Food = Food(FoodType(Constants.DefaultEnergyOfMeat, Meat), position, quantityFromDeath())

    override def shift(pos: Point): Animal = this.update(position = pos)

    override def eat(food: Food): (Animal, Option[Food]) = food.foodType.foodCategory match {
      case Meat if species.alimentationType == Carnivore => consume(food)
      case Vegetable if species.alimentationType == Herbivore => consume(food)
      case _ => throw new IllegalArgumentException("A carnivore trying to eat vegetable or a Herbivore trying to eat Meat")
    }

    override def toString: String = s"Animal: ${species.name}, Size: ${species.size}, Health: $health, Thirst: $thirst, ${species.strength} pos: ${(position.x, position.y)}"

    /**
     * Method to obtain the amount of meat it will produce once it is dead; it is based on the size of the animal.
     *
     * @return the quantity of food.
     */
    private def quantityFromDeath(): Int = species.size match {
      case Big => Constants.QuantityForBig
      case Medium => Constants.QuantityForMedium
      case Small => Constants.QuantityForSmall
    }

    /**
     * Method to restore health to an [[Animal]] by eating food.
     *
     * @param food The food to eat.
     * @return a pair that contains the [[Animal]] with the health increased and the remaining food, if there is still any.
     */
    private def consume(food: Food): (Animal, Option[Food]) = health match {
      case Constants.MaxHealth => (this, Some(food))
      case _ if Constants.MaxHealth - health > food.foodType.energy * food.quantity =>
        Logger.info(species.name + s" eat all the ${food.foodType.foodCategory}")
        Statistics.update(foodEaten = food.quantity)
        (this.update(health = health + food.foodType.energy * food.quantity), None)
      case _ =>
        Logger.info(species.name + s" eat some ${food.foodType.foodCategory}")
        val foodToEat = (Constants.MaxHealth - health) / food.foodType.energy
        Statistics.update(foodEaten = foodToEat)
        (this.update(health = health + food.foodType.energy * foodToEat), Some(food.consume(foodToEat)))
    }
  }
}
