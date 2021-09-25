import org.scalatest.funsuite.AnyFunSuite
import utility.{Point, RectangleArea}

class PointRectangleTest extends AnyFunSuite{
  test("Use + on Point"){
    val pointZero = Point(0,0)
    val pointOne = pointZero + (1,1)
    assert(pointOne == Point(1,1))
  }

  test("Use - on Point"){
    val pointOne = Point(1,1)
    val pointZero = pointOne - (1,1)
    assert(pointZero == Point(0,0))
  }

  test("Create a rectangle area"){
    val rectangle = RectangleArea(Point(0,0), Point(1,1))
    assert(rectangle.width == 1)
    assert(rectangle.height == 1)
  }

}
