package utility

import model.animal.{Carnivore, Medium, Small, Species}
import model.food.{FoodType, Vegetable}
import org.scalatest.funsuite.AnyFunSuite
import utility.serializer.{DefaultSerializer, OfFood, OfSpecies, Serializer}

import java.awt.Color

class SerializeTest extends AnyFunSuite{

  val defaultSerializer: Serializer = Serializer(DefaultSerializer)
  val speciesSerializer: Serializer = Serializer(OfSpecies)
  val foodSerializer: Serializer = Serializer(OfFood)
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

  test("Test serialize and deserialize from file"){
    val fileName = "cars.json"
    val cars = List(car, car2, car3)
    defaultSerializer.serializeManyToFile(cars)(fileName)
    //check that all cars from the first list have the same doors of the deserialized one
    val deserializedCars = defaultSerializer.deserializeManyFromFile(fileName)(classOf[Car])
    assert(deserializedCars.zip(cars).count(t => t._1.doors == t._2.doors) == cars.size)
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

  test("Test custom serializer for Species"){
    val json = speciesSerializer.serializeOne(Species("dog", Small, 100, 10, Carnivore, Color.red))
    assert(json == "{\n  \"color\": {\n    \"value\": -65536,\n    \"falpha\": 0.0\n  },\n  \"name\": \"dog\",\n  \"size\": \"Small\",\n  \"strength\": 100,\n  \"sight\": 10,\n  \"alimentationType\": \"Carnivore\"\n}")
  }

  test("Test custom deserializer for Species"){
    val dog = speciesSerializer.deserializeOne("{\n  \"color\": {\n    \"value\": -65536,\n    \"falpha\": 0.0\n  },\n  \"name\": \"dog\",\n  \"size\": \"Small\",\n  \"strength\": 100,\n  \"sight\": 10,\n  \"alimentationType\": \"Carnivore\"\n}")(classOf[Species])
    assert(dog.size == Small)
  }

  test("Test serialize and deserialize many to file for Species"){
    val fileName = "speciesSerializerTest.json"
    val species = Seq(Species("dog", Small, 100, 10, Carnivore, Color.red), Species("cat", Small, 80, 60, Carnivore, Color.blue), Species("cow", Medium, 40 ,50, Carnivore, Color.green))
    speciesSerializer.serializeManyToFile(species)(fileName)
    val deserializedSpecies = speciesSerializer.deserializeManyFromFile(fileName)(classOf[Species])
    assert(deserializedSpecies.zip(species).count(t => t._1.name == t._2.name) == species.size)
  }

  test("Test serialize and deserialize many to file for Food"){
    val fileName = "foodTypeSerializerTest.json"
    val foodTypes = Seq(FoodType(10, Vegetable, Color.green), FoodType(20, Vegetable, Color.green), FoodType(15, Vegetable, Color.ORANGE))
    foodSerializer.serializeManyToFile(foodTypes)(fileName)
    val deserializedFoods = foodSerializer.deserializeManyFromFile(fileName)(classOf[FoodType])
    assert(deserializedFoods.zip(foodTypes).count(t=>t._1.color==t._2.color && t._1.energy == t._2.energy) == foodTypes.size)
   }
}
