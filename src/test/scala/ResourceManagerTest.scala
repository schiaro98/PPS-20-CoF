import controller.{FoodsFromFile, ResourceManager}
import model.{Area, Fertile, Food, Habitat, Probability, Rock, Volcano, Water}
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, NoSuchFileException, Path}
import scala.reflect.io.File

class ResourceManagerTest extends AnyFunSuite{

  val icon = "icon.png"

  val fertileArea: Area = Area(Fertile, (0,0), (10,10))
  val waterArea: Area = Area(Water, (0, 15), (15,30))
  val rockArea: Area = Area(Rock, (40,40), (50,50))
  val volcanoArea: Area = Area(Volcano, (70,70), (80,80))
  val area: Area = Area((60,60), (65, 65), Probability(100))

  val habitat: Habitat = Habitat( Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea, area))

  test("ResourceManager grow() with no growable foods"){
    val resMan = ResourceManager(habitat)

    val newResMan = resMan.grow()
    assert(newResMan.foods.isEmpty)
  }

  test("ResourceManager grow()"){
    val resMan = ResourceManager(habitat, Set(Food(icon, 5), Food(icon, 10), Food(icon, 15), Food(icon, 50)))
    val newResMan = resMan.grow()
//    println(newResMan.foods)
    assert(newResMan.foods.nonEmpty)
  }

  test("ResourceManager import food from non existing file"){
    val resMan = ResourceManager(habitat, Set(Food(icon, 5), Food(icon, 10), Food(icon, 15), Food(icon, 50)))
    assertThrows[NoSuchFileException](resMan.importFoodsFromFile("absolutelyNonExistingFileMadeUpOnlyForThisTest.txtxtxt"))
  }

  test("ResourceManager write food to file"){
    val resMan = ResourceManager(habitat, Set(Food(icon, 5), Food(icon, 10), Food(icon, 15), Food(icon, 50)))
    resMan.writeFoodsToFile(Constants.FOODS_FILE_PATH)
    val path = Path.of("res"+File.separator+"serialization"+File.separator+Constants.FOODS_FILE_PATH)
    val json = Files.readString(path, StandardCharsets.UTF_8)
//    println(json)
    assert(json == "{\"icon\":\"icon.png\",\"energy\":5}{\"icon\":\"icon.png\",\"energy\":10}{\"icon\":\"icon.png\",\"energy\":15}{\"icon\":\"icon.png\",\"energy\":50}")
  }

  test("ResourceManager import food from file"){
    val resMan = ResourceManager(habitat)
    val newResMan = resMan.importFoodsFromFile(Constants.FOODS_FILE_PATH)
//    println(newResMan.growableFoods)
    assert(newResMan.growableFoods.nonEmpty)
  }

  test("ResourceManager from existing file with foods"){
    //making sure there is a file with foods
    val initResMAn = ResourceManager(habitat, Set(Food(icon, 5), Food(icon, 10), Food(icon, 15), Food(icon, 50)))
    initResMAn.writeFoodsToFile(Constants.FOODS_FILE_PATH)

    val resMan = ResourceManager(FoodsFromFile, habitat, Constants.FOODS_FILE_PATH)
    assert(resMan.growableFoods.nonEmpty)
    assert(resMan.growableFoods == initResMAn.growableFoods)
  }

}
