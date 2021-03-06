package controller

import controller.manager.BattleManager
import model.animal
import model.animal._
import model.position.Point
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class BattleManagerTest extends AnyFlatSpec with should.Matchers {

  "An Herbivore" should "not fight" in {
    val size = 10
    val herbivore: Animal = animal.Animal(Species("Herbivore1", Medium, 10, 10, Herbivore), Point(0,0))
    val bm = BattleManager(Seq.fill(size)(herbivore))
    val (notDeadAnimal, meat) = bm.battle()
    notDeadAnimal.length should be (size)
    meat.length should be (0)
  }

  "Distant animals" should "not fight" in {
    val a1: Animal = animal.Animal(Species("tiger1", Medium, 10, 10, Carnivore), Point(0,0))
    val a2: Animal = animal.Animal(Species("tiger2", Big, 10, 10, Carnivore), Point(100,100))
    val a3: Animal = animal.Animal(Species("tiger3", Small, 10, 10, Carnivore), Point(200,200))
    val bm = BattleManager(Seq(a1,a2,a3))
    val (notDeadAnimal, meat) = bm.battle()
    notDeadAnimal.length should be (3)
    meat foreach println
    meat.length should be (0)
  }
}
