package model

trait Area {

  val name: String
  val dimensions: (Int, Int)
  val position: (Int, Int)
}

object Area {
  def apply(
             name: String,
             dimensions: (Int, Int),
             position: (Int, Int)): Area = SimpleArea(name, dimensions, position)

  private case class SimpleArea(  name: String ,
                                  dimensions: (Int, Int),
                                  position: (Int, Int)) extends Area {
  }
}

trait WalkableArea extends Area {
  val fertility: Probability
  val hospitality: Probability
}

object WalkableArea {
  def apply(
             name: String,
             dimensions: (Int, Int),
             position: (Int, Int),
             fertility: Probability,
             hospitality: Probability): WalkableArea = new SimpleWalkableArea(name, dimensions, position,fertility, hospitality)

  private class SimpleWalkableArea( override val name: String = "Walkable area",
                                    override val dimensions: (Int, Int),
                                    override val position: (Int, Int),
                                    override val fertility: Probability,
                                    override val hospitality: Probability) extends WalkableArea {

  }
}

trait NonWalkableArea extends Area {

}
