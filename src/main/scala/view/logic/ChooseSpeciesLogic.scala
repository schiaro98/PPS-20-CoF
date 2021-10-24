package view.logic

import model.animal._
import utility.Constants
import utility.serializer.{OfSpecies, Serializer}

import java.awt.Color
import java.io.{FileNotFoundException, IOException}

class ChooseSpeciesLogic(speciesFile: String) {

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
  def initialize(): Unit = getAllSpecies.foreach(s => species += (s -> Constants.InitialNumForAnimals))

  def getAllSpecies: Seq[Species] = try {
    serializer.deserializeManyFromFile(speciesFile)(classOf[Species])
  } catch {
    case e: NullPointerException =>
      Seq(Species("Hippopotamus", Big, 30, 10, Carnivore, new Color(-8752012)),
          Species("Hyena", Small, 20, 15, Carnivore, new Color(-3398716)))
    case _ => Seq.empty
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

  def increase(s: String): Unit = {
    val foundSpecies = getAllSpecies.find(species => species.name == s)
    increase(foundSpecies.getOrElse(throw new IllegalArgumentException("Trying to manage a species never seen before")))
  }

  /**
   * Remove a species
   *
   * @param s species of the animal
   */
  def remove(s: Species): Unit = species = species - s

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
  def captionSpecies(color: Color, name: String, size: Size, strength: String, sight: String, alimentationType: Type): Species =
    Species(name, size, strength.toInt, sight.toInt, alimentationType, color)

  /**
   * Add species to species file
   *
   * @param s species to be added
   */
  def addSpeciesInTheFile(s: Species): Unit = {
    try {
      val speciesFromFile = serializer.deserializeManyFromFile(speciesFile)(classOf[Species]) :+ s
      serializer.serializeManyToFile(speciesFromFile)(speciesFile)
    } catch {
      case e: FileNotFoundException => println("Couldn't find that file.")
      case e: IOException => println("Had an IOException trying to read that file")
      case _ => println("error")
    }
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
  def removeAllSpeciesFromFile(): Unit =
    serializer.serializeManyToFile(Seq.empty[Species])(speciesFile)

  /**
   * Return the species, given the name
   */
  def getSpecies(name: String): Option[Species] =
    species.keySet.find(species => species.name == name)

}
