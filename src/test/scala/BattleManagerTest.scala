import controller.BattleManager
import model.Size.Medium
import model.{Animal, Carnivorous, Species}
import org.scalatest.funsuite.AnyFunSuite
import utility.Point

class BattleManagerTest extends AnyFunSuite{

  val a1: Animal = Carnivorous(Species("", "tiger", Medium, 10, 10), Point(0,0))
  val a2: Animal = Carnivorous(Species("", "tiger", Medium, 10, 10), Point(1,1))
  val a3: Animal = Carnivorous(Species("", "tiger", Medium, 10, 10), Point(2,2))
  val unreachableAnimal: Animal = Carnivorous(Species("", "tiger", Medium, 10, 10), Point(100,100))
  val bm: BattleManager = BattleManager()

  test("Test if two animals sees each other"){
    assert(bm.canSee(a1, a2))
    assert(bm.canSee(a2, a1))
    assert(!bm.canSee(a1, unreachableAnimal))
  }

  test("Test if animals that sees each other are returned"){
    assert(bm.visibleAnimals(Seq(a1,a2,a3,unreachableAnimal)).toSet ===
      Seq((a1,a2), (a2,a1), (a1, a3), (a2, a3), (a3,a1), (a3,a2)).toSet)
  }

  test("Test battle between animals that cannot see each other"){
    assertThrows[IllegalArgumentException](bm.battle(a1, unreachableAnimal))
  }

  /*
  TODO test probabilities for battles
   */
}
