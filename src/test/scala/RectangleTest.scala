import org.scalatest.funsuite.AnyFunSuite
import utility.{Point, RectangleArea}

import scala.util.Random

class RectangleTest extends AnyFunSuite{

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
    val testQ = 1000
    val r = RectangleArea(Point(0, 0), Point(1, 1))
    for (_ <- 0 to testQ) {
      r.getIn4Quadrant(size).foreach(rect => assert(rect.isValid))
    }
  }

  test("Find random rectangles in not squared habitat "){
    val testQ = 1000
    val r = RectangleArea(Point(0, 0), Point(1, 1))
    for (_ <- 0 to testQ) {
      r.getIn4Quadrant((Random.between(100, 1000), Random.between(100, 1000))).foreach(rect => assert(rect.isValid))
    }
  }
}
