package model

import model.habitat.{Area, Fertile, GrowFood}
import model.position.Point
import model.shape.RectangleArea
import org.scalatest.funsuite.AnyFunSuite

class AreaTest extends AnyFunSuite{

  val image = "vegetable.png"

  test("Create illegal Fertile Area"){
    assertThrows[IllegalArgumentException](habitat.Area(Fertile, RectangleArea(Point(20,20), Point(15, 25))))
  }

  test("Create empty Fertile Area without name"){
    val area = habitat.Area(Fertile, shape.RectangleArea(Point(10,20), Point(15, 25)))
    assert(area.areaType == Fertile)
  }

  test("Create FertileArea with GrowFood"){
    val areaWithGrowFood = habitat.Area(Fertile, shape.RectangleArea(Point(10,20), Point(15, 25)), Probability(50))
    assert(areaWithGrowFood.isInstanceOf[Area with GrowFood])
  }

  test("Test if area contains a Point"){
    val areaWithGrowFood = habitat.Area(Fertile, shape.RectangleArea(Point(10,20), Point(15, 25)), Probability(50))
    assert(areaWithGrowFood.contains(Point(13,23)))
  }

}
