import controller.ShiftManager
import model.Size.Medium
import model.{Animal, Area, Carnivorous, EmptyHabitatType, Habitat, Herbivore, Probability, Species}
import org.scalatest.funsuite.AnyFunSuite
import utility.Point

import scala.collection.mutable

class ShiftManagerTest extends AnyFunSuite{

  val ourHabitat = Habitat(Probability(0))

  val path = "res/images/tiger-icon.jpg"
  val c: Animal = Carnivorous(Species(path, "tiger", Medium, 10, 10), Point(15,15))
  val h: Animal = Herbivore(Species(path, "elephant", Medium, 10, 10),  Point(40,40))

  test("Create ShiftManager"){
    val sm: ShiftManager = ShiftManager(ourHabitat, mutable.Map(c->Some(Point(55,100)), h->None))
    assert(sm.animals.size == 2)
  }

  test("Test ShiftManager in an empty habitat"){
    val habitat = Habitat(EmptyHabitatType,Probability(0),(500,500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat, mutable.Map(c->Some(Point(55,100)), h->Some(Point(0,0))))
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    sm.walk()
    assert(!sm.animalsDestinations.contains(c))
    assert(!sm.animalsDestinations.contains(h))
  }

  test("Testing walk in ShiftManager"){
    val sm: ShiftManager = ShiftManager(ourHabitat, mutable.Map(c->Some(Point(55,100)), h->None))
    val fuck = sm.walk()
//    println(fuck.foreach(println))
  }

}
