import Serializer.{deserializeManyFromFile, serializeManyToFile}

sealed trait Writer

object Writer {
  case class Animal(name: String, quantity: Int) //TODO aggiungere altri campi

  val filename = "animals.txt"
  val basicAnimals: List[Animal] = List(Animal("Lion", 1), Animal("Zebra", 2), Animal("Ippo", 3))

  def read: Seq[Animal] = deserializeManyFromFile(filename)(classOf[Animal])

  def write(): Unit = {
    serializeManyToFile(basicAnimals)(filename)
  }
}
