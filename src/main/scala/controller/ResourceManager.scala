package controller

import controller.Aliases.FoodInstances
import model.{Area, Food, FoodInstance, GrowFood, Habitat}

import scala.annotation.tailrec
import scala.language.reflectiveCalls
import scala.util.Random

// TODO: this may be not necessary but it's cool
object Aliases {
  type FoodInstances = Seq[FoodInstance]
}

trait ResourceManager {

  import Aliases._

  val habitat: Habitat
  val growableFoods: Set[Food]
  val foods: FoodInstances

  def grow(): ResourceManager
}

object ResourceManager {

  def apply(habitat: Habitat, growableFoods: Set[Food] = Set.empty[Food], foods: FoodInstances = Seq.empty[FoodInstance]): ResourceManager =
    new ResourceManagerImpl(habitat, growableFoods, foods)

  class ResourceManagerImpl(override val habitat: Habitat,
                            override val growableFoods: Set[Food],
                            override val foods: FoodInstances) extends ResourceManager {

    private def randomFood(): Option[Food] = {
      @tailrec
      def randomFoodImpl(set: Set[Food])(n: Int): Food = if (n == 0) set.head else randomFoodImpl(set.tail)(n - 1)

      if (growableFoods.nonEmpty) {
        Some(randomFoodImpl(growableFoods)(new Random().nextInt(growableFoods.size)))
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

  }
}