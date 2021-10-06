package utility

case class RectangleArea(topLeft: Point, bottomRight: Point) {

  require(isValid, "Illegal corners")
  def width: Int = bottomRight.x - topLeft.x
  def height: Int = bottomRight.y - topLeft.y
  def isValid: Boolean = topLeft.x < bottomRight.x && topLeft.y < bottomRight.y
  def overlap(otherArea: RectangleArea): Boolean =
      topLeft.x < otherArea.bottomRight.x &&
      bottomRight.x > otherArea.topLeft.x &&
      topLeft.y < otherArea.bottomRight.y &&
      bottomRight.y > otherArea.topLeft.y

  def contains(p: Point): Boolean = p.isLeft(bottomRight) && p.isRight(topLeft)&& p.isUnder(topLeft) && p.isOver(bottomRight)


  def getIn4Quadrant(size: (Int, Int)) : Seq[RectangleArea] = {
    var rectangles = Seq.empty[RectangleArea]
    val max = Point(size._1 / 4, size._2 / 4)
    val center = Point(max.x * 2, max.y * 2)
    val end = Point(size._1, size._2)
    val centerFirstQuadrant = Point(max.x, max.y)
    val centerSecondQuadrant = Point(max.x*3, max.y)
    val centerThirdQuadrant = Point(max.x, max.y * 3)
    val centerFourthQuadrant = Point(max.x*3, max.y*3)
    val p = Point(0,0)

    //Alto a sx
    rectangles = rectangles :+ RectangleArea(
      p.getRandomPoint(p, centerFirstQuadrant),
      p.getRandomPoint(centerFirstQuadrant, center)
    )

    //Alto a dx
    rectangles = rectangles :+ RectangleArea(
      p.getRandomPoint(Point(center.x, 0), centerSecondQuadrant),
      p.getRandomPoint(centerSecondQuadrant, Point(end.x, center.y))
    )

    //Basso a sx
    rectangles = rectangles :+ RectangleArea(
      p.getRandomPoint(Point(0, center.y), centerThirdQuadrant),
      p.getRandomPoint(centerThirdQuadrant, Point(center.x, end.y))
    )

    //Basso a dx
    rectangles = rectangles :+ RectangleArea(
      p.getRandomPoint(center, centerFourthQuadrant),
      p.getRandomPoint(centerFourthQuadrant, end)
    )

    rectangles
  }
}
