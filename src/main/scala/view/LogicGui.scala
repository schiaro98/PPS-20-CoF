package view

import controller.{OfSpecies, Serializer}
import model.{Size, Species}

class LogicGui(speciesFile: String) {

  val serializer: Serializer = Serializer(OfSpecies)
  var species = Map.empty[String, Int]
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

  def captionSpecies(name: String, size: String, strength: String, sight: String): Species = {
    Species("icon.txt", name,  toSize(size), tryToInt(strength), tryToInt(sight))
  }

  def tryToInt(s: String): Int = {
    try {
      s.toInt
    } catch {
      case _: Exception => 0
    }
  }

  def toSize(s: String): Size = s match {
    case "Small" => Size.Small
    case "Medium" => Size.Medium
    case "Big" => Size.Big
  }

  def addSpecies(species: Species): Unit = {
    speciesSeq = speciesSeq :+ species
    serializer.serializeManyToFile(speciesSeq)(speciesFile)
  }

  def removeSpecies(species: Species): Unit = {
    speciesSeq = speciesSeq.filterNot(x => x == species)
    serializer.serializeManyToFile(speciesSeq)(speciesFile)
  }

  def removeAllSpecies(): Unit = {
    serializer.serializeManyToFile(Seq.empty[Species])(speciesFile)
  }
}
