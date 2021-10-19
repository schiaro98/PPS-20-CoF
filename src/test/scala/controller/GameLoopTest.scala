package controller

import model.{Animal, Habitat, Point}
import org.scalatest.funsuite.AnyFunSuite
import utility.{AnimalUtils, Constants}
import view.LogicGui

class GameLoopTest extends AnyFunSuite{


  val logic = new LogicGui(Constants.SavedSpecies)
  val habitat: Habitat = Habitat()
  logic.initialize()
  var animalsInMap: Seq[Animal] = AnimalUtils.generateInitialAnimals(logic.species, habitat)
  var resourceManager: ResourceManager = ResourceManager(habitat, Constants.FoodsFilePath)

  test("Basic loop should whork"){
    var i = 0
    while (animalsInMap.lengthIs > 0) {
      println(i)
      i+=1
      val destinationManager: DestinationManager = DestinationManager(animalsInMap, resourceManager.foodInstances, habitat)
      val destinations: Map[Animal, Point] = destinationManager.calculateDestination()
      val shiftManager = ShiftManager(habitat, destinations)

      shiftManager.walk()
      animalsInMap = shiftManager.animals.toSeq

      val feedManager = FeedManager(animalsInMap, resourceManager.foodInstances)

      val (animalsUpdated, foodsRemaining) = feedManager.consumeResources()
      animalsInMap = feedManager.lifeCycleUpdate(animalsUpdated)

      val battleManager: BattleManager = BattleManager(animalsInMap)
      val result = battleManager.battle()
      animalsInMap = result._1
      val oldFood = resourceManager.foodInstances
      resourceManager = resourceManager.foodInstances_(oldFood.filterNot((result._2 ++ foodsRemaining).contains(_)))

      //TODO Calcolo eventi inaspettati

      resourceManager = resourceManager.grow()
    }
  }
}
