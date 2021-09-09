import controller.ResourceManager
import model.{Area, Fertile, Food, Habitat, Probability, Rock, Volcano, Water}
import org.scalatest.funsuite.AnyFunSuite

class ResourceManagerTest extends AnyFunSuite{

  val icon = "icon.png"

  val fertileArea: Area = Area(Fertile, (0,0), (10,10))
  val waterArea: Area = Area(Water, (0, 15), (15,30))
  val rockArea: Area = Area(Rock, (40,40), (50,50))
  val volcanoArea: Area = Area(Volcano, (70,70), (80,80))
  val area: Area = Area((60,60), (65, 65), Probability(100))

  val habitat: Habitat = Habitat( Probability(1), (100, 100), Seq(fertileArea, waterArea, rockArea, volcanoArea, area))

  test("ResourceManager grow() with no growable foods"){
    val resMan = ResourceManager(habitat)

    val newResMan = resMan.grow()
    assert(newResMan.foods.isEmpty)
  }

  test("ResourceManager grow()"){
    val resMan = ResourceManager(habitat, Set(Food(icon, 5), Food(icon, 10), Food(icon, 15), Food(icon, 50)))
    val newResMan = resMan.grow()
    println(newResMan.foods)
    assert(newResMan.foods.nonEmpty)
  }
}
