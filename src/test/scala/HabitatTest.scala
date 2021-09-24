import model.{Area, EmptyHabitatType, Fertile, GridHabitatType, Habitat, Probability, RandomHabitatType, Rock, SimpleHabitatType, Volcano, Water}
import org.scalatest.funsuite.AnyFunSuite

class HabitatTest extends AnyFunSuite{

  val centerArea: Area = Area(Rock, (3, 2), (7, 5))

  test("Create empty Habitat"){
    val habitat = Habitat(EmptyHabitatType, Probability(1), (100, 100), Seq.empty)
    assert(habitat.areas.isEmpty)
  }

  test("Create Habitat with overlapping areas"){
    val fertileArea = Area(Fertile, (0,0), (10,10))
    val waterArea = Area(Water, (0,0), (10,10))
    val rockArea = Area(Rock, (0,0), (10,10))
    val volcanoArea = Area(Volcano, (0,0), (10,10))

    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea)))
  }

  test("Overlapping on left upper corner"){
    val a = Area(Water, (2,1), (4,3))
    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(a, centerArea)))
  }

  test("Overlapping on left bottom corner"){
    val b = Area(Water, (2,4), (4,6))
    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(b, centerArea)))
  }

  test("Overlapping on right upper corner"){
    val c = Area(Water, (6,1), (8,3))
    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(c, centerArea)))
  }

  test("Overlapping on right bottom corner"){
    val d = Area(Water, (6,4), (8,6))
    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(d, centerArea)))
  }

  test("Create Habitat with too big areas"){
    val fertileArea = Area(Fertile, (0,0), (10,10))
    val waterArea = Area(Water, (90, 90), (110,100))
    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(fertileArea, waterArea)))
  }

  test("Create Habitat with areas"){
    val fertileArea = Area(Fertile, (0,0), (10,10))
    val waterArea = Area(Water, (0, 15), (15,30))
    val rockArea = Area(Rock, (40,40), (50,50))
    val volcanoArea = Area(Volcano, (70,70), (80,80))

    val habitat = Habitat( SimpleHabitatType, Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea))
    assert(habitat.areas.length == 4)
  }

  test("Create a grid Habitat with areas"){
    //TODO grid areas are overlapping
    // TODO: manca l'assert schia
    val a1 = Area(Water, (40,40), (46,63))
    val a2 = Area(Water, (30,40), (35,46))
    val habitat = Habitat( SimpleHabitatType, Probability(1), (100, 100), Seq(a1, a2))
  }
}
