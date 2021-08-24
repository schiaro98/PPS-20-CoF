import model.{Habitat, Probability}
import org.scalatest.funsuite.AnyFunSuite

class HabitatTest extends AnyFunSuite{

  test("Create empty Habitat"){
    val habitat = Habitat( Probability(1), (100, 100))
    assert(habitat.walkableAreas.isEmpty)
    assert(habitat.nonWalkableAreas.isEmpty)
  }
}
