package view

import model._
import org.scalatest.funsuite.AnyFunSuite

class LogicGuiTest extends AnyFunSuite {
  val mng = new LogicGui("speciesTest.txt")
  val exampleSpecies: Species = Species("TestName", Small, 10, 10, Carnivore)
  mng.removeAllSpeciesFromFile()
  mng.addSpeciesInTheFile(Species("Lion", Medium, 10, 10, Carnivore))
  mng.addSpeciesInTheFile(Species("Ippo", Big, 10, 10, Carnivore))
  mng.addSpeciesInTheFile(Species("Zebra", Small, 10, 10, Carnivore))
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
