package model

sealed trait AreaType

case object Fertile extends AreaType

case object Water extends AreaType

case object Rock extends AreaType

case object Volcano extends AreaType

sealed trait Area {
  val name: String
  val areaType: AreaType
  val topLeft: (Int, Int)
  val bottomRight: (Int, Int)
  require(topLeft._1 < bottomRight._1 && topLeft._2 < bottomRight._2, "inserted illegal corners")
}

object Area {

  def apply(areaType: AreaType, topLeft: (Int, Int), bottomRight: (Int, Int)): Area = areaType match {
    case Fertile => FertileArea(areaType, topLeft, bottomRight)
    case Water => WaterArea(areaType, topLeft, bottomRight)
    case Rock => RockArea(areaType, topLeft, bottomRight)
    case Volcano => VolcanoArea(areaType, topLeft, bottomRight)
  }

  def apply(name: String, areaType: AreaType, topLeft: (Int, Int), bottomRight: (Int, Int)): Area = areaType match {
    case Fertile => FertileArea(areaType, topLeft, bottomRight, name)
    case Water => WaterArea(areaType, topLeft, bottomRight, name)
    case Rock => RockArea(areaType, topLeft, bottomRight, name)
    case Volcano => VolcanoArea(areaType, topLeft, bottomRight, name)
  }

  private case class FertileArea(areaType: AreaType,
                                 topLeft: (Int, Int),
                                 bottomRight: (Int, Int),
                                 name: String = "a fertile area") extends Area

  private case class WaterArea(areaType: AreaType,
                               topLeft: (Int, Int),
                               bottomRight: (Int, Int),
                               name: String = "a bit of water") extends Area

  private case class RockArea(areaType: AreaType,
                                 topLeft: (Int, Int),
                                 bottomRight: (Int, Int),
                                 name: String = "a rock area") extends Area

  private case class VolcanoArea(areaType: AreaType,
                               topLeft: (Int, Int),
                               bottomRight: (Int, Int),
                               name: String = "a volcano") extends Area
}
