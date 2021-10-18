package model

import utility.Constants.DefaultFoodQuantity
import utility.RectangleArea
import view.Rectangle

import java.awt.Color
import scala.util.Random

/**
 * The possible types of area.
 */
sealed trait AreaType
case object Fertile extends AreaType
case object Water extends AreaType
case object Rock extends AreaType
case object Volcano extends AreaType

//TODO scaladoc
sealed trait Area {
  val name: String
  val areaType: AreaType
  val area: Rectangle

  /**
   * Method to check if a [[Point]] is inside the [[Area]].
   *
   * @param p the [[Point]] to check.
   * @return true if the [[Area]] contains the [[Point]], false otherwise.
   */
  def contains(p: Point): Boolean = area.contains(p)
}

//TODO scaladoc
object Area {

  //TODO scaladoc
  def apply(areaType: AreaType, area: RectangleArea, probability: Probability = Probability(0), name: String = ""): Area =
    areaType match {
      case Fertile => FertileAreaGrowFood(new Rectangle(area, Color.green), probability, name)
      case Water => SimpleAreaImpl(new Rectangle(area, Color.blue), if (name == "") "a bit of water" else name, areaType)
      case Rock => SimpleAreaImpl(new Rectangle(area, new Color(102, 51, 0)), if (name == "") "a rock area" else name, areaType)
      case Volcano => SimpleAreaImpl(new Rectangle(area, Color.red), if (name == "") "a volcano" else name, areaType)
    }

  private case class SimpleAreaImpl(override val area: Rectangle,
                                    override val name: String,
                                    override val areaType: AreaType,
                                   ) extends Area

  private case class FertileAreaGrowFood(override val area: Rectangle,
                                         override val fertility: Probability,
                                         override val name: String = "a fertile area",
                                         override val areaType: AreaType = Fertile,
                                        ) extends Area with GrowFood {
    require(areaType == Fertile)

    override def growFood(optFood: Option[Food]): Option[FoodInstance] = {
      if (optFood.isDefined) {
        val food = optFood.get
        if (fertility.calculate) {
          val random = new Random
          val _1 = random.between(area.topLeft.x, area.bottomRight.x)
          val _2 = random.between(area.topLeft.y, area.bottomRight.y)

          val quantity = random.nextInt(DefaultFoodQuantity)
          return Some(Vegetable(quantity, Point(_1, _2), food.energy, food.color))
        }
      }
      None
    }
  }
}

//TODO scaladoc
sealed trait GrowFood {
  val fertility: Probability
  // TODO: make it return a food (possibly from foods if the probability is true

  /**
   * todo spiegazione
   *
   * @param food the [[Food]] that can grow.
   * @return an optional of [[FoodInstance]].
   */
  def growFood(food: Option[Food]): Option[FoodInstance]
}