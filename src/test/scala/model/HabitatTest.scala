package model

import model.habitat.{Area, EmptyHabitatType, Fertile, GridHabitatType, Habitat, RandomHabitatType, Rock, SimpleHabitatType, Volcano, Water}
import model.position.Point
import model.shape.RectangleArea
import org.scalatest.funsuite.AnyFunSuite
import utility.Constants

class HabitatTest extends AnyFunSuite{

  val testQ = 100
  val centerArea: Area = habitat.Area(Rock, RectangleArea(Point(3, 2), Point(7, 5)))

  test("Create empty Habitat"){
    val habitat = Habitat(EmptyHabitatType, Probability(1), (100, 100), Seq.empty)
    assert(habitat.areas.isEmpty)
  }

  test("Create Habitat with overlapping areas"){
    val fertileArea = habitat.Area(Fertile, shape.RectangleArea(Point(0,0), Point(10,10)))
    val waterArea = habitat.Area(Water, shape.RectangleArea(Point(0,0), Point(10,10)))
    val rockArea = habitat.Area(Rock, shape.RectangleArea(Point(0,0), Point(10,10)))
    val volcanoArea = habitat.Area(Volcano, shape.RectangleArea(Point(0,0), Point(10,10)))

    assertThrows[IllegalArgumentException](habitat.Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea)))
  }

  test("Overlapping on left upper corner"){
    val a = habitat.Area(Water, shape.RectangleArea(Point(2,1), Point(4,3)))
    assertThrows[IllegalArgumentException](habitat.Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(a, centerArea)))
  }

  test("Overlapping on left bottom corner"){
    val b = habitat.Area(Water, shape.RectangleArea(Point(2,4), Point(4,6)))
    assertThrows[IllegalArgumentException](habitat.Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(b, centerArea)))
  }

  test("Overlapping on right upper corner"){
    val c = habitat.Area(Water, shape.RectangleArea(Point(6,1), Point(8,3)))
    assertThrows[IllegalArgumentException](habitat.Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(c, centerArea)))
  }

  test("Overlapping on right bottom corner"){
    val d = habitat.Area(Water, shape.RectangleArea(Point(6,4), Point(8,6)))
    assertThrows[IllegalArgumentException](habitat.Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(d, centerArea)))
  }

  test("Create Habitat with too big areas"){
    val fertileArea = habitat.Area(Fertile, shape.RectangleArea(Point(0,0), Point(10,10)))
    val waterArea = habitat.Area(Water, shape.RectangleArea(Point(90, 90), Point(110,100)))
    assertThrows[IllegalArgumentException](habitat.Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(fertileArea, waterArea)))
  }

  test("Create Habitat with areas"){
    val fertileArea = Area(Fertile, shape.RectangleArea(Point(0,0), Point(10,10)))
    val waterArea = Area(Water, shape.RectangleArea(Point(0, 15), Point(15,30)))
    val rockArea = Area(Rock, shape.RectangleArea(Point(40,40), Point(50,50)))
    val volcanoArea = Area(Volcano, shape.RectangleArea(Point(70,70), Point(80,80)))

    val habitat = Habitat( SimpleHabitatType, Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea))
    assert(habitat.areas.lengthIs == 4)
  }

  test("Create a grid Habitat with areas, and test it with different number of areas"){
    val tollerance = 5 //Ten % of size is an acceptable tollerance, some areas cannot be drawn because of limit of space
    for(_ <- 0 to testQ * 100) {
        val habitat = Habitat(GridHabitatType, Probability(1), (1000, 1000), Seq.empty)
        assert(habitat.areas.lengthIs >=
          (Constants.DefaultGridSize - (Constants.DefaultGridSize / tollerance)))
    }
  }

  test("Create a random Habitat with areas, and test it with different number of areas"){
    for(_ <- 0 to testQ){
      val habitat = Habitat(RandomHabitatType, Probability(1), (1000, 1000), Seq.empty)
      assert(habitat.areas.lengthIs == 4)
    }
  }
}
