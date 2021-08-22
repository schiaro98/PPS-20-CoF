import org.scalatest.funsuite.AnyFunSuite
import model.Area
import model.TypeOfArea.{Dangerous, Fertile}

class AreaTest extends AnyFunSuite{

  test("Not create Illegal Areas"){
    assertThrows[IllegalArgumentException](Area(Fertile, "fertile", (10, 10), (5,5), fertility = 500))
    assertThrows[IllegalArgumentException](Area(Fertile, "fertile", (10, 10), (5,5), fertility = -100))
    assertThrows[IllegalArgumentException](Area(Fertile, "fertile", (10, 10), (5,5), hospitality = 500))
    assertThrows[IllegalArgumentException](Area(Fertile, "fertile", (10, 10), (5,5), hospitality = -100))
    assertThrows[IllegalArgumentException](Area(Dangerous, "dangerous", (10, 10), (5,5), danger = 500))
    assertThrows[IllegalArgumentException](Area(Dangerous, "dangerous", (10, 10), (5,5), danger = -100))
  }

  test("Create FertileArea") {
    val fertileArea: Area = Area(Fertile, "fertile", (10, 10), (5,5), fertility = 50)
    assert(fertileArea.name == "fertile")
  }

}
