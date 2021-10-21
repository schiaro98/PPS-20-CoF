package controller

import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.RectangleArea

import scala.util.Random

class ShiftManagerTest extends AnyFunSuite {
  val ourHabitat: Habitat = Habitat(Probability(0))

  val MaxX = 500
  val MaxY = 500

  //species
  val tiger: Species = Species("tiger", Medium, 10, 10, Herbivore)
  val elephant: Species = Species("elephant", Medium, 10, 10, Herbivore)
  val dog: Species = Species("dog", Medium, 20, 20, Carnivore)
  val cat: Species = Species("cat", Small, 15, 10, Carnivore)

  val t1: Animal = Animal(tiger, getLegalRandomPoint(ourHabitat))
  val e1: Animal = Animal(elephant, getLegalRandomPoint(ourHabitat))
  val d1: Animal = Animal(dog, getLegalRandomPoint(ourHabitat))
  val c1: Animal = Animal(cat, getLegalRandomPoint(ourHabitat))
  //destinations
  val dest1: Point = Point.getRandomPoint(Point(MaxX, MaxY))
  val dest2: Point = Point.getRandomPoint(Point(MaxX, MaxY))
  val dest3: Point = Point.getRandomPoint(Point(MaxX, MaxY))
  val dest4: Point = Point.getRandomPoint(Point(MaxX, MaxY))


  test("Create ShiftManager") {
    val sm: ShiftManager = ShiftManager(ourHabitat, Map(t1 -> Point(55, 100)))
    assert(sm.animals.size == 1)
  }

  test("Single animal arrives at destination in empty habitat") {
    val dest = Point(55, 100)
    val habitat = Habitat(EmptyHabitatType, Probability(0), (500, 500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat, Map(t1 -> dest))
    while (sm.animals.count(_.position == dest) != 1) {
      sm.walk()
    }
    assert(sm.animals.count(_.position == dest) == 1)
  }

  test("Multiple animals arrive at different random destinations in empty habitat") {
    val destinations = Set(dest1, dest2, dest3, dest4)
    val habitat = Habitat(EmptyHabitatType, Probability(0), (MaxX, MaxY), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat,
      Map(t1 -> dest1,
        e1 -> dest2,
        d1 -> dest3,
        c1 -> dest4
      ))
    while (sm.animals.count(a => destinations.contains(a.position)) != sm.animals.size) {
      sm.walk()
    }
    assert(sm.animals.count(a => destinations.contains(a.position)) == sm.animals.size)
  }

  test("Multiple animals arrive at the same random destination in empty habitat") {
    val r = (x: Int) => Random.nextInt(x)
    val dest = Point(r(500), r(500))
    val habitat = Habitat(EmptyHabitatType, Probability(0), (500, 500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat, (t1, dest), (e1, dest), (d1, dest), (c1, dest))
    while (sm.animals.count(_.position == dest) != sm.animals.size) {
      sm.walk()
    }
    assert(sm.animals.count(_.position == dest) == sm.animals.size)
  }

  test("One animal walk to a random destination in ours habitat") {
    val d = getLegalRandomPoint(ourHabitat)
    val dist = t1.position.distance(d)
    val sm: ShiftManager = ShiftManager(ourHabitat, (t1, d))
    for (_ <- 0 to 100) {
      sm.walk()
    }
    assert(sm.animals.head.position.distance(d) < dist)
  }

  test("Test 100 times multiple animals walk to one random destination in ours habitat") {
    for (_ <- 0 until 100) {
      val dest = Point.getRandomPoint(Point(MaxX, MaxY))

      val sumOfDistances = t1.position.distance(dest) + e1.position.distance(dest) +
        d1.position.distance(dest) + c1.position.distance(dest)

      val sm: ShiftManager = ShiftManager(ourHabitat, (t1, dest), (e1, dest), (d1, dest), (c1, dest))
      for (_ <- 0 to 100) {
        sm.walk()
        require(sm.animals.size == 4)
      }
      val sumOfDistancesAfterWalking = sm.animals.map(_.position.distance(dest)).sum
      assert(sumOfDistancesAfterWalking < sumOfDistances)
    }
  }

  test("Multiple animals walk to different random destinations in Random Habitat") {
    val randHabitat = Habitat(RandomHabitatType, Probability(0), (500, 500), Seq.empty)

    val t: Animal = Animal(tiger, getLegalRandomPoint(randHabitat))
    val e: Animal = Animal(elephant, getLegalRandomPoint(randHabitat))
    val d: Animal = Animal(dog, getLegalRandomPoint(randHabitat))
    val c: Animal = Animal(cat, getLegalRandomPoint(randHabitat))

    val sm: ShiftManager = ShiftManager(randHabitat, (t, dest1), (e, dest2), (d, dest3), (c, dest4))
    for (_ <- 0 to 100) {
      sm.walk()
      sm.animals.count(isAnimalInLegalPosition(randHabitat, _)) == sm.animals.size
    }
    assert(sm.animals.count(isAnimalInLegalPosition(randHabitat, _)) == sm.animals.size)
  }

  test("Destination is inside non walkable area and animal doesn't go inside") {
    val area = Area(Water, RectangleArea(Point(15, 15), Point(50, 50)))
    val h = Habitat(unexpectedEvents = Probability(10), areas = Seq(area))
    val t: Animal = Animal(tiger, getLegalRandomPoint(h))
    val sm = ShiftManager(h, (t, Point(30, 30)))
    for (_ <- 1 to 100) {
      sm.walk()
    }
    assert(!area.contains(sm.animals.head.position))
  }

  def isAnimalInLegalPosition(h: Habitat, a:Animal): Boolean =
    h.areas.filterNot(_.areaType == Fertile).count(_.contains(a.position)) == 0

  def getLegalRandomPoint(h: Habitat): Point = {
    val p = Point(Random.nextInt(h.dimensions._1), Random.nextInt(h.dimensions._2))
    if (h.areas.filterNot(_.areaType.walkable).count(a => a.contains(p)) == 0) p else getLegalRandomPoint(h)
  }

  def getAnimalFromSpecies(s: Species, sm: ShiftManager): Animal = sm.animals.filter(_.species == s).head
}
