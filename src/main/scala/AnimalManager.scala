class AnimalManager {
  import Writer._

  var animals = Map.empty[String, Int]

  /*
    TODO non so da dove recuperarli
   */
  var species = Seq("Lion", "Ippo", "Zebra", "Horse", "Ryno")

  def initialize(): Unit = {
    write() //Scrivo animali di base
    read.foreach(animal => { animals += (animal.name -> animal.quantity) }) //e li leggo
  }

  /**
    Add a new animal if it doesn't exist
   @param str name of the animal
   */
  def add(str: String): Unit = {
    if(!animals.contains(str)) {
      animals += str -> 1
    }
  }

  /**
    Increase quantity of an animal
    if it doesn't exist, add it
    @param str name of the animal
   */
  def increase(str: String): Unit = {
    if(animals.contains(str)){
      animals += str -> animals(str).+(1)
    } else {
      add(str)
    }
  }

  /**
    Remove an animal
   @param str name of the animal
   */
  def remove(str: String): Unit = animals = animals - str

  /**
   * Decrease quantity of an animal, deleting it if it's equal to zero
   * @param str name of the animal
   */
  def decrease(str: String): AnyVal = {
    val value = animals get str
    value match {
      case Some(value) if value > 1 => animals += str -> animals(str).-(1)
      case Some(_) => remove(str)
      case None => println("Animal not found") //TODO maybe exception
    }
  }


}
