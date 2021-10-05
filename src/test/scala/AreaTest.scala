import model.{Area, Fertile, GrowFood, Probability}
import org.scalatest.funsuite.AnyFunSuite
import utility.{Point, RectangleArea}

class AreaTest extends AnyFunSuite{

  val image = "vegetable.png"

  test("Create illegal Fertile Area"){
    assertThrows[IllegalArgumentException](Area(Fertile, RectangleArea(Point(20,20), Point(15, 25))))
  }

  test("Create empty Fertile Area without name"){
    val area = Area(Fertile, RectangleArea(Point(10,20), Point(15, 25)))
    assert(area.areaType == Fertile)
  }

  test("Create FertileArea with GrowFood"){
    val areaWithGrowFood = Area(Fertile, RectangleArea(Point(10,20), Point(15, 25)), Probability(50))
    assert(areaWithGrowFood.isInstanceOf[Area with GrowFood])
  }

  test("Test if area contains a Point"){
    val areaWithGrowFood = Area(Fertile, RectangleArea(Point(10,20), Point(15, 25)), Probability(50))
    assert(areaWithGrowFood.contains(Point(13,23)))
  }

}
