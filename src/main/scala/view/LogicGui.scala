package view

import Writer._

class LogicGui {

  var species = Map.empty[String, Int]

  /*
    TODO non so da dove recuperarli
    La specie va create dal menu e salvata su file
   */

  def initialize(): Unit = {
    write() //Scrivo animali di base
    read.foreach(speciesOnFile => { species += (speciesOnFile.name -> 1) }) //e li leggo
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
      case None => println("Animal not found") //TODO maybe exception
    }
  }
}
