import controller.ShiftManager
import model.Size.{Medium, Small}
import model.{Animal, Area, Carnivorous, EmptyHabitatType, Habitat, Herbivore, Probability, Species}
import org.scalatest.funsuite.AnyFunSuite
import utility.Point

import scala.util.Random

class ShiftManagerTest extends AnyFunSuite{

  val ourHabitat = Habitat(Probability(0))

  val path = "res/images/tiger-icon.jpg"
  val tiger: Animal = Carnivorous(Species(path, "tiger", Medium, 10, 10), Point(15,15))
  val elephant: Animal = Herbivore(Species(path, "elephant", Medium, 10, 10),  Point(40,40))
  val dog: Animal = Carnivorous(Species(path, "dog", Medium, 20,20), Point(200,200))
  val cat: Animal = Carnivorous(Species(path, "cat", Small, 15,10), Point(100,20))

  test("Create ShiftManager"){
    val sm: ShiftManager = ShiftManager(ourHabitat, Map(tiger->Some(Point(55,100)), elephant->None))
    assert(sm.animals.size == 2)
  }

  test("Single animal arrives at destination in empty habitat"){
    val dest = Point(55,100)
    val habitat = Habitat(EmptyHabitatType,Probability(0),(500,500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat, Map(tiger->Some(dest)))
    for(i <- 0 to 40){
      sm.walk()
    }
    assert(sm.animals.count(a => a.position == dest) == 1)
  }

  test("Multiple animals arrive at different random destinations in empty habitat"){
    val r = (x:Int) => Random.nextInt(x)
    val dest1 = Point(r(500),r(500))
    val dest2 = Point(r(500),r(500))
    val dest3 = Point(r(500),r(500))
    val dest4 = Point(r(500),r(500))
    val destinations = Set(dest1,dest2,dest3,dest4)
    val habitat = Habitat(EmptyHabitatType,Probability(0),(500,500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat,
      Map(tiger->Some(dest1),
        elephant->Some(dest2),
        dog->Some(dest3),
        cat->Some(dest4)
      ))
      //100 iterations should be enough
    for(i <- 0 to 100){
      sm.walk()
    }
    assert(sm.animals.count(a => destinations.contains(a.position)) == sm.animals.size)
  }

  test("Multiple animals arrive at the same random destination in empty habitat"){
    val r = (x:Int) => Random.nextInt(x)
    val dest = Point(r(500),r(500))
    val habitat = Habitat(EmptyHabitatType,Probability(0),(500,500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat,
      Map(tiger->Some(dest),
        elephant->Some(dest),
        dog->Some(dest),
        cat->Some(dest)
      ))
    //100 iterations should be enough
    for(i <- 0 to 100){
      sm.walk()
    }
    assert(sm.animals.count(a => a.position == dest) == sm.animals.size)
  }

//  test("Testing walk in ShiftManager"){
//    val sm: ShiftManager = ShiftManager(ourHabitat, Map(c->Some(Point(55,100)), h->None))
//    val fuck = sm.walk()
//    println(fuck.foreach(println))
//  }

}
