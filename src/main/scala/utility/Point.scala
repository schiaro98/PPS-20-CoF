package utility

case class Point(x: Int, y: Int) {
   def +(add: (Int, Int)): Point = Point(x + add._1, y + add._2)

  def -(sub: (Int, Int)): Point = Point(x - sub._1, y - sub._2)

  def +(add: Point): Point = Point(x + add.x, y + add.y)

  def -(sub: Point): Point = Point(x - sub.x, y - sub.y)

  def ==(p: Point): Boolean = x == p.x && y == p.y

  def distance(p: Point): Double = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2))
}
