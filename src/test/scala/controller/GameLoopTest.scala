package controller

import model.{Animal, Habitat, Point}
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants
import view.LogicGui

class GameLoopTest extends AnyFunSuite{

  val habitat: Habitat = Habitat()
  val logic = new LogicGui(Constants.SavedSpecies)
  logic.initialize()

  test("The computation performed in the GameLoop should not throw exceptions"){

    //reusable manager
    var animalManager: AnimalManager = AnimalManager().generateInitialAnimals(logic.species, habitat)
    var resourceManager: ResourceManager = ResourceManager(habitat, Constants.FoodsFilePath)

    for (_ <- 1 to 20 if animalManager.animals.lengthIs > 0) {

      //find destination
      val destinationManager: DestinationManager =
        DestinationManager(animalManager.animals, resourceManager.someFoods, habitat)
      val destinations: Map[Animal, Point] = destinationManager.calculateDestination()

      //animals movement
      val shiftManager = ShiftManager(habitat, destinations)
      shiftManager.walk()
      animalManager = AnimalManager(shiftManager.animals.toSeq)

      //animals eat and drink
      val feedManager = FeedManager(animalManager.animals, resourceManager.someFoods, habitat)
      val (animalAfterFeed, foodAfterFeed) = feedManager.consumeResources()
      animalManager = AnimalManager(animalAfterFeed)
      resourceManager = resourceManager.someFoods_(foodAfterFeed)

      //animals life cycle
      val (animalAfterLifeCycle, foodAfterLifeCycle) = animalManager.lifeCycleUpdate()
      animalManager = AnimalManager(animalAfterLifeCycle)
      resourceManager = resourceManager.someFoods_(resourceManager.someFoods ++ foodAfterLifeCycle)

      //animals battle
      val battleManager: BattleManager = BattleManager(animalManager.animals)
      val (animalAfterBattle, foodAfterBattle) = battleManager.battle()
      animalManager = AnimalManager(animalAfterBattle)
      resourceManager = resourceManager.someFoods_(resourceManager.someFoods ++ foodAfterBattle)

      //animals killed by unexpected events
      val (animalAfterUnexpectedEvents, foodAfterUnexpectedEvents) = animalManager.unexpectedEvents(habitat)
      animalManager = AnimalManager(animalAfterUnexpectedEvents)
      resourceManager = resourceManager.someFoods_(resourceManager.someFoods ++ foodAfterUnexpectedEvents)

      //vegetable growth
      resourceManager = resourceManager.grow()
    }
  }
}
