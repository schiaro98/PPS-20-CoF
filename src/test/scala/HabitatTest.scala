import model.{Habitat, NonWalkableArea, Probability, Rock, WalkableArea, Water}
import org.scalatest.funsuite.AnyFunSuite

class HabitatTest extends AnyFunSuite{

  test("Create empty Habitat"){
    val habitat = Habitat( Probability(1), (100, 100))
    assert(habitat.walkableAreas.isEmpty)
    assert(habitat.nonWalkableAreas.isEmpty)
  }

  //le aree possono accavallarsi, non so se va bene
  //posso creare delle aree piu grandi dell'habitat stesso
  test("Create Habitat with areas"){
    val wa1 = WalkableArea("wa1", (10, 10), (20, 20), Probability(50), Probability(20))
    val wa2 = WalkableArea("wa2", (13, 15), (24, 27), Probability(50), Probability(20))
    val nwa1 = NonWalkableArea("nwa1", (10,20), (60,80), Water)
    val nwa2 = NonWalkableArea("nwa2", (10,20), (60,80), Rock)
    val habitat = Habitat( Probability(1), (100, 100), Seq(wa1, wa2), Seq(nwa1,nwa2))
    assert(habitat.walkableAreas.length == 2)
    assert(habitat.nonWalkableAreas.length == 2)
  }
}
