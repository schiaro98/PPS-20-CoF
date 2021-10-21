package controller

import controller.manager.BattleManager
import model.{animal, _}
import model.animal.{Animal, Big, Carnivore, Herbivore, Medium, Small, Species}
import model.position.Point
import org.scalatest.funsuite.AnyFunSuite

class BattleManagerTest extends AnyFunSuite{

  val a1: Animal = animal.Animal(Species("tiger1", Medium, 10, 10, Carnivore), Point(0,0))
  val a2: Animal = animal.Animal(Species("tiger2", Big, 10, 10, Carnivore), Point(1,1))
  val a3: Animal = animal.Animal(Species("tiger3", Small, 10, 10, Carnivore), Point(2,2))
  val herb: Animal = animal.Animal(Species("elephant4", Small, 10, 10, Herbivore), Point(2,2))
  val unreachableAnimal: Animal = animal.Animal(Species("tiger", Medium, 10, 10, Carnivore), Point(100,100))
  val bm: BattleManager = BattleManager()

  test("Herbivore should not fight"){
    val size = 10
    val herbivore: Animal = animal.Animal(Species("Herbivore1", Medium, 10, 10, Herbivore), Point(0,0))
    val bm = BattleManager(Seq.fill(size)(herbivore))
    val (notDeadAnimal, meat) = bm.battle()
    assert(notDeadAnimal.lengthCompare(size) == 0)
    assert(meat.isEmpty)
  }

  test("Distant animals should not fight"){
    val a1: Animal = animal.Animal(Species("tiger1", Medium, 10, 10, Carnivore), Point(0,0))
    val a2: Animal = animal.Animal(Species("tiger2", Big, 10, 10, Carnivore), Point(100,100))
    val a3: Animal = animal.Animal(Species("tiger3", Small, 10, 10, Carnivore), Point(200,200))
    val bm = BattleManager(Seq(a1,a2,a3))
    val (notDeadAnimal, meat) = bm.battle()
    assert(notDeadAnimal.lengthCompare(3) == 0)
    assert(meat.isEmpty)
  }
}
