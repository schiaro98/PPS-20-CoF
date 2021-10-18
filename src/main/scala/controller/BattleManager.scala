package controller

import model._
import utility.Logger

sealed trait BattleManager {

  /**
   * For every animal that is able to see other animals, execute battles
   * @return the Meat that can be released during the battles
   */
  def battle(): Seq[FoodInstance]

  /**
   * Return a sequence of pairs of [[Animal]], the presence in this list means that animal 1 can see animal 2
   * @param seqOfAnimals list of animals to be checked
   * @return a sequence of pairs of animals that can see other animals
   */
  def visibleAnimals(seqOfAnimals: Seq[Animal]): Seq[(Animal, Animal)]
}

object BattleManager {
  def apply(animals: Seq[Animal] = Seq.empty): BattleManager = SimpleBattleManager(animals)


  private case class SimpleBattleManager(animals: Seq[Animal]) extends BattleManager {
    private val logger = Logger

    override def battle(): Seq[FoodInstance] = {
      visibleAnimals(animals)
        .filter(couple => isCarnivorous(couple._1))
        .map(couple => startBattle(couple._1, couple._2))
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
    def startBattle(attacker: Animal, defender: Animal): FoodInstance = {
      require(isCarnivorous(attacker))
      require(attacker.isAlive)
      require(attacker canSee defender)

      val probabilities = List(
        calculateProbabilityFromSize(attacker, defender),
        calculateProbabilityFromDistance(attacker, defender),
        calculateProbabilityFromStrength(attacker, defender)
      )

      if (Probability(probabilities.map(a => a.probability).sum / probabilities.length).calculate) {
        logger.info("Attacking animal: " + attacker.name + " has won against " + defender.name)
        defender.die()
      } else {
        logger.info("Defending animal: " + defender.name + " has won against "  + attacker.name)
        attacker.die()
      }
    }

    /**
     * Given a [[Probability]], it increase if the defending animal is fast (deducted by it's size) and far away. But it can
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
     * Given a [[Probability]], it decrease if the defending animal is stronger.
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
     * Calculate the [[Probability]] that animal 1, attacker wins, given only the two sizes
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

    /**
     * Check the alimentation type of given [[Animal]]
     * @param animal to be checked
     * @return true if the animal is Carnivorous
     */
     def isCarnivorous(animal: Animal): Boolean = animal.alimentationType == Carnivore
  }
}