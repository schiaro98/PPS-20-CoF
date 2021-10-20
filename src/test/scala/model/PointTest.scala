package model

import org.scalatest.funsuite.AnyFunSuite

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
    assert(p1.distance(p3) == 2)
  }

  test("Find random points between 0 and limits"){
    val size = Point(1000, 1000)
    val testQ = 1000
    for (_ <- 0 to testQ) {
      val p: Point = Point.getRandomPoint(size)
      assert(p.x >= 0 && p.y >= 0)
      assert(p.x <= size.x && p.y <= size.y)
    }
  }

  test("Find random points between limits"){
    val size = Point(1000, 1000)
    val testQ = 1000
    for (_ <- 0 to testQ) {
      val limits = Point.getRandomPoint(size)
      val p: Point = Point.getRandomPoint(limits, size)
      assert(p.x >= 0 && p.y >= 0)
      assert(p.x >= limits.x && p.y >= limits.y)
      assert(p.x <= size.y && p.y <= size.y)
    }
  }

  test("Test isUnder"){
    val up = Point(10,20)
    val down = Point(10,30)
    assert(down.isUnder(up))
  }

  test("Test isOver"){
    val up = Point(10,20)
    val down = Point(10,30)
    assert(up.isOver(down))
  }

  test("Test isLeft"){
    val left = Point(5, 10)
    val right = Point(10,10)
    assert(left.isLeft(right))
  }

  test("isRight"){
    val left = Point(5, 10)
    val right = Point(10,10)
    assert(right.isRight(left))
  }

  test("Find distance upon x axis"){
    val left = Point(5, 10)
    val right = Point(10,10)
    assert(left.fromX(right) == 5)
    assert(right.fromX(left) == 5)
  }

  test("Find distance upon y axis"){
    val up = Point(5, 20)
    val down = Point(10,10)
    assert(up.fromY(down) == 10)
    assert(down.fromY(up) == 10)
  }
}
