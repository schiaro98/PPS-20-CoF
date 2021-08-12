import Serializer.{deserializeMany, deserializeManyFromFile, deserializeOne, serializeMany, serializeManyToFile, serializeOne}
import org.scalatest.funsuite.AnyFunSuite
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

import scala.reflect.io.File

class SerializeTest extends AnyFunSuite{

  case class Car(brand: String, doors:Int)

  val car: Car = Car("Rover", 5)
  val car2: Car = Car("Ferrari", 3)
  val car3: Car = Car("Fiat", 5)

  test("serializeOne"){
    val json = serializeOne(car)
//    println(json)
    assert(json === "{\"brand\":\"Rover\",\"doors\":5}")
  }

  test("serializeMany"){
    val json = serializeMany(List(car, car2, car3))
//    println(json)
    assert(json === "{\"brand\":\"Rover\",\"doors\":5}{\"brand\":\"Ferrari\",\"doors\":3}{\"brand\":\"Fiat\",\"doors\":5}")
  }

  test("serializeManyToFile"){
    val fileName = "cars.txt"
    serializeManyToFile(List(car, car2, car3))(fileName)
    val path = Path.of("res"+File.separator+fileName)
    val json = Files.readString(path, StandardCharsets.UTF_8)
//    println(json)
    assert(json === "{\"brand\":\"Rover\",\"doors\":5}{\"brand\":\"Ferrari\",\"doors\":3}{\"brand\":\"Fiat\",\"doors\":5}")
  }



  test("deserializeOne"){
    val deserializedCar = deserializeOne("{\"brand\":\"Rover\",\"doors\":5}")(classOf[Car])
//    println(deserializedCar)
    assert(deserializedCar.doors === 5)
  }

  test("deserializeMany"){
    val deserializedCars = deserializeMany("{\"brand\":\"Rover\",\"doors\":5}{\"brand\":\"Ferrari\",\"doors\":3}{\"brand\":\"Fiat\",\"doors\":5}")(classOf[Car])
//    println(deserializedCars)
    assert(deserializedCars.length === 3)
    assert(deserializedCars.head.doors === 5)
  }

  test("deserializeManyFromFile"){
    val fileName = "cars.txt"
    serializeManyToFile(List(car, car2, car3))(fileName)
    val deserializedCars = deserializeManyFromFile(fileName)(classOf[Car])
//    println(deserializedCars)
    assert(deserializedCars.length === 3)
    assert(deserializedCars.head.doors === 5)
  }
}
