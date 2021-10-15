package controller

import model._
import utility.Logger

sealed trait BattleManager {

  def startBattle(a1: Animal, a2: Animal): Unit

  def calculateBattles(): Unit

  def visibleAnimals(seqOfAnimals: Seq[Animal]): Seq[(Animal, Animal)]
}

object BattleManager {
  def apply(animals: Seq[Animal]): BattleManager = SimpleBattleManager(animals)
  def apply(): BattleManager = SimpleBattleManager(Seq.empty)
  private val logger = Logger

  private case class SimpleBattleManager(animals: Seq[Animal]) extends BattleManager {

    override def calculateBattles(): Unit = {
      visibleAnimals().filter(couple => isCarnivorous(couple._1)).foreach(couple => startBattle(couple._1, couple._2))
    }
    /**
     * Return a sequence of tuples of every animal an animal can see, given the sequence created in the constructor
     *
     * @return Sequence of tuples
     */
    def visibleAnimals(): Seq[(Animal, Animal)] = {
      visibleAnimals(animals)
    }

    /**
     * Return a sequence of tuples of every animal an animal can see.
     *
     * @param seqOfAnimals animal to be compared
     * @return Sequence of tuples
     */
    override def visibleAnimals(seqOfAnimals: Seq[Animal]): Seq[(Animal, Animal)] = {
      var visible: Seq[(Animal, Animal)] = Seq.empty
      for (a <- seqOfAnimals; b <- seqOfAnimals.filterNot(animal => a == animal)) {
        if (a canSee b) visible = visible :+ (a, b)
      }
      visible
    }

    /**
     * Execute the battle between ONE attacking animal and One defending
     *
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

      if (Probability(probabilities.map(a => a.probability).sum / probabilities.length).calculate) {
        logger.info("Attacking animal: " + attacker + "has won")
        //attacker.eat(defender.die())
        //TODO come rilascio la risorsa? defender.die()
      } else {
        logger.info("Defending animal: " + defender + "has won")
      }
    }

    /**
     * Given a probability, it increase if the defending animal is fast (deducted by it's size) and far away. But it can
     * increase if the attacking animal is near or fast
     *
     * @param attacker attacking animal
     * @param defender defending animal
     * @return Output probability of winning the battle
     */
     def calculateProbabilityFromDistance(attacker: Animal, defender: Animal): Probability = {
      val probability = Probability(50)
      attacker.position.distance(defender.position).toInt match {
        case x if x <= 1 => probability.increase(50)
        case x if x <= 5 && x > 1 =>
          if (attacker.size != Big && defender.size == Big) probability.increase(20)
          else if (attacker.size == Big && defender.size == Small) probability.decrease(20)
          else probability
        case x if x <= 10 && x > 5 =>
          if (attacker.size != Big && defender.size == Big) probability
          else if (attacker.size == Big && defender.size == Small) probability.decrease(50)
          else probability
        case x if x > 10 => probability.increase(75)
      }
    }

    /**
     * Given a probability, it decrease if the defending animal is stronger.
     *
     * @param attacker attacking animal
     * @param defender defending animal
     * @return Output probability of winning the battle
     */
     def calculateProbabilityFromStrength(attacker: Animal, defender: Animal): Probability = {
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
     *
     * @param attacker animal who figth
     * @param defender animal who has been figthed
     * @return probability of the attacker to win
     */
     def calculateProbabilityFromSize(attacker: Animal, defender: Animal): Probability = {
      var probability = Probability(50)
      probability = attacker.size match {
        case Big => defender.size match {
          case Medium => probability.increase(20)
          case Small => probability.increase(50)
          case _ => probability
        }
        case Medium => defender.size match {
          case Big => probability.decrease(20)
          case Small => probability.increase(20)
          case _ => probability
        }
        case Small => defender.size match {
          case Big => probability.decrease(80)
          case Medium => probability.decrease(50)
          case _ => Probability(probability.probability)
        }
      }
      probability
    }

     def isCarnivorous(animal: Animal): Boolean = animal.alimentationType == Carnivore
  }
}