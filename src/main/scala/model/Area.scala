package model

import utility.Constants.DefaultFoodQuantity
import utility.{Point, RectangleArea}

import scala.util.Random

sealed trait AreaType

case object Fertile extends AreaType

case object Water extends AreaType

case object Rock extends AreaType

case object Volcano extends AreaType

sealed trait Area {
  val name: String
  val areaType: AreaType
  val area: RectangleArea
  require(area.isValid, "inserted illegal corners")
}

object Area {
  def apply(areaType: AreaType, area: RectangleArea): Area = areaType match {
    case Fertile =>  new FertileAreaGrowFood(area, Probability(0), "a fertile area")
    case Water => SimpleArea(areaType, area, "a bit of water")
    case Rock => SimpleArea(areaType, area, "a rock area")
    case Volcano => SimpleArea(areaType, area, "a volcano")
  }

  def apply(name: String, areaType: AreaType, area: RectangleArea): Area = areaType match {
    case Fertile => new FertileAreaGrowFood(area, Probability(0),  name)
    case Water => SimpleArea(areaType, area, name)
    case Rock => SimpleArea(areaType, area, name)
    case Volcano => SimpleArea(areaType, area, name)
  }

  def apply(areaType: AreaType, area: RectangleArea, probability: Probability): Area = areaType match {
    case Fertile => new FertileAreaGrowFood(area, probability, "a fertile area which can grow food")
    case Water => SimpleArea(areaType, area, "a bit of water")
    case Rock => SimpleArea(areaType, area, "a rock area")
    case Volcano => SimpleArea(areaType, area, "a volcano")
  }

  def apply(area: RectangleArea, fertility: Probability
            //            , foods: Set[Food]
           ): Area =
    new FertileAreaGrowFood(area, fertility, "a fertile area which can grow food"
      //      , foods
    )


  private case class SimpleArea(areaType: AreaType,
                                area: RectangleArea,
                                name: String) extends Area

  private class FertileAreaGrowFood(area: RectangleArea,
                                    override val fertility: Probability,
                                    name: String
                                    //                                    override val foods: Set[Food]
                                   ) extends SimpleArea(Fertile, area: RectangleArea, name) with GrowFood {
    require(areaType == Fertile)

    override def growFood(optFood: Option[Food]): Option[FoodInstance] = {
      if (optFood.isDefined) {
        val food = optFood.get
        if (fertility.calculate) {
          val random = new Random
          val _1 = area.topLeft.x + random.nextInt((area.bottomRight.x - area.topLeft.x) + 1)
          val _2 = area.topLeft.y + random.nextInt((area.bottomRight.y - area.topLeft.y) + 1)

          val quantity = random.nextInt(DefaultFoodQuantity)
          return Some(Vegetable(quantity, Point(_1, _2), food.energy, food.icon))
        }
      }
      None
    }
  }

  def randomType: AreaType = {
    val choices: Seq[AreaType] = Seq(Fertile, Water, Rock, Volcano)
    Random.shuffle(choices).head
  }
}

sealed trait GrowFood {
  val fertility: Probability
  //  val foods: Set[Food]
  // TODO: make it return a food (possibly from foods if the probability is true

  def growFood(food: Option[Food]): Option[FoodInstance]
}