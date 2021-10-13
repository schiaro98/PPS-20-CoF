import controller.ShiftManager
import model.Size.{Medium, Small}
import model.Type.{Carnivore, Herbivore}
import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.Point

import scala.util.Random

class ShiftManagerTest extends AnyFunSuite {

  val ourHabitat: Habitat = Habitat(Probability(0))

  val path = "res/images/tiger-icon.jpg"
  val tiger: Animal = Animal(Species(path, "tiger", Medium, 10, 10, Herbivore), getLegalRandomPoint(ourHabitat))
  val elephant: Animal = Animal(Species(path, "elephant", Medium, 10, 10, Herbivore), getLegalRandomPoint(ourHabitat))
  val dog: Animal = Animal(Species(path, "dog", Medium, 20, 20, Carnivore ), getLegalRandomPoint(ourHabitat))
  val cat: Animal = Animal(Species(path, "cat", Small, 15, 10, Carnivore), getLegalRandomPoint(ourHabitat))
  val MaxX = 500
  val MaxY = 500

  test("Create ShiftManager") {
    val sm: ShiftManager = ShiftManager(ourHabitat, Map(tiger -> Point(55, 100)))
    assert(sm.animals.size == 2)
  }

  test("Single animal arrives at destination in empty habitat") {
    val dest = Point(55, 100)
    val habitat = Habitat(EmptyHabitatType, Probability(0), (500, 500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat, Map(tiger -> dest))
    var i = 0
    while (sm.animals.count(a => a.position == dest) != 1){
      sm.walk()
      i+=1
    }
//    println(i)
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
    var i = 0
    while(sm.animals.count(a => destinations.contains(a.position)) != sm.animals.size) {
      sm.walk()
      require(1 < 100)
    }
    assert(sm.animals.count(a => destinations.contains(a.position)) == sm.animals.size)
  }

  test("Multiple animals arrive at the same random destination in empty habitat") {
    val r = (x: Int) => Random.nextInt(x)
    val dest = Point(r(500), r(500))
    val habitat = Habitat(EmptyHabitatType, Probability(0), (500, 500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat, (tiger, dest),(elephant, dest), (dog,dest), (cat,dest))
    var i = 0
    while(sm.animals.count(a => a.position == dest) != sm.animals.size) {
      sm.walk()
      require(1 < 100)
    }
    assert(sm.animals.count(a => a.position == dest) == sm.animals.size)
  }

  test("One animal arrives at different random destination in ours habitat") {
    val d = getLegalRandomPoint(ourHabitat)
    val sm: ShiftManager = ShiftManager(ourHabitat,(tiger, d))
    var i = 0
    while(sm.animals.count(a => a.position == d) != sm.animals.size){
      i += 1
      sm.walk()
      sm.animals.foreach(animal => require(ourHabitat.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(animal.position)) == 0))
    }
    assert(sm.animals.count(a => a.position == d) == sm.animals.size)
  }

  test("Multiple animals arrive at different random destinations in ours habitat") {
    val dest1 = getLegalRandomPoint(ourHabitat)
    val dest2 = getLegalRandomPoint(ourHabitat)
    val dest3 = getLegalRandomPoint(ourHabitat)
    val dest4 = getLegalRandomPoint(ourHabitat)
    val destinations = Set(dest1, dest2, dest3, dest4)
    val sm: ShiftManager = ShiftManager(ourHabitat,(tiger, dest1), (elephant, dest2), (dog, dest3), (cat, dest4))
    var i = 0
    while(sm.animals.count(a => destinations.contains(a.position)) != sm.animals.size){
      i += 1
      sm.walk()
      sm.animals.foreach(animal => require(ourHabitat.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(animal.position)) == 0))
    }
    assert(sm.animals.count(a => destinations.contains(a.position)) == sm.animals.size)
  }



  def getLegalRandomPoint(h: Habitat): Point = {
    val p = Point(Random.nextInt(h.dimensions._1), Random.nextInt(h.dimensions._2))
    if (h.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(p)) == 0) p else getLegalRandomPoint(h)
  }

}
