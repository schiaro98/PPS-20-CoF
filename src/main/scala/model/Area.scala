package model

import utility.Constants.DefaultFoodQuantity
import utility.{Point, RectangleArea}

import java.awt.Color
import scala.util.Random

sealed trait AreaType

case object Fertile extends AreaType

case object Water extends AreaType

case object Rock extends AreaType

case object Volcano extends AreaType

sealed trait Area {
  val name: String
  val color: Color
  val areaType: AreaType
  val area: RectangleArea

  def contains(p: Point): Boolean = area.contains(p)
}

object Area {
  def apply(areaType: AreaType, area: RectangleArea, probability: Probability = Probability(0), name: String = ""): Area =
    areaType match {
    case Fertile => FertileAreaGrowFood(area, probability, name)
    case Water => SimpleAreaImpl(area, if (name == "") "a bit of water" else name, Color.blue,areaType )
    case Rock => SimpleAreaImpl(area, if (name == "") "a rock area" else name, new Color(102,51,0), areaType)
    case Volcano => SimpleAreaImpl(area, if (name == "") "a volcano" else name, Color.red, areaType)
  }

  private case class SimpleAreaImpl(override val area: RectangleArea,
                                    override val name: String,
                                    override val color: Color,
                                    override val areaType: AreaType) extends Area

  private case class FertileAreaGrowFood(override val area: RectangleArea,
                                    override val fertility: Probability,
                                    override val name: String = "a fertile area",
                                    override val color: Color = Color.green,
                                    override val areaType: AreaType = Fertile
//                                    override val foods: Set[Food]
                                   ) extends Area with GrowFood {
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