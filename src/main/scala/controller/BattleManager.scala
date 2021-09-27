package controller

import model.Animal

sealed trait BattleManager {
  val animals: Seq[Animal]

  def visibleAnimals(animals: Seq[Animal]) : Seq[(Animal, Animal)]

  def canSee(a1: Animal, a2: Animal) : Boolean

  def battle(a1: Animal, a2: Animal) : Boolean
}

object BattleManager {
  def apply(animals: Seq[Animal]) : BattleManager = SimpleBattleManager(animals)
  def apply() : BattleManager = SimpleBattleManager(Seq.empty)
}

private case class SimpleBattleManager(animals: Seq[Animal]) extends BattleManager {

  /**
   * Return a sequence of tuples of every animal an animal can see
   * @param animals sequence of animals present
   * @return Sequence of tuples
   */
  override def visibleAnimals(animals: Seq[Animal]): Seq[(Animal, Animal)] = {
    var visible: Seq[(Animal, Animal)] = Seq.empty
    for( a <- animals; b <- animals.filterNot(animal => a == animal)) {
      if(canSee(a,b)) visible = visible :+ (a,b)
    }
    visible
  }

  /**
   * Return true if animal a1 can see animal a2
   */
  override def canSee(a1: Animal, a2: Animal): Boolean = a1.position.distance(a2.position) <= a1.sight

  /**
   * Execute battle/figth between two animals
   * @param a1 animal who figth
   * @param a2 animal who has been figthed
   * @return true if animal a1 wins, false otherwise
   */
  override def battle(a1: Animal, a2: Animal): Boolean = {
    require(canSee(a1, a2))
    ???
  }
}