import model._
import org.scalatest.funsuite.AnyFunSuite
import view.LogicGui

class LogicGuiFileTest extends AnyFunSuite {
  val mng = new LogicGui("speciesTest.txt")
  mng.removeAllSpeciesFromFile()

  val exampleSpecies: Species = Species("", "TestName", Small, 10, 10, Carnivore)

  test("File initially must be empty") {
    assert(mng.species.isEmpty)
  }

  test("Should be possible add an animal") {
    mng.addSpeciesInTheFile(exampleSpecies)
    mng.add(exampleSpecies)
    assert(mng.species.size == 1)
  }

  test("Should be possible increase quantity of an animal") {
    mng.add(exampleSpecies)
    mng.increase(exampleSpecies)
    assert(mng.species.size == 1)
    assert(mng.species(exampleSpecies) == 2)
  }

  test("Should be possible to delete an animal") {
    mng.add(exampleSpecies)
    mng.increase(exampleSpecies)
    assert(mng.species(exampleSpecies) == 3)
    mng.remove(exampleSpecies)
    assert(mng.species.isEmpty)
  }

  test("Decrease quantity of an animal") {
    mng.add(exampleSpecies)
    mng.increase(exampleSpecies)

    assert(mng.species.size == 1)
    assert(mng.species(exampleSpecies) == 2)
    mng.decrease(exampleSpecies)

    assert(mng.species(exampleSpecies) == 1)
    mng.decrease(exampleSpecies)

    assert(mng.species.isEmpty)
  }

}
