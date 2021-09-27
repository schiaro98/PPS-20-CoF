package utility

case class RectangleArea(topLeft: Point, bottomRight: Point) {

  require(isValid, "Illegal corners")
  def width: Int = bottomRight.x - topLeft.x
  def height: Int = bottomRight.y - topLeft.y
  def isValid: Boolean = topLeft.x < bottomRight.x && topLeft.y < bottomRight.y
  def overlap(otherArea: RectangleArea): Boolean = {
    if (this.topLeft.x < otherArea.bottomRight.x &&
      this.bottomRight.x > otherArea.topLeft.x &&
      this.topLeft.y < otherArea.bottomRight.y &&
      this.bottomRight.y > otherArea.topLeft.y) true
    else false
  }
}
