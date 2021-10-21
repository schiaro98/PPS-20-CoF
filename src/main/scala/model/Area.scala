package model

import utility.Constants.DefaultFoodQuantity
import utility.{Constants, RectangleArea, Statistics}
import view.Rectangle

import java.awt.Color
import scala.util.Random

/**
 * The possible types of area that can be walkable or not.
 */
sealed trait AreaType {
  val walkable: Boolean = false
}
case object Fertile extends AreaType {
  override val walkable: Boolean = true
}
case object Water extends AreaType
case object Rock extends AreaType
case object Volcano extends AreaType

/**
 * The Model of an [[Area]]
 */
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

object Area {

  /**
   * Method to obtain all the possible [[AreaType]].
   *
   * @return a Set with all the possible [[AreaType]].
   */
  def getAllTypeOfArea = Set(Fertile, Water, Rock, Volcano)

  /**
   * Apply method for [[Area]].
   *
   * @param areaType    the [[AreaType]].
   * @param area        the [[Rectangle]] which delimits the [[Area]].
   * @param probability the [[Probability]] that determines the fertility of a [[Fertile]] area.
   * @param name        the name of the [[Area]].
   * @return an implementation of [[Area]].
   */
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
          val _2 = random.between(area.topLeft.y - Constants.PixelForFood, area.bottomRight.y - Constants.PixelForFood)

          val quantity = random.nextInt(DefaultFoodQuantity)
          Statistics.update(foods = quantity)
          return Some(FoodInstance(food, Point(_1, _2), quantity))
        }
      }
      None
    }
  }
}

/**
 * Trait that makes it possible to grow food.
 */
sealed trait GrowFood {
  val fertility: Probability

  /**
   * Given a food calculate the probability of its growth.
   *
   * @param food the [[Food]] that can grow.
   * @return an [[Option]] of [[FoodInstance]].
   */
  def growFood(food: Option[Food]): Option[FoodInstance]
}