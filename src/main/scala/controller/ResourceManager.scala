package controller

import model.{Area, Food, GrowFood, Habitat}

import scala.annotation.tailrec
import scala.language.reflectiveCalls

trait ResourceManager {

  val habitat: Habitat
  val growableFoods: Set[Food]
  val foods: Seq[Food]

  def grow()
}

// TODO: we should make an interface CategoryOfFood, like food but without position so that we can create food dynamically
object ResourceManager {

  def apply(habitat: Habitat, growableFoods: Set[Food] = Set.empty[Food], foods: Seq[Food] = Seq.empty[Food]): ResourceManager =
    new ResourceManagerImpl(habitat, growableFoods, foods)

  class ResourceManagerImpl(override val habitat: Habitat,
                            override val growableFoods: Set[Food],
                            override val foods: Seq[Food]) extends ResourceManager {

    override def grow(): Unit = {
//      class StructType {
//        def callGrowFood(c: {def growFood()}): Unit = c.growFood()
//      }

      @tailrec
      def growImpl(areas: Seq[Area]): Unit = areas match {
        case h :: t =>
          h match {
            case growArea: Area with GrowFood =>
              growArea.growFood()
            case _ =>
          }
          growImpl(t)
        case _ =>
      }

      growImpl(habitat.areas)
    }
  }
}