import model.{Area, Fertile, GrowFood, Probability, Vegetable}
import org.scalatest.funsuite.AnyFunSuite

class AreaTest extends AnyFunSuite{

  val image = "vegetable.png"

  test("Create illegal Fertile Area"){
    assertThrows[IllegalArgumentException](Area(Fertile, (20,20), (15, 25)))
  }

  test("Create empty Fertile Area without name"){
    val area = Area(Fertile, (10,20), (15, 25))
    assert(area.areaType == Fertile)
  }

  test("Create FertileArea with GrowFood"){
    // TODO: work on it with a working manager for food and areas
    val areaWithGrowFood = Area((10,20), (15, 25), Probability(30))
    assert(areaWithGrowFood.isInstanceOf[Area with GrowFood])
  }

}
