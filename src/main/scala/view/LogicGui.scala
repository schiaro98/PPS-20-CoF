package view

import controller.OfSpecies
import model.{Size, Species, Type}
import utility.{OfSpecies, Serializer}

class LogicGui(speciesFile: String) {

  /**
   * Serializer object is used to write or retrieve species saved in species file
   */
  val serializer: Serializer = Serializer(OfSpecies)

  /**
   * Species is used to store the number of animal for every species
   */
  var species = Map.empty[Species, Int]

  /**
   * Save the Species present in the file in a variable
   */
  def initialize(): Unit = getAllSpecies.foreach(s => species += (s -> 1))

  def getAllSpecies: Seq[Species] = serializer.deserializeManyFromFile(speciesFile)(classOf[Species])

  /**
   * Add a new species if it doesn't exist
   * @param s species of the animal
   */
  def add(s: Species): Unit = {
    if (!species.contains(s)) {
      species += s -> 1
    }
  }

  /**
   * Increase the number of animal of a species; if it doesn't exist, it adds it
   *
   * @param s species of the animal
   */
  def increase(s: Species): Unit = {
    if (species.contains(s)) {
      species += s -> species(s).+(1)
    } else {
      add(s)
    }
  }

  def increase(s: String): Unit = {
    val foundSpecies = getAllSpecies.find(species => species.name == s)
    increase(foundSpecies.getOrElse(throw new IllegalArgumentException("Trying to manage a species never seen before")))
  }

  /**
   * Remove a species
   *
   * @param s species of the animal
   */
  def remove(s: Species): Unit = {
    species = species - s
  }

  /**
   * Decrease quantity of an animal, deleting it if it's equal to zero
   *
   * @param s species of the animal
   */
  def decrease(s: Species): AnyVal = {
    val value = species get s
    value match {
      case Some(value) if value > 1 => species += s -> species(s).-(1)
      case Some(_) => remove(s)
      case None => throw new IllegalArgumentException("Animal not found")
    }
  }

  /**
   * Given the Species parameters, give an istance of Species
   */
  def captionSpecies(name: String, size: Size, strength: String, sight: String, alimentationType: Type): Species = {
    Species("icon.txt", name, size, tryToInt(strength), tryToInt(sight), alimentationType)
  }

  // TODO: gestire meglio la gestione degli input da utente

  /**
   * Convert int to String
   *
   * @throws Exception if string is not a number
   */
  def tryToInt(s: String): Int = {
    try {
      s.toInt
    } catch {
      case _: Exception => 0
    }
  }

  /**
   * Add species to species file
   *
   * @param s species to be added
   */
  def addSpeciesInTheFile(s: Species): Unit = {
    val speciesFromFile = serializer.deserializeManyFromFile(speciesFile)(classOf[Species]) :+ s
    serializer.serializeManyToFile(speciesFromFile)(speciesFile)
  }

  /**
   * Remove species in species file
   *
   * @param s species to be removed
   */
  def removeSpeciesFromFile(s: Species): Unit = {
    val speciesSeq = species.keySet.filterNot(x => x == s)
    serializer.serializeManyToFile(speciesSeq)(speciesFile)
  }

  /**
   * Remove all species from species file, basically it clean the species file
   */
  def removeAllSpeciesFromFile(): Unit = {
    serializer.serializeManyToFile(Seq.empty[Species])(speciesFile)
  }

  /**
   * Return the species, given the name
   */
  def getSpecies(name: String): Option[Species] = {
    species.keySet.find(species => species.name == name)
  }

}
