package view

import controller.{OfSpecies, Serializer}
import model.{Size, Species}

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
  def initialize(): Unit = {
    val speciesFromFile = serializer.deserializeManyFromFile(speciesFile)(classOf[Species])
    speciesFromFile.foreach(s => {
      species += (s -> 1)
    })
  }

  /**
   * Add a new species if it doesn't exist
   *
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
      case None => println("Animal not found") //TODO maybe exception, in the future
    }
  }

  /**
   * Given the Species parameters, give an istance of Species
   */
  def captionSpecies(name: String, size: String, strength: String, sight: String): Species = {
    Species("icon.txt", name, toSize(size), tryToInt(strength), tryToInt(sight))
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
   * Convert string into Size type
   */
  def toSize(s: String): Size = s match {
    case "Small" => Size.Small
    case "Medium" => Size.Medium
    case "Big" => Size.Big
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

//  /**
//   * Add a new species if it doesn't exist and add it also to species file
//   *
//   * @param s species to be added
//   */
//  def addAndUpdateFile(s: Species): Unit = {
//    add(s)
//    addSpeciesInTheFile(s)
//  }
//
//  /**
//   * Remove a species, also from species file
//   *
//   * @param s species of the animal
//   */
//  def removeAndUpdateFile(s: Species): Unit = {
//    remove(s)
//    removeSpeciesFromFile(s)
//  }
}
