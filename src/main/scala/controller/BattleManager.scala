package controller

import model.{Animal, Probability, Size}

sealed trait BattleManager {
  val animals: Seq[Animal]

  def visibleAnimals(animals: Seq[Animal]) : Seq[(Animal, Animal)]

  def canSee(a1: Animal, a2: Animal) : Boolean

  def battle(a1: Animal, a2: Animal) : Boolean

  def sureBattle(probability: Probability) : Boolean

  def startBattle(a1: Animal, a2: Animal): Boolean
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
   * @param attacker animal who figth
   * @param defender animal who has been figthed
   * @return true if animal attacker wins, false otherwise
   */
  override def battle(attacker: Animal, defender: Animal): Boolean = {
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
    probability.calculate //Esito della battaglia
  }

  override def startBattle(attacker: Animal, defender: Animal): Boolean = {
    require(canSee(attacker, defender))
    if(battle(attacker, defender)) defender.die()
    //else defender.scappa ?!?! TODO
  }

  override def sureBattle(probability: Probability): Boolean = {
    probability.calculate
  }
}