import controller.{DefaultSerializer, OfSpecies, Serializer}
import model.{Size, Species}
import org.scalatest.funsuite.AnyFunSuite

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
    val json = speciesSerializer.serializeOne(Species("dog.txt","dog",Size.Small, 100,10))
    assert( json == "{\n  \"icon\": \"dog.txt\",\n  \"name\": \"dog\",\n  \"size\": \"Small\",\n  \"strength\": 100,\n  \"sight\": 10\n}")
  }

  test("Test custom deserializer for Species"){
    val dog = speciesSerializer.deserializeOne("{\n  \"icon\": \"dog.txt\",\n  \"name\": \"dog\",\n  \"size\": \"Small\",\n  \"strength\": 100,\n  \"sight\": 10\n}")(classOf[Species])
    assert(dog.size == Size.Small)
  }

  test("Test serialize many to file for Species"){
    val fileName = "speciesSerializerTest.txt"
    speciesSerializer.serializeManyToFile(Seq(Species("dog.png","dog",Size.Small, 100,10), Species("cat.png","cat",Size.Small, 80,60), Species("cow.png", "cow", Size.Medium, 40,50)))(fileName)
    val path = Path.of("res"+File.separator+"serialization"+File.separator+fileName)
    val json = Files.readString(path, StandardCharsets.UTF_8)
    assert(json == "{\n  \"icon\": \"dog.png\",\n  \"name\": \"dog\",\n  \"size\": \"Small\",\n  \"strength\": 100,\n  \"sight\": 10\n}{\n  \"icon\": \"cat.png\",\n  \"name\": \"cat\",\n  \"size\": \"Small\",\n  \"strength\": 80,\n  \"sight\": 60\n}{\n  \"icon\": \"cow.png\",\n  \"name\": \"cow\",\n  \"size\": \"Medium\",\n  \"strength\": 40,\n  \"sight\": 50\n}")
  }

  test("Test serialize many from file for Species"){
    val fileName = "speciesSerializerTest.txt"
    val species = speciesSerializer.deserializeManyFromFile(fileName)(classOf[Species])
    assert(species.lengthIs == 3)
  }
}
