package controller

import controller.Aliases.FoodInstances
import model._
import utility.{Constants, OfFood, Serializer}

import scala.annotation.tailrec
import scala.util.Random

object Aliases {
  type FoodInstances = Seq[FoodInstance]
}

sealed trait ResourceManager {

  import Aliases._

  /**
   *
   * @return the FoodInstnces in the ResourceManager
   */
  def foodInstances: FoodInstances

  def foodInstances_(fi:FoodInstances) : ResourceManager

  /**
   *
   * @return the Set of foods that the [ResourceManager] can grow
   */
  def foods: Set[Food]

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

  def apply(habitat: Habitat,
            growableFoods: Set[Food] = Set.empty[Food],
            foodInstances: FoodInstances = Seq.empty[FoodInstance],
           ): ResourceManager = new ResourceManagerImpl(habitat, growableFoods, foodInstances)

  def apply(habitat: Habitat, fileName: String): ResourceManager =
    new ResourceManagerImpl(habitat, Set.empty[Food], Seq.empty[FoodInstance]).importFoodsFromFile(fileName)

  private class ResourceManagerImpl(val habitat: Habitat,
                                    val foods: Set[Food],
                                    val foodInstances: FoodInstances,
                                   ) extends ResourceManager {

    private def randomFood(): Option[Food] = {
      if (foods.nonEmpty) {
        Some(foods.toSeq(new Random().nextInt(foods.size)))
      } else None
    }

    override def grow(): ResourceManager = {
      val newFoods = habitat.areas
        .filter(_.isInstanceOf[Area with GrowFood])
        .map(_.asInstanceOf[Area with GrowFood])
        .map(_.growFood(randomFood()))
        .filter(_.isDefined)
        .map(_.get)
      ResourceManager(habitat, foods.filter(_.foodType==VegetableType), foodInstances ++ newFoods)
    }

    override def importFoodsFromFile(fileName: String): ResourceManager = {
      val serializer: Serializer = Serializer(OfFood)
      val growableFoods = serializer.deserializeManyFromFile(fileName)(classOf[Food])
      ResourceManager(habitat, growableFoods.toSet, foodInstances)
    }


    override def writeFoodsToFile(filename: String): Unit = {
      val serializer: Serializer = Serializer(OfFood)
      serializer.serializeManyToFile(foods)(Constants.FoodsFilePath)
    }

    override def fillHabitat(): ResourceManager = {
      @tailrec
      def _fillHabitat(resourceManager: ResourceManager): ResourceManager = {
        if (resourceManager.foodInstances.size > habitat.areas.count(a => a.areaType == Fertile) * 10)
          resourceManager
        else _fillHabitat(resourceManager.grow())
      }
      //if there isn't at least one Fertile area with fertility > 0 no foods are produced and infinite loops occur
      if (habitat.areas
        .filter(_.areaType == Fertile)
        .map(_.asInstanceOf[Area with GrowFood])
        .count(_.fertility == Probability(0)) == habitat.areas.count(_.areaType==Fertile))  this
      else _fillHabitat(this)
    }

    override def foodInstances_(fi: FoodInstances) : ResourceManager =
      ResourceManager(habitat, foods, fi ++ foodInstances)
  }
}