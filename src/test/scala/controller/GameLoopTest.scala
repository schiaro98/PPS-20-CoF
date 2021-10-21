package controller

import model.Habitat
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants
import view.LogicGui

import scala.annotation.tailrec

class GameLoopTest extends AnyFunSuite{

  val habitat: Habitat = Habitat()
  val logic = new LogicGui(Constants.SavedSpecies)
  logic.initialize()

  test("The computation performed in the GameLoop should not throw exceptions") {
    val gameLoop = GameLoop(logic.species, habitat)
    val animalManager: AnimalManager = AnimalManager().generateInitialAnimals(logic.species, habitat)
    val resourceManager: ResourceManager = ResourceManager(habitat, Constants.FoodsFilePath).fillHabitat()

    @tailrec
    def loop(animalManager: AnimalManager, resourceManager: ResourceManager, counter: Int = 0): Unit = {
      val (newAnimalManager, newResourceManager) = gameLoop.compute(animalManager, resourceManager)
      if (counter < 20 || animalManager.animals.lengthIs > 0)
        loop(newAnimalManager, newResourceManager, counter + 1)
    }
    loop(animalManager, resourceManager)
  }
}
