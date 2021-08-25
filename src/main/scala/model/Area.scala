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

sealed trait WalkableArea extends Area {
  val fertility: Probability
  val hospitality: Probability
}

object WalkableArea {
  def apply(dimensions: (Int, Int),
            position: (Int, Int),
            fertility: Probability,
            hospitality: Probability): WalkableArea =
    new SimpleWalkableArea(dimensions = dimensions, position = position, fertility = fertility, hospitality = hospitality)

  def apply( name: String,
             dimensions: (Int, Int),
             position: (Int, Int),
             fertility: Probability,
             hospitality: Probability): WalkableArea =
    new SimpleWalkableArea(name, dimensions, position, fertility, hospitality)

  private class SimpleWalkableArea( override val name: String = "Simple walkable area",
                                    override val dimensions: (Int, Int),
                                    override val position: (Int, Int),
                                    override val fertility: Probability,
                                    override val hospitality: Probability) extends WalkableArea {}
}


sealed trait TypeOfNonWalkableArea
case object Water extends TypeOfNonWalkableArea
case object Rock extends TypeOfNonWalkableArea
case object Volcano extends TypeOfNonWalkableArea


sealed trait NonWalkableArea extends Area {
  val typeOfNonWalkableArea: TypeOfNonWalkableArea
}

object NonWalkableArea {

  def apply(dimensions: (Int, Int),
            position: (Int, Int),
            typeOfNonWalkableArea: TypeOfNonWalkableArea): NonWalkableArea =
    new SimpleNonWalkableArea(dimensions = dimensions, position = position,typeOfNonWalkableArea = typeOfNonWalkableArea)

  def apply(name: String,
              dimensions: (Int, Int),
              position: (Int, Int),
              typeOfNonWalkableArea: TypeOfNonWalkableArea): NonWalkableArea =
      new SimpleNonWalkableArea(name, dimensions, position, typeOfNonWalkableArea)

  private class SimpleNonWalkableArea( override val name: String = "Simple non walkable area",
                                       override val dimensions: (Int, Int),
                                       override val position: (Int, Int),
                                       override val typeOfNonWalkableArea: TypeOfNonWalkableArea) extends NonWalkableArea {}
}