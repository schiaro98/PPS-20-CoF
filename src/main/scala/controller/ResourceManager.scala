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
  def foods: FoodInstances

  /**
   *
   * @return the Set of foods that the ResourceManager can grow
   */
  def growableFoods: Set[Food]

  /**
   *
   * @return a ResourceManager with fixed amount of FoodInstnce
   */
  def fillHabitat(): ResourceManager

  /**
   * Creates an Habitat with foods written on some file
   *
   * @param fileName of the resource with foods
   * @return a ResourceManager with Foods
   */
  def importFoodsFromFile(fileName: String): ResourceManager

  /**
   * Writes foods of the current ResourceManager to file
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
            foods: FoodInstances = Seq.empty[FoodInstance],
           ): ResourceManager = new ResourceManagerImpl(habitat, growableFoods, foods)

  def apply(habitat: Habitat, fileName: String): ResourceManager =
    new ResourceManagerImpl(habitat, Set.empty[Food], Seq.empty[FoodInstance]).importFoodsFromFile(fileName)

  private class ResourceManagerImpl(val habitat: Habitat,
                                    val growableFoods: Set[Food],
                                    val foods: FoodInstances,
                                   ) extends ResourceManager {

    private def randomFood(): Option[Food] = {
      if (growableFoods.nonEmpty) {
        Some(growableFoods.toSeq(new Random().nextInt(growableFoods.size)))
      } else None
    }

    override def grow(): ResourceManager = {
      val newFoods = habitat.areas
        .filter(area => area.isInstanceOf[Area with GrowFood])
        .map(a => a.asInstanceOf[Area with GrowFood])
        .map(gfa => gfa.growFood(randomFood()))
        .filter(optFood => optFood.isDefined)
        .map(optFood => optFood.get)
      ResourceManager(habitat, growableFoods, foods ++ newFoods)
    }

    override def importFoodsFromFile(fileName: String): ResourceManager = {
      val serializer: Serializer = Serializer(OfFood)
      val growableFoods = serializer.deserializeManyFromFile(fileName)(classOf[Food])
      ResourceManager(habitat, growableFoods.toSet, foods)
    }


    override def writeFoodsToFile(filename: String): Unit = {
      val serializer: Serializer = Serializer(OfFood)
      serializer.serializeManyToFile(growableFoods)(Constants.FoodsFilePath)
    }

    override def fillHabitat(): ResourceManager = {
      @tailrec
      def _fillHabitat(resourceManager: ResourceManager): ResourceManager = {
        if (resourceManager.foods.size > habitat.areas.count(a => a.areaType == Fertile) * 10)
          resourceManager
        else _fillHabitat(resourceManager.grow())
      }

      _fillHabitat(this)
    }


  }
}