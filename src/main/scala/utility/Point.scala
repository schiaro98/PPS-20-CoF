package utility

case class Point(x: Int, y: Int) {
   def +(add: (Int, Int)): Point = Point(this.x + add._1, this.y + add._2)

  def -(sub: (Int, Int)): Point = Point(this.x - sub._1, this.y - sub._2)

  def ==(p: Point): Boolean = this.x == p.x && this.y == p.y
}
