package controller

import model.{Point, _}
import org.scalatest.funsuite.AnyFunSuite

import java.awt.Color

class DestinationManagerTest extends AnyFunSuite {
  val habitat: Habitat = Habitat(EmptyHabitatType, Probability(1), (100, 100), Seq.empty)
  val herbivore1: Animal = Animal(Species("HerbivoreExample1", Medium, 10, 10, Herbivore), Point(0,0))
  val herbivore2: Animal = Animal(Species("HerbivoreExample2", Medium, 10, 10, Herbivore), Point(0,10))
  val carnivore1: Animal = Animal(Species("CarnivoreExample1", Medium, 10, 10, Carnivore), Point(0,20))
  val carnivore2: Animal = Animal(Species("CarnivoreExample2", Medium, 10, 10, Carnivore), Point(0,30))

  val allAnimals = Seq(herbivore1, herbivore2, carnivore1, carnivore2)

  val carrotFood: Food = Food(5, Vegetable, Color.ORANGE)
  val beetFood: Food = Food(10, Vegetable, Color.PINK)
  val beefMeat: Food = Food(10, Meat, Color.red)
  val crocodileMeat: Food = Food(10, Meat, Color.green)

  val carrot: FoodInstance = FoodInstance(carrotFood, Point(1,0), 1)
  val beet: FoodInstance = FoodInstance(beetFood, Point(1,10), 1)

  val meat1: FoodInstance = FoodInstance(beefMeat, Point(1,20), 1)
  val meat2: FoodInstance = FoodInstance(crocodileMeat, Point(1,30), 1)
  val allFoods = Seq(carrot, beet, meat2, meat1)

  test("An herbivore should go towards a vegetable"){
    val destMng = DestinationManager(Seq(herbivore1), Seq(carrot), habitat)
    val result = destMng.calculateDestination()
    assert(result.size == 1)
    assert(result.keySet.contains(herbivore1))
    assert(result(herbivore1) == carrot.position)
  }

  test("An herbivore should go towards the next vegetable"){
    val destMng = DestinationManager(Seq(herbivore1), Seq(carrot, beet), habitat)
    val result = destMng.calculateDestination()
    assert(result.size == 1)
    assert(result.keySet.contains(herbivore1))
    assert(result(herbivore1) == carrot.position)
  }

  test("Different Herbivore should go towards the next vegetable"){
    val destMng = DestinationManager(Seq(herbivore1,herbivore2), Seq(carrot, beet), habitat)
    val result = destMng.calculateDestination()
    assert(result.size == 2)
    assert(result.keySet.toSeq == Seq(herbivore1, herbivore2))
    assert(result(herbivore1) == carrot.position)
    assert(result(herbivore2) == beet.position)
  }

  test("An herbivore should be able to choose between two veg at the same distance"){
    val destMng = DestinationManager(Seq(herbivore1), Seq(carrot, carrot), habitat)
    val result = destMng.calculateDestination()
    assert(result.size == 1)
    assert(result.keySet.contains(herbivore1))
    assert(result(herbivore1) == carrot.position)
  }

  test("Carnivore should go towards a Meat piece"){
    val destMng = DestinationManager(Seq(carnivore1), Seq(meat1), habitat)
    val result = destMng.calculateDestination()
    assert(result.size == 1)
    assert(result.keySet.contains(carnivore1))
    assert(result(carnivore1) == meat1.position)
  }

  test("Carnivore should go towards a herbivore, herbivore should go to random place"){
    val herbivore1: Animal = Animal(Species("HerbivoreExample2", Medium, 10, 10, Herbivore), Point(0,1))
    val carnivore1: Animal = Animal(Species("CarnivoreExample1", Medium, 10, 10, Carnivore), Point(0,2))
    val meat1: FoodInstance = FoodInstance(beefMeat, Point(5, 5), 1)

    val destMng = DestinationManager(Seq(carnivore1, herbivore1), Seq(meat1), habitat)
    val result = destMng.calculateDestination()
    assert(result.size == 2)
    assert(result.keySet.contains(carnivore1))
    assert(result(carnivore1) == herbivore1.position)
  }

  test("Carnivore should go towards a herbivore, herbivore should towards food"){
    val herbivore1: Animal = Animal(Species("HerbivoreExample2", Medium, 10, 10, Herbivore), Point(0,1))
    val carnivore1: Animal = Animal(Species("CarnivoreExample1", Medium, 10, 10, Carnivore), Point(0,2))
    val meat1: FoodInstance = FoodInstance(beefMeat, Point(5, 5), 1)
    val carrot: FoodInstance = FoodInstance(carrotFood, Point(1,0), 1)

    val destMng = DestinationManager(Seq(carnivore1, herbivore1), Seq(meat1, carrot), habitat)
    val result = destMng.calculateDestination()
    assert(result.size == 2)
    assert(result.keySet.contains(carnivore1))
    assert(result(carnivore1) == herbivore1.position)
    assert(result(herbivore1) == carrot.position)
  }

  test("Complete test of a dest manager"){
    val habitat = Habitat(Probability(1), (1000, 1000))

    val destMng = DestinationManager(allAnimals, allFoods, habitat)
    val result = destMng.calculateDestination()

    assert(result.contains(herbivore1))
    assert(result(herbivore1) == carrot.position)
    assert(result.contains(herbivore2))
    assert(result(herbivore2) == beet.position)
    assert(result.contains(carnivore1))
    assert(result(carnivore1) == meat1.position)
    assert(result.contains(carnivore2))
    assert(result(carnivore2) == meat2.position)
  }


  test("Test the drinking of animals"){
    val carnivore2: Animal = Animal(Species("CarnivoreExample2", Medium, 10, 10, Carnivore), Point(0,195))
    val habitat = Habitat(Probability(1), (1000, 1000))
    val destMng = DestinationManager(Seq(carnivore2), Seq.empty, habitat)
    destMng.calculateDestination()

    assert(habitat.areas.map(area => area.area.topLeft).contains(Point(400,0)))
    assert(habitat.areas.count(_.areaType == Water) == 6)
  }
}
