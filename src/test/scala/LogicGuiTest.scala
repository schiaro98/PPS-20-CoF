import model.Type.Carnivore
import model.{Size, Species}
import org.scalatest.funsuite.AnyFunSuite
import view.LogicGui

class LogicGuiTest extends AnyFunSuite {
  val mng = new LogicGui("speciesTest.txt")
  val exampleSpecies: Species = Species("", "TestName", Size.Small, 10, 10, Carnivore)
  mng.removeAllSpeciesFromFile()
  mng.addSpeciesInTheFile(Species("null","Lion", Size.Medium, 10, 10, Carnivore))
  mng.addSpeciesInTheFile(Species("null","Ippo", Size.Big, 10, 10, Carnivore))
  mng.addSpeciesInTheFile(Species("null","Zebra", Size.Small, 10, 10, Carnivore))
  mng.initialize()

  test("List should be populated with basic species after initialize"){
    assert(mng.species.size == 3)
  }

  test("Should be possible add an animal"){
    mng.add(exampleSpecies)
    assert(mng.species.size == 4)
  }

  test("Should be possible increase quantity of an animal"){
    mng.add(exampleSpecies)
    mng.increase(exampleSpecies)
    assert(mng.species.size == 4)
  }

  test("Should be possible to delete an animal"){
    mng.add(exampleSpecies)
    mng.increase(exampleSpecies)
    assert(mng.species.size == 4)
    mng.remove(exampleSpecies)
    assert(mng.species.size == 3)
  }

  test("Decrease quantity of an animal"){
    mng.add(exampleSpecies)
    mng.increase(exampleSpecies)

    assert(mng.species.size == 4)
    assert(mng.species(exampleSpecies) == 2)
    mng.decrease(exampleSpecies)

    assert(mng.species(exampleSpecies) == 1)
    mng.decrease(exampleSpecies)

    assert(mng.species.size == 3)
  }
}
