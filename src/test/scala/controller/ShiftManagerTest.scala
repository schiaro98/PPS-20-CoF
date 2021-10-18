package controller

import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.{Point, RectangleArea}

import scala.util.Random

class ShiftManagerTest extends AnyFunSuite {
  val ourHabitat: Habitat = Habitat(Probability(0))

  val MaxX = 500
  val MaxY = 500

  val tiger: Animal = Animal(Species("tiger", Medium, 10, 10, Herbivore), getLegalRandomPoint(ourHabitat))
  val elephant: Animal = Animal(Species( "elephant", Medium, 10, 10, Herbivore), getLegalRandomPoint(ourHabitat))
  val dog: Animal = Animal(Species("dog", Medium, 20, 20, Carnivore ), getLegalRandomPoint(ourHabitat))
  val cat: Animal = Animal(Species( "cat", Small, 15, 10, Carnivore), getLegalRandomPoint(ourHabitat))
  val dest1: Point = Point.getRandomPoint(Point(MaxX,MaxY))
  val dest2: Point = Point.getRandomPoint(Point(MaxX,MaxY))
  val dest3: Point = Point.getRandomPoint(Point(MaxX,MaxY))
  val dest4: Point = Point.getRandomPoint(Point(MaxX,MaxY))


  test("Create ShiftManager") {
    val sm: ShiftManager = ShiftManager(ourHabitat, Map(tiger -> Point(55, 100)))
    assert(sm.animals.size == 1)
  }

  test("Single animal arrives at destination in empty habitat") {
    val dest = Point(55, 100)
    val habitat = Habitat(EmptyHabitatType, Probability(0), (500, 500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat, Map(tiger -> dest))
    while (sm.animals.count(a => a.position == dest) != 1){
      sm.walk()
    }
    assert(sm.animals.count(a => a.position == dest) == 1)
  }

  test("Multiple animals arrive at different random destinations in empty habitat") {
    val r = (x: Int) => Random.nextInt(x)
    val dest1 = Point(r(MaxX), r(MaxY))
    val dest2 = Point(r(MaxX), r(MaxY))
    val dest3 = Point(r(MaxX), r(MaxY))
    val dest4 = Point(r(MaxX), r(MaxY))
    val destinations = Set(dest1, dest2, dest3, dest4)
    val habitat = Habitat(EmptyHabitatType, Probability(0), (MaxX, MaxY), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat,
      Map(tiger -> dest1,
        elephant -> dest2,
        dog -> dest3,
        cat -> dest4
      ))
    while(sm.animals.count(a => destinations.contains(a.position)) != sm.animals.size) {
      sm.walk()
    }
    assert(sm.animals.count(a => destinations.contains(a.position)) == sm.animals.size)
  }

  test("Multiple animals arrive at the same random destination in empty habitat") {
    val r = (x: Int) => Random.nextInt(x)
    val dest = Point(r(500), r(500))
    val habitat = Habitat(EmptyHabitatType, Probability(0), (500, 500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat, (tiger, dest),(elephant, dest), (dog,dest), (cat,dest))
    while(sm.animals.count(a => a.position == dest) != sm.animals.size) {
      sm.walk()
    }
    assert(sm.animals.count(a => a.position == dest) == sm.animals.size)
  }

  test("One animal may arrive to random destination in ours habitat") {
    val d = getLegalRandomPoint(ourHabitat)
    val sm: ShiftManager = ShiftManager(ourHabitat,(tiger, d))
    for (_ <- 0 to 100) {
      sm.walk()
      sm.animals.foreach(animal => require(ourHabitat.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(animal.position)) == 0))
    }
    sm.animals.foreach(animal => assert(ourHabitat.areas.filterNot(_.areaType == Fertile).count(a => a.contains(animal.position)) == 0))
  }

  test("Multiple animals may arrive at different random destinations in ours habitat") {
    val sm: ShiftManager = ShiftManager(ourHabitat,(tiger, dest1), (elephant, dest2), (dog, dest3), (cat, dest4))
    for (_ <- 0 to 100) {
      sm.walk()
      sm.animals.foreach(animal => require(ourHabitat.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(animal.position)) == 0))
    }
    sm.animals.foreach(animal => assert(ourHabitat.areas.filterNot(_.areaType == Fertile).count(a => a.contains(animal.position)) == 0))
  }

  test("Test 100 times multiple animals to random destinations in ours habitat"){
    for (_ <- 0 until 100) {
      val dest1 = Point.getRandomPoint(Point(MaxX,MaxY))
      val dest2 = Point.getRandomPoint(Point(MaxX,MaxY))
      val dest3 = Point.getRandomPoint(Point(MaxX,MaxY))
      val dest4 = Point.getRandomPoint(Point(MaxX,MaxY))

      val sm: ShiftManager = ShiftManager(ourHabitat,(tiger, dest1), (elephant, dest2), (dog, dest3), (cat, dest4))
      for (_ <- 0 to 100) {
        require(sm.animals.size == 4)
        sm.animals.foreach(animal => require(ourHabitat.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(animal.position)) == 0))
      }
      sm.animals.foreach(animal => assert(ourHabitat.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(animal.position)) == 0))
    }
  }

  test("Multiple animals may arrive at different random destinations in Random Habitat") {
    val randHabitat = Habitat(RandomHabitatType,Probability(0), (500,500), Seq.empty)

    val tiger: Animal = Animal(Species("tiger", Medium, 10, 10, Herbivore), getLegalRandomPoint(randHabitat))
    val elephant: Animal = Animal(Species( "elephant", Medium, 10, 10, Herbivore), getLegalRandomPoint(randHabitat))
    val dog: Animal = Animal(Species("dog", Medium, 20, 20, Carnivore ), getLegalRandomPoint(randHabitat))
    val cat: Animal = Animal(Species( "cat", Small, 15, 10, Carnivore), getLegalRandomPoint(randHabitat))

    val sm: ShiftManager = ShiftManager(randHabitat,(tiger, dest1), (elephant, dest2), (dog, dest3), (cat, dest4))
    for (_ <- 0 to 100) {
      sm.walk()
      sm.animals.foreach(animal => require(randHabitat.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(animal.position)) == 0))
    }
    sm.animals.foreach(animal => assert(randHabitat.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(animal.position)) == 0))
  }

  test("Destination is inside non walkable area"){
    val area = Area(Water, RectangleArea(Point(15, 15), Point(50,50)))
    val h = Habitat(unexpectedEvents = Probability(10), areas = Seq(area))
    val tiger: Animal = Animal(Species("tiger", Medium, 10, 10, Carnivore), getLegalRandomPoint(h))
    val sm = ShiftManager(h, (tiger, Point(30,30) ))
    for (_ <- 1 to 100){
      sm.walk()
    }
    assert(!area.contains(sm.animals.head.position))
  }

  def getLegalRandomPoint(h: Habitat): Point = {
    val p = Point(Random.nextInt(h.dimensions._1), Random.nextInt(h.dimensions._2))
    if (h.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(p)) == 0) p else getLegalRandomPoint(h)
  }
}
