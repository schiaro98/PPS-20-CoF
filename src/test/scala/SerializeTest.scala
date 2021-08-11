import Serializer.{deserializeOne, serializeMany, serializeManyOnFile, serializeOne}
import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source
import scala.reflect.io.File

class SerializeTest extends AnyFunSuite{
  case class Car(brand: String, doors:Int)

  test("serializeOne"){
    val car = Car("Rover", 5)
    assert(serializeOne(car)==="{\"brand\":\"Rover\",\"doors\":5}")
  }

  test("serializeMany"){
    val car = Car("Rover", 5)
    val car2 = Car("Ferrari", 3)
    val car3 = Car("Fiat", 5)

    val json = serializeMany(List(car, car2, car3))
//    println(json)
    assert(json === "{\"brand\":\"Rover\",\"doors\":5}{\"brand\":\"Ferrari\",\"doors\":3}{\"brand\":\"Fiat\",\"doors\":5}")
  }

  test("serializeManyOnFile"){
    val car = Car("Rover", 5)
    val car2 = Car("Ferrari", 3)
    val car3 = Car("Fiat", 5)
    val fileName = "cars.txt"

    serializeManyOnFile(List(car, car2, car3))(fileName)
    val fileContents = Source.fromFile("res"+File.separator+fileName).getLines.mkString
    //    println(fileContents)
    assert(fileContents === "{\"brand\":\"Rover\",\"doors\":5}{\"brand\":\"Ferrari\",\"doors\":3}{\"brand\":\"Fiat\",\"doors\":5}")
  }

  test("deserializeOne"){
    val deserializedCar = deserializeOne("{\"brand\":\"Rover\",\"doors\":5}")(classOf[Car])
    assert(deserializedCar.doors === 5)
  }


}
