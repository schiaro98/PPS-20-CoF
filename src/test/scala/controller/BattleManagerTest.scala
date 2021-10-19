package controller

import model.{Point, _}
import org.scalatest.funsuite.AnyFunSuite

class BattleManagerTest extends AnyFunSuite{

  val a1: Animal = Animal(Species("tiger1", Medium, 10, 10, Carnivore), Point(0,0))
  val a2: Animal = Animal(Species("tiger2", Small, 10, 10, Carnivore), Point(1,1))
  val a3: Animal = Animal(Species("tiger3", Small, 10, 10, Carnivore), Point(2,2))
  val herb: Animal = Animal(Species("elephant4", Small, 10, 10, Herbivore), Point(2,2))
  val unreachableAnimal: Animal = Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(100,100))
  val bm: BattleManager = BattleManager()

  test("Test if two animals sees each other"){
    assert(a1 canSee  a2)
    assert(a2 canSee a1)
    assert(!(a1 canSee unreachableAnimal))
  }

  test("Test if animals that sees each other are returned"){
    assert(bm.visibleAnimals(Seq(a1,a2,a3,unreachableAnimal)).toSet ===
      Seq((a1,a2), (a2,a1), (a1, a3), (a2, a3), (a3,a1), (a3,a2)).toSet)
  }


  test("Execute a simple battle"){
    val bm = BattleManager(Seq(a1, a2, a3))
    bm.battle()
    val result = bm.battle()
    result._1.foreach(println)
  }

  test("Check result of the battle"){
    val bm = BattleManager(Seq(a1, a2, a3, herb, unreachableAnimal))
    val result = bm.battle()

  }

/*  test("Test if an herbivour can battle"){
    val bm = BattleManager(Seq(herb))
    assertThrows[IllegalArgumentException](bm.startBattle(herb, a1))
  }

  test("Test if a damaged (with a low health) animal can battle"){
    val damagedAnimal = a2.update(health = 0, a2.thirst, a2.position)
    assertThrows[IllegalArgumentException](bm.startBattle(damagedAnimal, a1))
  }*/
}
