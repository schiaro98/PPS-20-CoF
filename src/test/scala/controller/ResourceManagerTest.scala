package controller

import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.{Constants, RectangleArea}

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, NoSuchFileException, Path}
import scala.reflect.io.File

class ResourceManagerTest extends AnyFunSuite{

  val fertileArea: Area = Area(Fertile, RectangleArea(Point(0,0), Point(10,10)),probability = Probability(0))
  val waterArea: Area = Area(Water, RectangleArea(Point(0, 15), Point(15,30)))
  val rockArea: Area = Area(Rock, RectangleArea(Point(40,40), Point(50,50)))
  val volcanoArea: Area = Area(Volcano, RectangleArea(Point(70,70), Point(80,80)))
  val area: Area = Area(Fertile, RectangleArea(Point(60,60), Point(65, 65)), Probability(100))

  val habitat: Habitat = Habitat( Probability(30), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea, area))

  val foods = Set(Food(5, Vegetable), Food(10, Vegetable), Food(15, Vegetable), Food(50, Vegetable))

  test("ResourceManager grow() with no growable foods"){
    val resMan = ResourceManager(habitat)

    val newResMan = resMan.grow()
    assert(newResMan.foodInstances.isEmpty)
  }

  test("ResourceManager grow()"){
    val resMan = ResourceManager(habitat, foods)
    val newResMan = resMan.grow()
    assert(newResMan.foodInstances.nonEmpty)
  }

  test("ResourceManager import food from non existing file"){
    val resMan = ResourceManager(habitat, foods)
    assertThrows[NoSuchFileException](resMan.importFoodsFromFile("absolutelyNonExistingFileMadeUpOnlyForThisTest.txtxtxt"))
  }

  test("ResourceManager write food to file"){
    val resMan = ResourceManager(habitat, foods)
    resMan.writeFoodsToFile(Constants.FoodsFilePath)
    val path = Path.of("res"+File.separator+"serialization"+File.separator+Constants.FoodsFilePath)
    val json = Files.readString(path, StandardCharsets.UTF_8)
    assert(json == "{\"color\":{\"value\":-16755701,\"falpha\":0.0},\"energy\":5,\"foodType\":\"Vegetable\"}{\"color\":{\"value\":-16755701,\"falpha\":0.0},\"energy\":10,\"foodType\":\"Vegetable\"}{\"color\":{\"value\":-16755701,\"falpha\":0.0},\"energy\":15,\"foodType\":\"Vegetable\"}{\"color\":{\"value\":-16755701,\"falpha\":0.0},\"energy\":50,\"foodType\":\"Vegetable\"}")
  }

  test("ResourceManager import food from file"){
    val resMan = ResourceManager(habitat)
    val newResMan = resMan.importFoodsFromFile(Constants.FoodsFilePath)
    assert(newResMan.foods.nonEmpty)
  }


  test("Initialize habitat with foods"){
    val resMan = ResourceManager(habitat, Constants.FoodsFilePath)
    val newResMan = resMan.fillHabitat()
    assert(newResMan.foodInstances.size > habitat.areas.count(a => a.areaType == Fertile) * Constants.FoodToGrowPerFertileArea)
  }

  test("Try to initialize habitat with all fertile areas with zero fertility"){
    val f1: Area = Area(Fertile, RectangleArea(Point(0,0), Point(10,10)),probability = Probability(0))
    val f2: Area = Area(Fertile, RectangleArea(Point(60,60), Point(65, 65)), Probability(0))
    val w: Area = Area(Water, RectangleArea(Point(0, 15), Point(15,30)))
    val habitat: Habitat = Habitat( Probability(30),(100,100), Seq(f1,f2,w))
    val resMan = ResourceManager(habitat, Constants.FoodsFilePath)
    val newResMan = resMan.fillHabitat()
    assert(newResMan.foodInstances.size== resMan.foodInstances.size)
  }

}
