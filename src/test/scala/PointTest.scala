import org.scalatest.funsuite.AnyFunSuite
import utility.Point

import scala.util.Random

class PointTest extends AnyFunSuite{
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

  test("Distance between two points"){
    val p1 = Point(0,0)
    val p2 = Point(1,0)
    val p3 = Point(1,1)
    assert(p1.distance(p2) == 1)
    assert(p1.distance(p3) == Math.sqrt(2))
  }

  test("Find random points between 0 and limits"){
    val size = Point(1000, 1000)
    val testQ = 1000
    var p = Point(0,0)
    for (_ <- 0 to testQ) {
      p = p.getRandomPoint(size)
      assert(p.x >= 0 && p.y >= 0)
      assert(p.x <= size.x && p.y <= size.y)
    }
  }

  test("Find random points between limits"){
    val size = Point(1000, 1000)
    val testQ = 1000
    var p = Point(0,0)
    for (_ <- 0 to testQ) {
      val limits = p.getRandomPoint(size)
      p = p.getRandomPoint(limits, size)
      assert(p.x >= 0 && p.y >= 0)
      assert(p.x >= limits.x && p.y >= limits.y)
      assert(p.x <= size.y && p.y <= size.y)
    }
  }
}
