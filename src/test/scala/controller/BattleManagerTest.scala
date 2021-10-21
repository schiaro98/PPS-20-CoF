package controller

import model._
import org.scalatest.funsuite.AnyFunSuite

class BattleManagerTest extends AnyFunSuite{

  /*
  TODO add tests
   */
  val a1: Animal = Animal(Species("tiger1", Medium, 10, 10, Carnivore), Point(0,0))
  val a2: Animal = Animal(Species("tiger2", Big, 10, 10, Carnivore), Point(1,1))
  val a3: Animal = Animal(Species("tiger3", Small, 10, 10, Carnivore), Point(2,2))
  val herb: Animal = Animal(Species("elephant4", Small, 10, 10, Herbivore), Point(2,2))
  val unreachableAnimal: Animal = Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(100,100))
  val bm: BattleManager = BattleManager()

  test("Execute a simple battle"){
    val bm = BattleManager(Seq(a1, a2)) 
    //val (winner, meat) = bm.battle()
    //assert(winner.size == 1)
    //assert(meat.size == 1)
  }

}
