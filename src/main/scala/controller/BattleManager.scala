package controller

import model.{Animal, Carnivorous, Probability, Size}

sealed trait BattleManager {
  val animals: Seq[Animal]

  def visibleAnimals() : Seq[(Animal, Animal)]

  def visibleAnimals(animals: Seq[Animal]) : Seq[(Animal, Animal)]

  def battle(probability: Probability) : Boolean

  def calculateProbabilityFromSize(a1: Animal, a2: Animal) : Probability

  def calculateProbabilityFromDistance(a1: Animal, a2: Animal) : Probability

  def calculateProbabilityFromStrength(a1: Animal, a2: Animal) : Probability

  def startBattle(a1: Animal, a2: Animal): Unit

  def calculateBattles(): Unit

  def isCarnivorous(animal: Animal): Boolean
}

object BattleManager {
  def apply(animals: Seq[Animal]) : BattleManager = SimpleBattleManager(animals)
  def apply() : BattleManager = SimpleBattleManager(Seq.empty)
}

private case class SimpleBattleManager(animals: Seq[Animal]) extends BattleManager {

  /**
   * Return a sequence of tuples of every animal an animal can see, given the sequence created in the constructor
   * @return Sequence of tuples
   */
  override def visibleAnimals(): Seq[(Animal, Animal)] = {
    visibleAnimals(animals)
  }

  /**
   * Return a sequence of tuples of every animal an animal can see.
   * @param seqOfAnimals animal to be compared
   * @return Sequence of tuples
   */
  def visibleAnimals(seqOfAnimals : Seq[Animal]): Seq[(Animal, Animal)] = {
    var visible: Seq[(Animal, Animal)] = Seq.empty
    for( a <- seqOfAnimals; b <- seqOfAnimals.filterNot(animal => a == animal)) {
      if(a canSee b) visible = visible :+ (a,b)
    }
    visible
  }

  /**
   * Execute the battle between the attacker and the defender animal
   * @param attacker Attacking animal
   * @param defender Defending animal
   * @return
   */
  override def startBattle(attacker: Animal, defender: Animal): Unit = {
    require(isCarnivorous(attacker))
    require(attacker.isAlive)
    require(attacker canSee defender)

    val probabilities = List(
      calculateProbabilityFromSize(attacker, defender),
      calculateProbabilityFromDistance(attacker, defender),
      calculateProbabilityFromStrength(attacker, defender)
    )

    if(battle(Probability(probabilities.map(a => a.probability).sum / probabilities.length))){
      //TODO Do something about the two animals, togliere vita iniziale all'animale
      println("Attacking animal: " + attacker + "has won")
    } else {
      println("Defending animal: " + defender + "has won")
    }
  }

  override def battle(probability: Probability): Boolean = probability.calculate

  /**
   * Given a probability, it increase if the defending animal is fast (deducted by it's size) and far away. But it can
   * increase if the attacking animal is near or fast
   * @param attacker attacking animal
   * @param defender defending animal
   * @return Output probability of winning the battle
   */
  override def calculateProbabilityFromDistance(attacker: Animal, defender: Animal): Probability = {
    val probability = Probability(50)
    attacker.position.distance(defender.position).toInt match {
      case x if x <= 1 => probability.increase(50)
      case x if x <= 5 && x > 1 =>
        if (attacker.size != Size.Big && defender.size == Size.Big) probability.increase(20)
        else if (attacker.size == Size.Big && defender.size == Size.Small) probability.decrease(20)
        else probability
      case x if x <= 10 && x > 5 =>
        if (attacker.size != Size.Big && defender.size == Size.Big) probability
        else if (attacker.size == Size.Big && defender.size == Size.Small) probability.decrease(50)
        else probability
      case x if x > 10 => probability.increase(75)
    }
  }

  /**
   * Given a probability, it decrease if the defending animal is stronger.
   * @param attacker attacking animal
   * @param defender defending animal
   * @return Output probability of winning the battle
   */
  override def calculateProbabilityFromStrength(attacker: Animal, defender: Animal): Probability = {
    val probability = Probability(50)
    attacker.strength - defender.strength match {
      case x if x > 5 => probability.increase(50) //Attacking molto più forte
      case x if x > 0 && x <= 5 => probability.increase(20)
      case x if x <= 0 && x > -5 => probability.decrease(20)
      case x if x <= -5 => probability.decrease(50) //Defending molto più forte
    }
  }

  /**
   * Calculate the probability that animal 1, attacker wins, given only the two sizes
   * @param attacker animal who figth
   * @param defender animal who has been figthed
   * @return probability of the attacker to win
   */
  override def calculateProbabilityFromSize(attacker: Animal, defender: Animal): Probability = {
    var probability = Probability(50)
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
    probability
  }


  override def calculateBattles(): Unit = {
    visibleAnimals().filter(couple => isCarnivorous(couple._1)).foreach(couple => startBattle(couple._1, couple._2))
  }

  override def isCarnivorous(animal: Animal): Boolean = animal.isInstanceOf[Carnivorous]
}