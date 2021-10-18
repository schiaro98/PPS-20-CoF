package controller

import model.{Point, _}
import org.scalatest.funsuite.AnyFunSuite

class DestinationManagerTest extends AnyFunSuite {
  val habitat: Habitat = Habitat(EmptyHabitatType, Probability(1), (100, 100), Seq.empty)
  val herbivore1: Animal = Animal(Species("HerbivoreExample1", Medium, 10, 10, Herbivore), Point(0,0))
  val herbivore2: Animal = Animal(Species("HerbivoreExample2", Medium, 10, 10, Herbivore), Point(3,3))
  val carnivore1: Animal = Animal(Species("CarnivoreExample1", Medium, 10, 10, Carnivore), Point(0,0))
  val carnivore2: Animal = Animal(Species("CarnivoreExample2", Medium, 10, 10, Carnivore), Point(3,3))

  val carrot: Vegetable = Vegetable(1, Point(1,1), 1)
  val beet: Vegetable = Vegetable(1, Point(2,2), 1)

  val meat1: Meat = Meat(1, Point(1,1), 1)
  val meat2: Meat = Meat(1, Point(2,2), 1)

  test("An herbivore should go towards a vegetable, if it's the only choice"){
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
    println(result)
    assert(result.size == 1)
    assert(result.keySet.contains(carnivore1))
    assert(result(carnivore1) == meat1.position)
  }

  test("Carnivore should go towards a herbivore, herbivore should go to random place"){
    val destMng = DestinationManager(Seq(carnivore1, herbivore1), Seq(meat1), habitat)
    val result = destMng.calculateDestination()
    println(result)
    assert(result.size == 2)
    assert(result.keySet.contains(carnivore1))
    assert(result(carnivore1) == herbivore1.position)
  }

  test("Carnivore should go towards a herbivore, herbivore should towards food"){
    val destMng = DestinationManager(Seq(carnivore1, herbivore1), Seq(meat1, carrot), habitat)
    val result = destMng.calculateDestination()
    println(result)
    assert(result.size == 2)
    assert(result.keySet.contains(carnivore1))
    assert(result(carnivore1) == herbivore1.position)
    assert(result(herbivore1) == carrot.position)

  }

}
