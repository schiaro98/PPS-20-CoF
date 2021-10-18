package utility

import model._
import org.scalatest.funsuite.AnyFunSuite

import java.awt.Color
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import scala.reflect.io.File

class SerializeTest extends AnyFunSuite{

  val defaultSerializer: Serializer = Serializer(DefaultSerializer)
  val speciesSerializer: Serializer = Serializer(OfSpecies)
  case class Car(brand: String, doors:Int)

  val car: Car = Car("Rover", 5)
  val car2: Car = Car("Ferrari", 3)
  val car3: Car = Car("Fiat", 5)

  test("serializeOne"){
    val json = defaultSerializer.serializeOne(car)
    assert(json === "{\"brand\":\"Rover\",\"doors\":5}")
  }

  test("serializeMany"){
    val json = defaultSerializer.serializeMany(List(car, car2, car3))
    assert(json === "{\"brand\":\"Rover\",\"doors\":5}{\"brand\":\"Ferrari\",\"doors\":3}{\"brand\":\"Fiat\",\"doors\":5}")
  }

  test("serializeManyToFile"){
    val fileName = "cars.txt"
    defaultSerializer.serializeManyToFile(List(car, car2, car3))(fileName)
    val path = Path.of("res"+File.separator+"serialization"+File.separator+fileName)
    val json = Files.readString(path, StandardCharsets.UTF_8)
    assert(json === "{\"brand\":\"Rover\",\"doors\":5}{\"brand\":\"Ferrari\",\"doors\":3}{\"brand\":\"Fiat\",\"doors\":5}")
  }

  test("deserializeOne"){
    val deserializedCar = defaultSerializer.deserializeOne("{\"brand\":\"Rover\",\"doors\":5}")(classOf[Car])
    assert(deserializedCar.doors === 5)
  }

  test("deserializeMany"){
    val deserializedCars = defaultSerializer.deserializeMany("{\"brand\":\"Rover\",\"doors\":5}{\"brand\":\"Ferrari\",\"doors\":3}{\"brand\":\"Fiat\",\"doors\":5}")(classOf[Car])
    assert(deserializedCars.length === 3)
    assert(deserializedCars.head.doors === 5)
  }

  test("deserializeManyFromFile"){
    val fileName = "cars.txt"
    defaultSerializer.serializeManyToFile(List(car, car2, car3))(fileName)
    val deserializedCars = defaultSerializer.deserializeManyFromFile(fileName)(classOf[Car])
    assert(deserializedCars.length === 3)
    assert(deserializedCars.head.doors === 5)
  }

  test("Test custom serializer for Species"){
    val json = speciesSerializer.serializeOne(Species("dog", Small, 100, 10, Carnivore, Color.red))
    assert(json == "{\n  \"color\": {\n    \"value\": -65536,\n    \"falpha\": 0.0\n  },\n  \"name\": \"dog\",\n  \"size\": \"Small\",\n  \"strength\": 100,\n  \"sight\": 10,\n  \"alimentationType\": \"Carnivore\"\n}")
  }

  test("Test custom deserializer for Species"){
    val dog = speciesSerializer.deserializeOne("{\n  \"color\": {\n    \"value\": -65536,\n    \"falpha\": 0.0\n  },\n  \"name\": \"dog\",\n  \"size\": \"Small\",\n  \"strength\": 100,\n  \"sight\": 10,\n  \"alimentationType\": \"Carnivore\"\n}")(classOf[Species])
    assert(dog.size == Small)
  }

  test("Test serialize many to file for Species"){
    val fileName = "speciesSerializerTest.txt"
    speciesSerializer.serializeManyToFile(Seq(Species("dog", Small, 100, 10, Carnivore, Color.red), Species("cat", Small, 80, 60, Carnivore, Color.blue), Species("cow", Medium, 40 ,50, Carnivore, Color.green)))(fileName)
    val path = Path.of("res"+File.separator+"serialization"+File.separator+fileName)
    val json = Files.readString(path, StandardCharsets.UTF_8)
    assert(json == "{\n  \"color\": {\n    \"value\": -65536,\n    \"falpha\": 0.0\n  },\n  \"name\": \"dog\",\n  \"size\": \"Small\",\n  \"strength\": 100,\n  \"sight\": 10,\n  \"alimentationType\": \"Carnivore\"\n}{\n  \"color\": {\n    \"value\": -16776961,\n    \"falpha\": 0.0\n  },\n  \"name\": \"cat\",\n  \"size\": \"Small\",\n  \"strength\": 80,\n  \"sight\": 60,\n  \"alimentationType\": \"Carnivore\"\n}{\n  \"color\": {\n    \"value\": -16711936,\n    \"falpha\": 0.0\n  },\n  \"name\": \"cow\",\n  \"size\": \"Medium\",\n  \"strength\": 40,\n  \"sight\": 50,\n  \"alimentationType\": \"Carnivore\"\n}")
  }

  test("Test serialize many from file for Species"){
    val fileName = "speciesSerializerTest.txt"
    val species = speciesSerializer.deserializeManyFromFile(fileName)(classOf[Species])
    assert(species.lengthIs == 3)
  }
}
