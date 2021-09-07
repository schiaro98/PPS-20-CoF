import model.{Area, Fertile, Habitat, Probability, Rock, Volcano, Water}
import org.scalatest.funsuite.AnyFunSuite

class HabitatTest extends AnyFunSuite{

  test("Create empty Habitat"){
    val habitat = Habitat( Probability(1), (100, 100))
    assert(habitat.areas.isEmpty)
  }

  //le aree possono accavallarsi, non so se va bene
  //posso creare delle aree piu grandi dell'habitat stesso
  test("Create Habitat with overlapping areas"){
    val fertileArea = Area(Fertile, (0,0), (10,10))
    val waterArea = Area(Water, (0,0), (10,10))
    val rockArea = Area(Rock, (0,0), (10,10))
    val volcanoArea = Area(Volcano, (0,0), (10,10))

    assertThrows[IllegalArgumentException](Habitat( Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea)))
  }

  test("Create Habitat with too big areas"){
    val fertileArea = Area(Fertile, (0,0), (10,10))
    val waterArea = Area(Water, (90, 90), (110,100))
    assertThrows[IllegalArgumentException](Habitat( Probability(1), (100, 100), Seq(fertileArea, waterArea)))
  }

  test("Create Habitat with areas"){
    val fertileArea = Area(Fertile, (0,0), (10,10))
    val waterArea = Area(Water, (0, 15), (15,30))
    val rockArea = Area(Rock, (40,40), (50,50))
    val volcanoArea = Area(Volcano, (70,70), (80,80))

    val habitat = Habitat( Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea))
    assert(habitat.areas.length == 4)
  }

}
