package controller

import controller.manager.ResourceManager
import model.{shape, _}
import model.food.{FoodType, Vegetable}
import model.habitat.{Area, Fertile, Habitat, Rock, Volcano, Water}
import model.position.Point
import model.shape.RectangleArea
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, NoSuchFileException, Path}
import scala.reflect.io.File

class ResourceManagerTest extends AnyFunSuite{

  val fertileArea: Area = model.habitat.Area(Fertile, RectangleArea(Point(0,0), Point(10,10)),probability = Probability(100))
  val waterArea: Area = model.habitat.Area(Water, shape.RectangleArea(Point(0, 15), Point(15,30)))
  val rockArea: Area = model.habitat.Area(Rock, shape.RectangleArea(Point(40,40), Point(50,50)))
  val volcanoArea: Area = model.habitat.Area(Volcano, shape.RectangleArea(Point(70,70), Point(80,80)))
  val area: Area = model.habitat.Area(Fertile, shape.RectangleArea(Point(60,60), Point(65, 65)), Probability(100))

  val habitat: Habitat = model.habitat.Habitat( Probability(30), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea, area))

  val foodTypes = Set(FoodType(5, Vegetable), FoodType(10, Vegetable), FoodType(15, Vegetable), FoodType(50, Vegetable))

  test("ResourceManager grow() with no growable foodTypes"){
    val resMan = ResourceManager(habitat)

    val newResMan = resMan.grow()
    assert(newResMan.foodTypes.isEmpty)
  }

  test("ResourceManager grow()"){
    val resMan = ResourceManager(habitat, foodTypes)
    val newResMan = resMan.grow()
    assert(newResMan.foods.nonEmpty)
  }

  test("ResourceManager import food from non existing file"){
    val resMan = ResourceManager(habitat, foodTypes)
    assertThrows[NoSuchFileException](resMan.importFoodTypesFromFile("absolutelyNonExistingFileMadeUpOnlyForThisTest.txtxtxt"))
  }

  test("ResourceManager write food to file"){
    val resMan = ResourceManager(habitat, foodTypes)
    resMan.writeFoodTypesToFile(Constants.FoodsFilePath)
    val path = Path.of("res"+File.separator+"serialization"+File.separator+Constants.FoodsFilePath)
    val json = Files.readString(path, StandardCharsets.UTF_8)
    assert(json == "{\"color\":{\"value\":-16755701,\"falpha\":0.0},\"energy\":5,\"foodCategory\":\"Vegetable\"}{\"color\":{\"value\":-16755701,\"falpha\":0.0},\"energy\":10,\"foodCategory\":\"Vegetable\"}{\"color\":{\"value\":-16755701,\"falpha\":0.0},\"energy\":15,\"foodCategory\":\"Vegetable\"}{\"color\":{\"value\":-16755701,\"falpha\":0.0},\"energy\":50,\"foodCategory\":\"Vegetable\"}")
  }

  test("ResourceManager import food from file"){
    val resMan = ResourceManager(habitat)
    val newResMan = resMan.importFoodTypesFromFile(Constants.FoodsFilePath)
    assert(newResMan.foodTypes.nonEmpty)
  }

  test("Initialize habitat with foods"){
    val resMan = ResourceManager(habitat, Constants.FoodsFilePath)
    val newResMan = resMan.fillHabitat()
    assert(newResMan.foods.lengthCompare(Constants.InitialFoods) > 0)
  }

  test("Try to initialize habitat with all fertile areas with zero fertility"){
    val f1: Area = model.habitat.Area(Fertile, shape.RectangleArea(Point(0,0), Point(10,10)),probability = Probability(0))
    val f2: Area = model.habitat.Area(Fertile, shape.RectangleArea(Point(60,60), Point(65, 65)), Probability(0))
    val w: Area = model.habitat.Area(Water, shape.RectangleArea(Point(0, 15), Point(15,30)))
    val habitat: Habitat = model.habitat.Habitat( Probability(30),(100,100), Seq(f1,f2,w))
    val resMan = ResourceManager(habitat, Constants.FoodsFilePath)
    val newResMan = resMan.fillHabitat()
    assert(newResMan.foods.lengthCompare(resMan.foods.size) == 0)
  }

}
