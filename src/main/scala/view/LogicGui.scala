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
  var species = Map.empty[String, Int]

  /**
   * SpeciesSeq is where the different species are stored on runtime
   */
  var speciesSeq = Seq.empty[Species]

  def initialize(): Unit = {
    speciesSeq = serializer.deserializeManyFromFile(speciesFile)(classOf[Species])
    speciesSeq.foreach(speciesOnFile => { species += (speciesOnFile.name -> 1) }) //e li leggo
  }

  /**
  Add a new animal if it doesn't exist
  @param str name of the animal
   */
  def add(str: String): Unit = {
    if(!species.contains(str)) {
      species += str -> 1
    }
  }

  /**
  Increase quantity of an animal
    if it doesn't exist, add it
  @param str name of the animal
   */
  def increase(str: String): Unit = {
    if(species.contains(str)){
      species += str -> species(str).+(1)
    } else {
      add(str)
    }
  }

  /**
  Remove an animal
  @param str name of the animal
   */
  def remove(str: String): Unit = species = species - str

  /**
   * Decrease quantity of an animal, deleting it if it's equal to zero
   * @param str name of the animal
   */
  def decrease(str: String): AnyVal = {
    val value = species get str
    value match {
      case Some(value) if value > 1 => species += str -> species(str).-(1)
      case Some(_) => remove(str)
      case None => println("Animal not found") //TODO maybe exception, in the future
    }
  }

  /**
   * Given the Species parameters, give an istance of Species
   */
  def captionSpecies(name: String, size: String, strength: String, sight: String): Species = {
    Species("icon.txt", name,  toSize(size), tryToInt(strength), tryToInt(sight))
  }

  // TODO: gestire meglio la gestione degli input da utente
  /**
    Convert int to String
   @throws Exception if string is not a number
   */
  def tryToInt(s: String): Int = {
    try {
      s.toInt
    } catch {
      case _: Exception => 0
    }
  }

  /**
  Convert string into Size type
   */
  def toSize(s: String): Size = s match {
    case "Small" => Size.Small
    case "Medium" => Size.Medium
    case "Big" => Size.Big
  }

  /**
   * Add species to species file
   * @param species to be added
   */
  def addSpecies(species: Species): Unit = {
    speciesSeq = speciesSeq :+ species
    serializer.serializeManyToFile(speciesSeq)(speciesFile)
  }

  /**
   * Remove species in species file
   * @param species to be removed
   */
  def removeSpecies(species: Species): Unit = {
    speciesSeq = speciesSeq.filterNot(x => x == species)
    serializer.serializeManyToFile(speciesSeq)(speciesFile)
  }

  /**
   * Remove all species from species file, basicaly it clean the species file
   */
  def removeAllSpecies(): Unit = {
    serializer.serializeManyToFile(Seq.empty[Species])(speciesFile)
  }

  /**
   * Return the species, given the name
   */
  def getSpecies(name: String): Option[Species] ={
    speciesSeq.find(species => species.name == name)
  }

}
