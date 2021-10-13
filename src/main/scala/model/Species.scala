package model

/**
 * Object that contains the possible size of a species.
 */
sealed trait Size
  case object Big extends Size
  case object Medium extends Size
  case object Small extends Size

sealed trait Type
  case object Herbivore extends Type
  case object Carnivore extends Type

/**
 * Trait that represent a specific species of animal.
 */
trait Species extends Visualizable {
  val name: String
  val size: Size
  val strength: Int
  val sight: Int
  val alimentationType: Type
  //todo inserire la velocità?
}

/**
 * Object that represent a specific species of animal.
 */
object Species {
  /**
   * Apply method for a Species.
   *
   * @param icon     the image to draw in the map.
   * @param name     the name of the species.
   * @param size     the size of the species.
   * @param strength the value that determines who wins in a fight between two animals.
   * @param sight    the visual range of the species.
   * @return an implementation of Species.
   */
  def apply(icon: String, name: String, size: Size, strength: Int, sight: Int, alimentationType: Type): Species =
    new SpeciesImpl(icon, name, size, strength, sight, alimentationType)

  private class SpeciesImpl(override val icon: String,
                            override val name: String,
                            override val size: Size,
                            override val strength: Int,
                            override val sight: Int,
                            override val alimentationType: Type
                           ) extends Species

}