package controller.manager

import controller.manager.Aliases.Foods
import model._
import model.food.{Food, FoodType, Vegetable}
import model.habitat.{Area, Fertile, GrowFood, Habitat}
import utility.Constants
import utility.serializer.{OfFood, Serializer}

import scala.annotation.tailrec
import scala.util.Random

object Aliases {
  /**
   * a [[Seq]] of [[Food]]
   */
  type Foods = Seq[Food]
}

sealed trait ResourceManager {

  import Aliases._

  /**
   *
   * @return the [[Foods]] in the ResourceManager
   */
  def foods: Foods

  /**
   *
   * @param f replace the [[Seq]] of [[Food]] to the [[ResourceManager]]
   * @return the updated [[ResourceManager]]
   */
  def foods_(f: Foods): ResourceManager

  /**
   *
   * @return the Set of foodTypes that the [ResourceManager] can grow
   */
  def foodTypes: Set[FoodType]

  /**
   *
   * @return a [[ResourceManager]] with fixed amount of [[Food]] if there isa at least one [[Area]] with fertility > 0
   */
  def fillHabitat(): ResourceManager

  /**
   *
   * @param fileName of the resource with Foods
   * @return a [[ResourceManager]] with [[Seq]] of [[Food]] read from file
   */
  def importFoodTypesFromFile(fileName: String): ResourceManager

  /**
   * Writes Seq of [[FoodType]] of the current [[ResourceManager]] to file
   *
   * @param filename of the resource in which the foodTypes wil be saved
   */
  def writeFoodTypesToFile(filename: String): Unit

  /**
   * Grow foodTypes in the current habitat
   *
   * @return new ResourceManager with updated fields
   */
  def grow(): ResourceManager
}

object ResourceManager {
  /**
   * The apply for [[ResourceManager]]
   *
   * @param habitat       the [[Habitat]] in which the Resources are placed
   * @param foodTypes         the [[Seq]] of [[FoodType]] that can be placed in the map from the [[ResourceManager]]
   * @param foods the actual [[Food]]
   * @return
   */
  def apply(habitat: Habitat,
            foodTypes: Set[FoodType] = Set.empty[FoodType],
            foods: Foods = Seq.empty[Food],
           ): ResourceManager = new ResourceManagerImpl(habitat, foodTypes, foods)

  /**
   * The apply for [[ResourceManager]]
   *
   * @param habitat  the [[Habitat]] in which the Resources are placed
   * @param fileName the name of the file of the [[FoodType]]
   * @return a [[ResourceManager]] implementation
   */
  def apply(habitat: Habitat, fileName: String): ResourceManager =
    new ResourceManagerImpl(habitat, Set.empty[FoodType], Seq.empty[Food]).importFoodTypesFromFile(fileName)

  private class ResourceManagerImpl(val habitat: Habitat,
                                    val foodTypes: Set[FoodType],
                                    val foods: Foods,
                                   ) extends ResourceManager {

    override def grow(): ResourceManager = {
      if (foods.count(_.foodType.foodCategory == Vegetable) < Constants.MaxFoods){
        val newFoods = habitat.areas
          .filter(_.isInstanceOf[Area with GrowFood])
          .map(_.asInstanceOf[Area with GrowFood])
          .map(_.growFood(randomVegetable()))
          .filter(_.isDefined)
          .map(_.get)
        ResourceManager(habitat, foodTypes.filter(_.foodCategory == Vegetable), foods ++ newFoods)
      } else this
    }

    /**
     *
     * @return a random [[FoodType]] from foodTypes
     */
    private def randomVegetable(): Option[FoodType] = {
      if (foodTypes.nonEmpty) {
        Some(foodTypes.filter(_.foodCategory == Vegetable).toSeq(new Random().nextInt(foodTypes.size)))
      } else None
    }

    override def importFoodTypesFromFile(fileName: String): ResourceManager = {
      val serializer: Serializer = Serializer(OfFood)
      val growableFoods = serializer.deserializeManyFromFile(fileName)(classOf[FoodType])
      ResourceManager(habitat, growableFoods.toSet, foods)
    }

    override def writeFoodTypesToFile(filename: String): Unit = {
      val serializer: Serializer = Serializer(OfFood)
      serializer.serializeManyToFile(foodTypes)(Constants.FoodsFilePath)
    }

    override def fillHabitat(): ResourceManager = {
      @tailrec
      def _fillHabitat(resourceManager: ResourceManager): ResourceManager = {
        if (resourceManager.foods.lengthCompare(Constants.InitialFoods) > 0)
          resourceManager
        else _fillHabitat(resourceManager.grow())
      }
      //if there isn't at least one Fertile area with fertility > 0 no foodTypes are produced and infinite loops occur
      if (habitat.areas
        .filter(_.areaType == Fertile)
        .map(_.asInstanceOf[Area with GrowFood])
        .count(_.fertility == Probability(0)) == habitat.areas.count(_.areaType == Fertile)) this
      else _fillHabitat(this)
    }

    override def foods_(f: Foods): ResourceManager =
      ResourceManager(habitat, foodTypes, f)
  }
}