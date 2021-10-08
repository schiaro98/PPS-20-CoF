package utility

import scala.util.Random

case class Point(x: Int, y: Int) {

  def +(add: Point): Point = Point(x + add.x, y + add.y)

  def -(sub: Point): Point = Point(x - sub.x, y - sub.y)

  def +(add: (Int, Int)): Point = Point(x + add._1, y + add._2)

  def -(sub: (Int, Int)): Point = Point(x - sub._1, y - sub._2)

  def ==(p: Point): Boolean = x == p.x && y == p.y

  /**
   * @param p the point of reference
   * @return true if the other point has a lower y
   */
  def isUnder(p:Point):Boolean = p.y < y

  /**
   * @param p the point of reference
   * @return true if the other point has a greater y
   */
  def isOver(p:Point): Boolean = !isUnder(p)

  /**
   * @param p the point of reference
   * @return true if the other point has a lower x
   */
  def isRight(p:Point): Boolean = p.x < x

  /**
   * @param p the point of reference
   * @return true if the other point has a greater x
   */
  def isLeft(p:Point):Boolean = !isRight(p)

  /**
   * @param p the point of reference
   * @return distance from another point
   */
  def distance(p: Point): Double = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2))

  /**
   * @param limits are the point max coordinates
   * @return a random point, between (0,0) and limits
   */
  def getRandomPoint(limits: Point): Point = getRandomPoint(Point(0,0), limits)

  /**
   * @param start are the point min coordinates
   * @param ends are the point max coordinates
   * @return a random point, between start and ends
   */
  def getRandomPoint(start: Point, ends: Point) : Point = {
    require(ends.x > start.x, "Illegal random point x")
    require(ends.y > start.y, "Illegal random point y")
    Point(Random.between(start.x, ends.x), Random.between(start.y, ends.y))
  }

  /**
   * Calculate distance upon x axis
   * @param p the point from which to calculate the distance
   * @return the distance upon the x axis
   */
  def fromX(p: Point): Int = Math.abs(p.x - x)

  /**
   * Calculate distance upon y axis
   * @param p the point from which to calculate the distance
   * @return the distance upon the y axis
   */
  def fromY(p: Point): Int = Math.abs(p.y - y)
}
