package controller

import model.{Animal, Habitat, Point}
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants
import view.LogicGui

class GameLoopTest extends AnyFunSuite{

  val logic = new LogicGui(Constants.SavedSpecies)
  val habitat: Habitat = Habitat()
  logic.initialize()
  var animalManager: AnimalManager = AnimalManager().generateInitialAnimals(logic.species, habitat)
  var resourceManager: ResourceManager = ResourceManager(habitat, Constants.FoodsFilePath)

  test("Basic loop should whork"){

    //TODO rivedere bene dopo l'aggiunta del AnimalManager

    var i = 0
    while (animalManager.animals.lengthIs > 0 && i < 100) {
      println(i)
      i+=1
      val destinationManager: DestinationManager = DestinationManager(animalManager.animals, resourceManager.foodInstances, habitat)
      val destinations: Map[Animal, Point] = destinationManager.calculateDestination()
      val shiftManager = ShiftManager(habitat, destinations)

      shiftManager.walk()
      animalManager = AnimalManager(shiftManager.animals.toSeq)

      val feedManager = FeedManager(animalManager.animals, resourceManager.foodInstances)

      val (animalsUpdated, foodsRemaining) = feedManager.consumeResources()
      animalManager = AnimalManager(animalsUpdated)
      animalManager = animalManager.lifeCycleUpdate()

      val battleManager: BattleManager = BattleManager(animalManager.animals)
      val result = battleManager.battle()
      animalManager = AnimalManager(result._1)
      val oldFood = resourceManager.foodInstances
      resourceManager = resourceManager.foodInstances_(oldFood.filterNot((result._2 ++ foodsRemaining).contains(_)))

      //TODO Calcolo eventi inaspettati

      resourceManager = resourceManager.grow()
    }
  }
}
