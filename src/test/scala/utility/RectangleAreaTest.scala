package utility

import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class RectangleAreaTest extends AnyFunSuite{

  val testQ = 1000

  test("Create a rectangle area"){
    val rectangle = RectangleArea(Point(0,0), Point(1,1))
    assert(rectangle.width == 1)
    assert(rectangle.height == 1)
  }

  test("Create an illegal rectangle"){
    assertThrows[IllegalArgumentException](RectangleArea(Point(1,1), Point(0,0)))
  }

  test("Find random rectangles"){
    val size = (1000, 1000)
    val r = RectangleArea(Point(0, 0), Point(1, 1))
    for (_ <- 0 to testQ) {
      r.getIn4Quadrant(size).foreach(rect => assert(rect.isValid))
    }
  }

  test("Find random rectangles in not squared habitat "){
    val r = RectangleArea(Point(0, 0), Point(1, 1))
    for (_ <- 0 to testQ) {
      r.getIn4Quadrant((Random.between(100, 1000), Random.between(100, 1000))).foreach(rect => assert(rect.isValid))
    }
  }

  test("Check if a Point is inside a rectangle"){
    val r = RectangleArea(Point(0,0), Point(100,100))
    assert(r.contains(Point(50,50)))
  }
}
