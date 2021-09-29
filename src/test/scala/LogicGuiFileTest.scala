import model.{Size, Species}
import org.scalatest.funsuite.AnyFunSuite
import view.LogicGui

class LogicGuiFileTest extends AnyFunSuite {
  var mng = new LogicGui("speciesTest.txt")
  mng.removeAllSpecies()

  val exampleSpecies: Species = Species("", "TestName", Size.Small, 10, 10)

  test("File initially must be empty"){
    assert(mng.species.isEmpty)
  }

  test("Should be possible add an animal"){
    mng.addSpecies(exampleSpecies)
    mng.add("TestName")
    assert(mng.species.size == 1)
    assert(mng.speciesSeq.lengthIs == 1)
  }

  test("Should be possible increase quantity of an animal"){
    mng.add("TestName")
    mng.increase("TestName")
    assert(mng.species.size == 1)
    assert(mng.speciesSeq.lengthIs == 1)
    assert(mng.species(exampleSpecies.name) == 2)
  }

  test("Should be possible to delete an animal"){
    mng.add("TestName")
    mng.increase("TestName")
    assert(mng.species(exampleSpecies.name) == 3)
    mng.remove("TestName")
    assert(mng.species.isEmpty)
  }

  test("Decrease quantity of an animal"){
    mng.add("TestName")
    mng.increase("TestName")

    assert(mng.species.size == 1)
    assert(mng.species("TestName") == 2)
    mng.decrease("TestName")

    assert(mng.species("TestName") == 1)
    mng.decrease("TestName")

    assert(mng.species.isEmpty)
  }

}
