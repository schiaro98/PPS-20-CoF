import controller.ShiftManager
import model.Size.{Medium, Small}
import model.Type.Carnivore
import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.Point

import scala.util.Random

class ShiftManagerTest extends AnyFunSuite{

  val ourHabitat: Habitat = Habitat(Probability(0))

  val path = "res/images/tiger-icon.jpg"
  val tiger: Animal = Animal(Species(path, "tiger", Medium, 10, 10, Carnivore), getPointInFertileAreas(ourHabitat))
  val elephant: Animal = Animal(Species(path, "elephant", Medium, 10, 10, Carnivore),  getPointInFertileAreas(ourHabitat))
  val dog: Animal = Animal(Species(path, "dog", Medium, 20,20, Carnivore), getPointInFertileAreas(ourHabitat))
  val cat: Animal = Animal(Species(path, "cat", Small, 15,10, Carnivore), getPointInFertileAreas(ourHabitat))
  val MaxX = 500
  val MaxY = 500

  test("Create ShiftManager"){
    val sm: ShiftManager = ShiftManager(ourHabitat, Map(tiger->Seq(Point(55,100)), elephant->Seq.empty))
    assert(sm.animals.size == 2)
  }

  test("Single animal arrives at destination in empty habitat"){
    val dest = Point(55,100)
    val habitat = Habitat(EmptyHabitatType,Probability(0),(500,500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat, Map(tiger->Seq(dest)))
    for(_ <- 0 to 40){
      sm.walk()
    }
    assert(sm.animals.count(a => a.position == dest) == 1)
  }

  test("Multiple animals arrive at different random destinations in empty habitat"){
    val r = (x:Int) => Random.nextInt(x)
    val dest1 = Point(r(MaxX),r(MaxY))
    val dest2 = Point(r(MaxX),r(MaxY))
    val dest3 = Point(r(MaxX),r(MaxY))
    val dest4 = Point(r(MaxX),r(MaxY))
    val destinations = Set(dest1,dest2,dest3,dest4)
    val habitat = Habitat(EmptyHabitatType,Probability(0),(MaxX, MaxY), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat,
      Map(tiger->Seq(dest1),
        elephant->Seq(dest2),
        dog->Seq(dest3),
        cat->Seq(dest4)
      ))
      //100 iterations should be enough
    for(_ <- 0 to 100){
      sm.walk()
    }
    assert(sm.animals.count(a => destinations.contains(a.position)) == sm.animals.size)
  }

  test("Multiple animals arrive at the same random destination in empty habitat"){
    val r = (x:Int) => Random.nextInt(x)
    val dest = Point(r(500),r(500))
    val habitat = Habitat(EmptyHabitatType,Probability(0),(500,500), Seq.empty[Area])
    val sm: ShiftManager = ShiftManager(habitat,
      Map(tiger->Seq(dest),
        elephant->Seq(dest),
        dog->Seq(dest),
        cat->Seq(dest)
      ))
    //100 iterations should be enough
    for(_ <- 0 to 100){
      sm.walk()
    }
    assert(sm.animals.count(a => a.position == dest) == sm.animals.size)
  }

  test("Multiple animals arrive at different random destinations in ours habitat"){
    val dest1 = getPointInFertileAreas(ourHabitat)
    val dest2 = getPointInFertileAreas(ourHabitat)
    val dest3 = getPointInFertileAreas(ourHabitat)
    val dest4 = getPointInFertileAreas(ourHabitat)
    val destinations = Set(dest1,dest2,dest3,dest4)
    val sm: ShiftManager = ShiftManager(ourHabitat,
      Map(tiger->Seq(dest1),
        elephant->Seq(dest2),
        dog->Seq(dest3),
        cat->Seq(dest4)
      ))
    //100 iterations should be enough
    for(_ <- 0 to 3000){
      sm.walk()
      sm.animals
        .foreach(animal => require(ourHabitat.areas.filterNot(a => a.areaType == Fertile)
          .count(a => a.contains(animal.position))==0))
    }
    assert(sm.animals.count(a => destinations.contains(a.position)) == sm.animals.size)
  }

  def getPointInFertileAreas(h: Habitat):Point = {
//    val r = (x:Int) => Random.nextInt(x)
    val p = Point(Random.nextInt(h.dimensions._1), Random.nextInt(h.dimensions._2))
    if (h.areas.filter(a => a.areaType == Fertile).count(a=>a.contains(p))==1) p else getPointInFertileAreas(h)
  }

}
