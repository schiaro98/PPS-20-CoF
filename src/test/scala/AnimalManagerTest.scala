import org.scalatest.funsuite.AnyFunSuite

class AnimalManagerTest extends AnyFunSuite {
  var mng = new AnimalManager

  test("Initial list of animals should be empty"){
    assert(mng.animals.isEmpty)
  }

  test("List should be populated with basic animals after initialize"){
    mng.initialize()
    assert(mng.animals.size == 3)
  }

  test("Should be possible add an animal"){
    mng.initialize()
    mng.add("TestName")
    assert(mng.animals.size == 4)
  }

  test("Should be possible increase quantity of an animal"){
    mng.initialize()
    mng.add("TestName")
    mng.increase("TestName")
    assert(mng.animals.size == 4)
  }

  test("Should be possible to delete an animal"){
    mng.initialize()
    mng.add("TestName")
    mng.increase("TestName")
    assert(mng.animals.size == 4)
    mng.remove("TestName")
    assert(mng.animals.size == 3)
  }

  test("Decrease quantity of an animal"){
    mng.initialize()
    mng.add("TestName")
    mng.increase("TestName")
    println(mng.animals)
    assert(mng.animals.size == 4)
    assert(mng.animals("TestName") == 2)
    mng.decrease("TestName")
    println(mng.animals)

    assert(mng.animals("TestName") == 1)
    mng.decrease("TestName")
    println(mng.animals)

    assert(mng.animals.size == 3)
  }
}
