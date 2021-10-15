package controller

import controller.Aliases.FoodInstances
import model.{Area, Food, FoodInstance, GrowFood, Habitat}
import utility.{Constants, OfFood, Serializer}

import scala.util.Random

object Aliases {
  type FoodInstances = Seq[FoodInstance]
}

sealed trait ResourceManager {

  import Aliases._

  val habitat: Habitat
  val growableFoods: Set[Food]
  val foods: FoodInstances

  /**
   * Creates an Habitat with foods written on some file
   * @param fileName of the resource with foods
   * @return a ResourceManager with Foods
   */
  def importFoodsFromFile(fileName: String): ResourceManager

  /**
   * Writes foods of the current ResourceManager to file
   * @param filename of the resource in which the foods wil be saved
   */
  def writeFoodsToFile(filename: String): Unit

  /**
   * Grow foods in the current habitat
   * @return new ResourceManager with updated fields
   */
  def grow(): ResourceManager
}

object ResourceManager {

  def apply(habitat: Habitat,
            growableFoods: Set[Food] = Set.empty[Food],
            foods: FoodInstances = Seq.empty[FoodInstance]): ResourceManager =
    new ResourceManagerImpl(habitat, growableFoods, foods)

  def apply(habitat: Habitat, fileName: String): ResourceManager =
    new ResourceManagerImpl(habitat,Set.empty[Food], Seq.empty[FoodInstance]).importFoodsFromFile(fileName)

  class ResourceManagerImpl(override val habitat: Habitat,
                            override val growableFoods: Set[Food],
                            override val foods: FoodInstances) extends ResourceManager {



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
      ResourceManager(habitat, growableFoods, newFoods)
    }

    override def importFoodsFromFile(fileName: String): ResourceManager ={
      val serializer: Serializer = Serializer(OfFood)
      val growableFoods = serializer.deserializeManyFromFile(fileName)(classOf[Food])
      ResourceManager(habitat, growableFoods.toSet, foods)
    }


    override def writeFoodsToFile(filename: String): Unit = {
      val serializer: Serializer = Serializer(OfFood)
      serializer.serializeManyToFile(growableFoods)(Constants.FoodsFilePath)
    }
  }
}