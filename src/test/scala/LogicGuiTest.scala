import org.scalatest.Ignore
import org.scalatest.funsuite.AnyFunSuite
import view.LogicGui

class LogicGuiTest extends AnyFunSuite {
  var mng = new LogicGui

  test("Initial list of species should be empty"){
    assert(mng.species.isEmpty)
  }

  test("List should be populated with basic species after initialize"){
    mng.initialize()
    assert(mng.species.size == 3)
  }

  test("Should be possible add an animal"){
    mng.initialize()
    mng.add("TestName")
    assert(mng.species.size == 4)
  }

  test("Should be possible increase quantity of an animal"){
    mng.initialize()
    mng.add("TestName")
    mng.increase("TestName")
    assert(mng.species.size == 4)
  }

  test("Should be possible to delete an animal"){
    mng.initialize()
    mng.add("TestName")
    mng.increase("TestName")
    assert(mng.species.size == 4)
    mng.remove("TestName")
    assert(mng.species.size == 3)
  }

  test("Decrease quantity of an animal"){
    mng.initialize()
    mng.add("TestName")
    mng.increase("TestName")
    println(mng.species)
    assert(mng.species.size == 4)
    assert(mng.species("TestName") == 2)
    mng.decrease("TestName")
    println(mng.species)

    assert(mng.species("TestName") == 1)
    mng.decrease("TestName")
    println(mng.species)

    assert(mng.species.size == 3)
  }
}
