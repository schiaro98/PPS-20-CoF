package controller.manager

import controller.manager.Aliases.SomeFoods
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
  type SomeFoods = Seq[Food]
}

sealed trait ResourceManager {

  import Aliases._

  /**
   *
   * @return the [[SomeFoods]] in the ResourceManager
   */
  def someFoods: SomeFoods

  /**
   *
   * @param fi replace the [[Seq]] of [[Food]] to the [[ResourceManager]]
   * @return the updated [[ResourceManager]]
   */
  def someFoods_(fi: SomeFoods): ResourceManager

  /**
   *
   * @return the Set of foods that the [ResourceManager] can grow
   */
  def foods: Set[FoodType]

  /**
   *
   * @return a [ResourceManager] with fixed amount of [FoodInstnce] if there isa at least one [Area] with fertility > 0
   */
  def fillHabitat(): ResourceManager

  /**
   *
   * @param fileName of the resource with Foods
   * @return a [ResourceManager] with Seq[Food] read from file
   */
  def importFoodsFromFile(fileName: String): ResourceManager

  /**
   * Writes Seq of [Food] of the current [ResourceManager] to file
   *
   * @param filename of the resource in which the foods wil be saved
   */
  def writeFoodsToFile(filename: String): Unit

  /**
   * Grow foods in the current habitat
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
   * @param foods         the [[Seq]] of [[FoodType]] that can be placed in the map from the [[ResourceManager]]
   * @param foodInstances the actual [[Food]]
   * @return
   */
  def apply(habitat: Habitat,
            foods: Set[FoodType] = Set.empty[FoodType],
            foodInstances: SomeFoods = Seq.empty[Food],
           ): ResourceManager = new ResourceManagerImpl(habitat, foods, foodInstances)

  /**
   * The apply for [[ResourceManager]]
   *
   * @param habitat  the [[Habitat]] in which the Resources are placed
   * @param fileName the name of the file of the [[FoodType]]
   * @return a [[ResourceManager]] implementation
   */
  def apply(habitat: Habitat, fileName: String): ResourceManager =
    new ResourceManagerImpl(habitat, Set.empty[FoodType], Seq.empty[Food]).importFoodsFromFile(fileName)

  private class ResourceManagerImpl(val habitat: Habitat,
                                    val foods: Set[FoodType],
                                    val someFoods: SomeFoods,
                                   ) extends ResourceManager {

    override def grow(): ResourceManager = {
      if (someFoods.count(_.foodCategory == Vegetable) < Constants.MaxFoodInstances){
        val newFoods = habitat.areas
          .filter(_.isInstanceOf[Area with GrowFood])
          .map(_.asInstanceOf[Area with GrowFood])
          .map(_.growFood(randomVegetable()))
          .filter(_.isDefined)
          .map(_.get)
        ResourceManager(habitat, foods.filter(_.foodCategory == Vegetable), someFoods ++ newFoods)
      } else this
    }

    /**
     *
     * @return a random [[FoodType]] from foods
     */
    private def randomVegetable(): Option[FoodType] = {
      if (foods.nonEmpty) {
        Some(foods.filter(_.foodCategory == Vegetable).toSeq(new Random().nextInt(foods.size)))
      } else None
    }

    override def importFoodsFromFile(fileName: String): ResourceManager = {
      val serializer: Serializer = Serializer(OfFood)
      val growableFoods = serializer.deserializeManyFromFile(fileName)(classOf[FoodType])
      ResourceManager(habitat, growableFoods.toSet, someFoods)
    }

    override def writeFoodsToFile(filename: String): Unit = {
      val serializer: Serializer = Serializer(OfFood)
      serializer.serializeManyToFile(foods)(Constants.FoodsFilePath)
    }

    override def fillHabitat(): ResourceManager = {
      @tailrec
      def _fillHabitat(resourceManager: ResourceManager): ResourceManager = {
        if (resourceManager.someFoods.lengthCompare(Constants.InitialFoodInstances) > 0)
          resourceManager
        else _fillHabitat(resourceManager.grow())
      }
      //if there isn't at least one Fertile area with fertility > 0 no foods are produced and infinite loops occur
      if (habitat.areas
        .filter(_.areaType == Fertile)
        .map(_.asInstanceOf[Area with GrowFood])
        .count(_.fertility == Probability(0)) == habitat.areas.count(_.areaType == Fertile)) this
      else _fillHabitat(this)
    }

    override def someFoods_(fi: SomeFoods): ResourceManager =
      ResourceManager(habitat, foods, fi)
  }
}