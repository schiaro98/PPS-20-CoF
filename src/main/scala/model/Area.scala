package model

import utility.Constants.DefaultFoodQuantity

import scala.util.Random

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
    case Fertile =>  new FertileAreaGrowFood(topLeft, bottomRight, Probability(0), "a fertile area")
    case Water => SimpleArea(areaType, topLeft, bottomRight, "a bit of water")
    case Rock => SimpleArea(areaType, topLeft, bottomRight, "a rock area")
    case Volcano => SimpleArea(areaType, topLeft, bottomRight, "a volcano")
  }

  def apply(name: String, areaType: AreaType, topLeft: (Int, Int), bottomRight: (Int, Int)): Area = areaType match {
    case Fertile => new FertileAreaGrowFood(topLeft, bottomRight, Probability(0),  name)
    case Water => SimpleArea(areaType, topLeft, bottomRight, name)
    case Rock => SimpleArea(areaType, topLeft, bottomRight, name)
    case Volcano => SimpleArea(areaType, topLeft, bottomRight, name)
  }

  def apply(areaType: AreaType, topLeft: (Int, Int), bottomRight: (Int, Int), probability: Probability): Area = areaType match {
    case Fertile => new FertileAreaGrowFood(topLeft, bottomRight, probability, "a fertile area which can grow food")
    case Water => SimpleArea(areaType, topLeft, bottomRight, "a bit of water")
    case Rock => SimpleArea(areaType, topLeft, bottomRight, "a rock area")
    case Volcano => SimpleArea(areaType, topLeft, bottomRight, "a volcano")
  }

  def apply(topLeft: (Int, Int), bottomRight: (Int, Int), fertility: Probability
            //            , foods: Set[Food]
           ): Area =
    new FertileAreaGrowFood(topLeft, bottomRight, fertility, "a fertile area which can grow food"
      //      , foods
    )


  private case class SimpleArea(areaType: AreaType,
                                topLeft: (Int, Int),
                                bottomRight: (Int, Int),
                                name: String) extends Area

  private class FertileAreaGrowFood(topLeft: (Int, Int),
                                    bottomRight: (Int, Int),
                                    override val fertility: Probability,
                                    name: String
                                    //                                    override val foods: Set[Food]
                                   ) extends SimpleArea(Fertile, topLeft, bottomRight, name) with GrowFood {
    require(areaType == Fertile)

    override def growFood(optFood: Option[Food]): Option[FoodInstance] = {
      if (optFood.isDefined) {
        val food = optFood.get
        if (fertility.calculate) {
          val random = new Random
          val _1 = topLeft._1 + random.nextInt((bottomRight._1 - topLeft._1) + 1)
          val _2 = topLeft._2 + random.nextInt((bottomRight._2 - topLeft._2) + 1)

          val quantity = random.nextInt(DefaultFoodQuantity)
          return Some(Vegetable(quantity, (_1, _2), food.energy, food.icon))
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