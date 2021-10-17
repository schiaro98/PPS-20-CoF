import controller.ResourceManager
import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.{Constants, Point, RectangleArea}

import java.awt.Color
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, NoSuchFileException, Path}
import scala.reflect.io.File

class ResourceManagerTest extends AnyFunSuite{

  val color: Color = Color.black

  val fertileArea: Area = Area(Fertile, RectangleArea(Point(0,0), Point(10,10)),probability = Probability(0))
  val waterArea: Area = Area(Water, RectangleArea(Point(0, 15), Point(15,30)))
  val rockArea: Area = Area(Rock, RectangleArea(Point(40,40), Point(50,50)))
  val volcanoArea: Area = Area(Volcano, RectangleArea(Point(70,70), Point(80,80)))
  val area: Area = Area(Fertile, RectangleArea(Point(60,60), Point(65, 65)), Probability(100))

  val habitat: Habitat = Habitat( Probability(30), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea, area))

  test("ResourceManager grow() with no growable foods"){
    val resMan = ResourceManager(habitat)

    val newResMan = resMan.grow()
    assert(newResMan.foods.isEmpty)
  }

  test("ResourceManager grow()"){
    val resMan = ResourceManager(habitat, Set(Food(color, 5), Food(color, 10), Food(color, 15), Food(color, 50)))
    val newResMan = resMan.grow()
    assert(newResMan.foods.nonEmpty)
  }

  test("ResourceManager import food from non existing file"){
    val resMan = ResourceManager(habitat, Set(Food(color, 5), Food(color, 10), Food(color, 15), Food(color, 50)))
    assertThrows[NoSuchFileException](resMan.importFoodsFromFile("absolutelyNonExistingFileMadeUpOnlyForThisTest.txtxtxt"))
  }

  test("ResourceManager write food to file"){
    val resMan = ResourceManager(habitat, Set(Food(color, 5), Food(color, 10), Food(color, 15), Food(color, 50)))
    resMan.writeFoodsToFile(Constants.FoodsFilePath)
    val path = Path.of("res"+File.separator+"serialization"+File.separator+Constants.FoodsFilePath)
    val json = Files.readString(path, StandardCharsets.UTF_8)
    assert(json == "{\"color\":{\"value\":-16777216,\"falpha\":0.0},\"energy\":5}{\"color\":{\"value\":-16777216,\"falpha\":0.0},\"energy\":10}{\"color\":{\"value\":-16777216,\"falpha\":0.0},\"energy\":15}{\"color\":{\"value\":-16777216,\"falpha\":0.0},\"energy\":50}")
  }

  test("ResourceManager import food from file"){
    val resMan = ResourceManager(habitat)
    val newResMan = resMan.importFoodsFromFile(Constants.FoodsFilePath)
    assert(newResMan.growableFoods.nonEmpty)
  }


  // TODO: infinite loop id the habitat doesn't have at least a fertileArea with fertility>0
  test("Initialize habitat with foods"){
    val resMan = ResourceManager(habitat, Constants.FoodsFilePath)
    val newResMan = resMan.fillHabitat()
    assert(newResMan.foods.size > habitat.areas.count(a => a.areaType == Fertile) * 10)
  }

}
