import model.{Size, Species}
import org.scalatest.funsuite.AnyFunSuite
import view.LogicGui

class LogicGuiTest extends AnyFunSuite {
  var mng = new LogicGui("speciesTest.txt")
  mng.removeAllSpecies()
  mng.addSpecies(Species("null","Lion", Size.Medium, 10, 10))
  mng.addSpecies(Species("null","Ippo", Size.Big, 10, 10))
  mng.addSpecies(Species("null","Zebra", Size.Small, 10, 10))
  mng.initialize()

  test("List should be populated with basic species after initialize"){
    assert(mng.speciesSeq.size == 3)
  }

  test("Should be possible add an animal"){
    mng.add("TestName")
    println(mng.species)
    println(mng.speciesSeq)
    assert(mng.species.size == 4)
  }

  test("Should be possible increase quantity of an animal"){
    mng.add("TestName")
    mng.increase("TestName")
    assert(mng.species.size == 4)
  }

  test("Should be possible to delete an animal"){
    mng.add("TestName")
    mng.increase("TestName")
    assert(mng.species.size == 4)
    mng.remove("TestName")
    assert(mng.species.size == 3)
  }

  test("Decrease quantity of an animal"){
    mng.add("TestName")
    mng.increase("TestName")

    assert(mng.species.size == 4)
    assert(mng.species("TestName") == 2)
    mng.decrease("TestName")

    assert(mng.species("TestName") == 1)
    mng.decrease("TestName")

    assert(mng.species.size == 3)
  }
}
