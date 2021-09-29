import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.{Point, RectangleArea}

class HabitatTest extends AnyFunSuite{

  val centerArea: Area = Area(Rock, RectangleArea(Point(3, 2), Point(7, 5)))

  test("Create empty Habitat"){
    val habitat = Habitat(EmptyHabitatType, Probability(1), (100, 100), Seq.empty)
    assert(habitat.areas.isEmpty)
  }

  test("Create Habitat with overlapping areas"){
    val fertileArea = Area(Fertile, RectangleArea(Point(0,0), Point(10,10)))
    val waterArea = Area(Water, RectangleArea(Point(0,0), Point(10,10)))
    val rockArea = Area(Rock, RectangleArea(Point(0,0), Point(10,10)))
    val volcanoArea = Area(Volcano, RectangleArea(Point(0,0), Point(10,10)))

    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea)))
  }

  test("Overlapping on left upper corner"){
    val a = Area(Water, RectangleArea(Point(2,1), Point(4,3)))
    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(a, centerArea)))
  }

  test("Overlapping on left bottom corner"){
    val b = Area(Water, RectangleArea(Point(2,4), Point(4,6)))
    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(b, centerArea)))
  }

  test("Overlapping on right upper corner"){
    val c = Area(Water, RectangleArea(Point(6,1), Point(8,3)))
    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(c, centerArea)))
  }

  test("Overlapping on right bottom corner"){
    val d = Area(Water, RectangleArea(Point(6,4), Point(8,6)))
    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(d, centerArea)))
  }

  test("Create Habitat with too big areas"){
    val fertileArea = Area(Fertile, RectangleArea(Point(0,0), Point(10,10)))
    val waterArea = Area(Water, RectangleArea(Point(90, 90), Point(110,100)))
    assertThrows[IllegalArgumentException](Habitat(SimpleHabitatType, Probability(1), (100, 100), Seq(fertileArea, waterArea)))
  }

  test("Create Habitat with areas"){
    val fertileArea = Area(Fertile, RectangleArea(Point(0,0), Point(10,10)))
    val waterArea = Area(Water, RectangleArea(Point(0, 15), Point(15,30)))
    val rockArea = Area(Rock, RectangleArea(Point(40,40), Point(50,50)))
    val volcanoArea = Area(Volcano, RectangleArea(Point(70,70), Point(80,80)))

    val habitat = Habitat( SimpleHabitatType, Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea))
    assert(habitat.areas.lengthIs == 4)
  }

  test("Create a grid Habitat with areas, and test it with different number of areas"){
    val tollerance = 5 //Ten % of size is an acceptable tollerance, some areas cannot be drawn because of limit of space
    val sizes = Seq(10, 20, 50, 100, 500)
    for(_ <- 0 to 1000) {
      sizes.foreach(size => {
        val habitat = Habitat(GridHabitatType, Probability(1), (1000, 1000), size)
        assert(habitat.areas.lengthIs >= (size - (size / tollerance)))
      })
    }
  }

  test("Create a random Habitat with areas, and test it with different number of areas"){
    for(_ <- 0 to 1000){
      val habitat = Habitat(RandomHabitatType, Probability(1), (1000, 1000), 4)
      assert(habitat.areas.lengthIs == 4)
    }
  }
}
