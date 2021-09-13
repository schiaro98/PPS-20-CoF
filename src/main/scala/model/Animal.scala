package model

import model.Size._
import utils.Constants
import utils.Constants._

import java.awt.image.BufferedImage

/**
 * Trait that represent the age of an animal.
 */
trait Age

/**
 * Object that contains the possible age of an animal.
 */
object Age {
  case object Young extends Age

  case object Adult extends Age

  case object Old extends Age
}

/**
 * Trait that represent an animal of a specific species.
 */
trait Animal extends Species with Placeable {
  val health: Int
  val thirst: Int
  val direction: (Int, Int) // (+1,0) va a destra,(0,-1) in basso,(-1,-1) diagonale basso-sinistra, ecc

  /**
   * Method to check if the animal is alive.
   *
   * @return a boolean, true if the animal is alive, false otherwise.
   */
  def isAlive: Boolean = health > 0 && thirst > 0

  /**
   * Method that map an animal to a new animal to prepare the next iteration of the simulation.
   *
   * @param health    the new health updated after any clashes.
   * @param thirst    the new thirst.
   * @param position  the new position after a possible move.
   * @param direction the new direction.
   * @return a new animal, the same as before but with the updated parameters for the next iteration of the simulation.
   */
  def update(health: Int = health,
             thirst: Int = thirst,
             position: (Int, Int) = position,
             direction: (Int, Int) = direction): Animal =
    Animal(Species(icon, name, size, strength, sight), position, direction, health, thirst)

  /**
   * Method to restore health to an animal by eating food.
   *
   * @param food The food to eat.
   * @return a pair that contains the animal with the health restored and the remaining food, if there is still any.
   */
  def eat(food: Food): (Animal, Option[Food]) = health match {
    //todo se il controllo viene fatto altrove (ad es. mangi solo se hai meno di 300 di fame) non serve
    case Constants.maxHealth => (this, Some(food))
    case _ if maxHealth - this.health > food.energy * food.quantity => (this.update(health = health + food.energy * food.quantity), None)
    case _ =>
      val foodToEat = (maxHealth - health) / food.energy + (if (maxHealth - health % food.energy == 0) 0 else 1)
      (this.update(health = maxHealth), Some(food.consume(foodToEat)))
  }

  /**
   * Method to quench an animal's thirst.
   *
   * @return a new animal, the same as before but with the parameter of thirst to the maximum.
   */
  def drink(): Animal = this.update(thirst = maxThirst)

  def quantityFromDeath(): Int = size match {
    case Big => quantityForBig
    case Medium => quantityForMedium
    case Small => quantityForSmall
  }

  /**
   * Method to obtain the meat that can be eaten from the carcass of the animal once it is dead.
   *
   * @return a new food, the meat that can be eaten
   */
  def die(): Meat = Meat(quantityFromDeath(), position)
}

/**
 * Object that represent an animal of a specific species.
 */
object Animal { //todo va bene private per non far creare un animal?

  /**
   * Apply method for an Animal.
   *
   * @param s         the species of the animal.
   * @param position  the location on the map, where the animal is.
   * @param direction the direction in which the animal is moving.
   * @param health    the parameter that indicates whether the animal is healthy.
   * @param thirst    the parameter that indicates whether the animal is thirsty.
   * @return a new implementation of Animal.
   */
  private def apply(s: Species, position: (Int, Int), direction: (Int, Int), health: Int = maxHealth, thirst: Int = maxThirst): Animal =
    new AnimalImpl(s.icon, s.name, s.size, s.strength, s.sight, health, thirst, position, direction)

  //todo aggiungere erbivoro/carnivoro come campo in specie e differenziare i due nell'apply di animal

  private class AnimalImpl(override val icon: BufferedImage,
                           override val name: String,
                           override val size: Size,
                           override val strength: Int,
                           override val sight: Int,
                           override val health: Int,
                           override val thirst: Int,
                           override val position: (Int, Int),
                           override val direction: (Int, Int)) extends Animal
}
