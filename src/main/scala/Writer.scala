import Serializer.{deserializeManyFromFile, serializeManyToFile}

sealed trait Writer

object Writer {
  case class Animal(name: String, var quantity: Int) //TODO aggiungere altri campi, prendendo la classe animal

  val filename = "animals.txt"

  val basicAnimals: Seq[Animal] = Seq(Animal("Lion",3),Animal("Zebra",2), Animal("Ippo", 1))

  def read: Seq[Animal] = deserializeManyFromFile(filename)(classOf[Animal])

  def write(): Unit = {
    serializeManyToFile(basicAnimals)(filename)
  }
}
