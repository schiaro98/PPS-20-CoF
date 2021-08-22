package model

import model.TypeOfArea.{Dangerous, Fertile}

trait TypeOfArea

object TypeOfArea {
  case object Fertile extends TypeOfArea
  case object Dangerous extends TypeOfArea
}

trait Area {

  val name: String
  val dimensions: (Int, Int)
  val position: (Int, Int)
}

object Area {
  def apply(
             typeOfArea: TypeOfArea,
             name: String,
             dimensions: (Int, Int),
             position: (Int, Int),
             fertility: Int = 30,
             hospitality: Int = 50,
             danger: Int = 10): Area = typeOfArea match {
    case Fertile => FertileArea(name, dimensions, position, fertility, hospitality)
    case Dangerous => DangerousArea(name,dimensions, position, danger)
    case _ => SimpleArea(name, dimensions, position)
  }

  private case class SimpleArea(name: String ,
                                dimensions: (Int, Int),
                                position: (Int, Int)) extends Area {

  }

  private case class FertileArea (name: String,
                                  dimensions: (Int, Int),
                                  position: (Int, Int),
                                  fertility: Int,
                                  hospitality: Int) extends Area {

    require(fertility >= 0 && fertility <= 100)
    require(hospitality >= 0 && hospitality <=100)
  }

  private case class DangerousArea(name: String,
                                   dimensions: (Int, Int),
                                   position: (Int, Int) = (5, 5),
                                   danger: Int) extends Area {

    require(danger >= 0 && danger <= 100)
  }
}



