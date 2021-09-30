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

  def getIn4Quadrant(size: (Int, Int)) : Seq[RectangleArea] = {
    var rectangles = Seq.empty[RectangleArea]
    val maxX = size._1 / 4
    val maxY = size._2 / 4
    val centerX = maxX * 2
    val centerY = maxY * 2
    val endX = size._1
    val endY = size._2
    val centerFirstQuadrant = Point(maxX, maxY)
    val centerSecondQuadrant = Point(maxX*3, maxY)
    val centerThirdQuadrant = Point(maxX, maxY*3)
    val centerFourthQuadrant = Point(maxX*3, maxY*3)
    val center = Point(centerX, centerY)
    val p = Point(0,0)

    //Alto a sx
    rectangles = rectangles :+ RectangleArea(
      p.getRandomPoint(Point(0, 0), centerFirstQuadrant),
      p.getRandomPoint(centerFirstQuadrant, center)
    )
    //Alto a dx
    rectangles = rectangles :+ RectangleArea(
      p.getRandomPoint(Point(centerX, 0), centerSecondQuadrant),
      p.getRandomPoint(centerSecondQuadrant, Point(endX, centerY))
    )

    //Basso a sx
    rectangles = rectangles :+ RectangleArea(
      p.getRandomPoint(Point(0, centerY), centerThirdQuadrant),
      p.getRandomPoint(centerThirdQuadrant, Point(centerX, endY))
    )

    //Basso a dx
    rectangles = rectangles :+ RectangleArea(
      p.getRandomPoint(center, centerFourthQuadrant),
      p.getRandomPoint(centerFourthQuadrant, Point(size._1, size._2))
    )

    rectangles
  }
}
