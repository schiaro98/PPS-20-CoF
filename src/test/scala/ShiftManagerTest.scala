import controller.ShiftManager
import model.Size.Medium
import model.{Animal, Carnivorous, Herbivore, Species}
import org.scalatest.funsuite.AnyFunSuite
import utility.Point

class ShiftManagerTest extends AnyFunSuite{

  val path = "res/images/tiger-icon.jpg"
  val c: Animal = Carnivorous(Species(path, "tiger", Medium, 10, 10), Point(15,15))
  val h: Animal = Herbivore(Species(path, "elephant", Medium, 10, 10),  Point(40,40))

  test("Create ShiftManager"){
    val sm: ShiftManager = ShiftManager(Map(c->Some(Point(55,100)), h->None))
    assert(sm.animals.size == 2)
  }

  test("Testing walk in ShiftManager"){
    val sm: ShiftManager = ShiftManager(Map(c->Some(Point(55,100)), h->None))
    val fuck = sm.walk
    println(fuck.foreach(println))
  }

}
