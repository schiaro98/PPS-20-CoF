package controller

import model.{Animal, Probability, Size}

sealed trait BattleManager {
  val animals: Seq[Animal]

  /**
   * This method gives a sequence of tuples of animals
   * Every tuple is formed by an animal x, and another animal y, visibile from x
   *
   * @param animals sequence of animal present
   * @return sequence of tuples
   */
  def visibleAnimals(animals: Seq[Animal]) : Seq[(Animal, Animal)]

  /**
   * Given two animals, return true if the first is able to see the second
   * @param a1 First animal
   * @param a2 second animal
   * @return
   */
  def canSee(a1: Animal, a2: Animal) : Boolean

  /**
   * Return trues with a given probability (Rappresent the win of a battle)
   * @param probability of winning a battle
   * @return true if the battle is win
   */
  def battle(probability: Probability) : Boolean

  /**
   * Calculate the probabilty that animal a1 wins the battle
   * @param a1 Attacking animal
   * @param a2 Defending animal
   * @return a probability
   */
  def calculateProbability(a1: Animal, a2: Animal) : Probability

  /**
   * Start a battle between two animals
   * @param a1 attacking animal
   * @param a2 defending animal
   * @return true if the battle is win by a1
   */
  def startBattle(a1: Animal, a2: Animal): Boolean
}

object BattleManager {
  def apply(animals: Seq[Animal]) : BattleManager = SimpleBattleManager(animals)
  def apply() : BattleManager = SimpleBattleManager(Seq.empty)
}

private case class SimpleBattleManager(animals: Seq[Animal]) extends BattleManager {

  override def visibleAnimals(animals: Seq[Animal]): Seq[(Animal, Animal)] = {
    var visible: Seq[(Animal, Animal)] = Seq.empty
    for( a <- animals; b <- animals.filterNot(animal => a == animal)) {
      if(canSee(a,b)) visible = visible :+ (a,b)
    }
    visible
  }

  override def canSee(a1: Animal, a2: Animal): Boolean = a1.position.distance(a2.position) <= a1.sight

  override def calculateProbability(attacker: Animal, defender: Animal): Probability = {
    var probability = Probability(50)
    if(attacker.strength > defender.strength){
      probability = attacker.size match {
        case Size.Big => defender.size match {
          case Size.Medium => probability.increase(20)
          case Size.Small => probability.increase(50)
          case _ => probability
        }
        case Size.Medium => defender.size match {
          case Size.Big => probability.decrease(20)
          case Size.Small => probability.increase(20)
          case _ => probability
        }
        case Size.Small => defender.size match {
          case Size.Big => probability.decrease(80)
          case Size.Medium => probability.decrease(50)
          case _ => Probability(probability.probability)
        }
      }
    }
    probability
  }

  override def startBattle(attacker: Animal, defender: Animal): Boolean = {
    require(canSee(attacker, defender))
    //if(battle(attacker, defender)) //defender.die()
    //else defender.scappa ?!?! TODO
    battle(calculateProbability(attacker, defender))
  }

  override def battle(probability: Probability): Boolean = probability.calculate
}