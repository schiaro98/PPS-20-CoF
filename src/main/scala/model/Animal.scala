package model

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
trait Animal extends Species {
  val health: Int //todo considerare il massimo della vita/sete 100 e se si arriva a zero si muore?
  val thirst: Int
  val position: (Int, Int) //todo fare una interfaccia con position implementata dagli elementi da disegnare?
  val direction: (Int, Int) //todo (+1,0),(0,-1),(-1,-1) ecc

  /**
   * Method to check if the animal is alive.
   *
   * @return a boolean, true if the animal is alive, false otherwise.
   */
  def isAlive: Boolean = health > 0 && thirst > 0

  /**
   * Method that map an animal to a new animal for the next iteration of the simulation.
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
  //todo l'indentazione va bene?

  /**
   * Method to restore health to an animal by eating food.
   *
   * @param food The food to eat.
   * @return a pair that contains the animal with the health restored and the remaining food, if there is still any
   */
  def eat(food: Food): (Animal, Option[Food]) = health match {
    //todo se il controllo viene fatto altrove (ad es. mangi solo se non hai 100 di fame) non serve
    case 100 => (this, Some(food))
    case _ if 100 - this.health > food.energy * food.quantity => (this.update(health = health + food.energy * food.quantity), None)
    case _ =>
      val eatable = (100 - health) / food.energy + (if (100 - health % food.energy == 0) 0 else 1)
      (this.update(health = 100), Some(food.consume(eatable)))
  }

  /**
   * Method to quench an animal's thirst
   *
   * @return a new animal, the same as before but with the parameter of thirst to the maximum
   */
  def drink(): Animal = this.update(thirst = 100)

  //todo classe apposita per il combattimento?
}

//todo si può togliere l'object animal per non farlo istanziare
/**
 * Object that represent an animal of a specific species.
 */
private object Animal { //todo va bene private per non far creare un animal?

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
  def apply(s: Species, position: (Int, Int), direction: (Int, Int), health: Int = 100, thirst: Int = 100): Animal =
    new AnimalImpl(s.icon, s.name, s.size, s.strength, s.sight, health, thirst, position, direction)

  private class AnimalImpl(override val icon: String,
                           override val name: String,
                           override val size: Size,
                           override val strength: Int,
                           override val sight: Int,
                           override val health: Int,
                           override val thirst: Int,
                           override val position: (Int, Int),
                           override val direction: (Int, Int)) extends Animal
}
