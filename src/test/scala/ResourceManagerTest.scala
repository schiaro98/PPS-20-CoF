import controller.ResourceManager
import model.{Area, Fertile, Habitat, Probability, Rock, Volcano, Water}
import org.scalatest.funsuite.AnyFunSuite

class ResourceManagerTest extends AnyFunSuite{

  test("Create a ResourceManager"){
    val fertileArea = Area(Fertile, (0,0), (10,10))
    val waterArea = Area(Water, (0, 15), (15,30))
    val rockArea = Area(Rock, (40,40), (50,50))
    val volcanoArea = Area(Volcano, (70,70), (80,80))
    val area = Area((60,60), (65, 65), Probability(30))

    val habitat = Habitat( Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea, area))
    val resMan = ResourceManager(habitat)
    resMan.grow
  }
}
