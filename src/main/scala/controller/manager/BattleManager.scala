package controller.manager

import model.Probability
import model.animal.{Animal, Big, Carnivore, Medium, Small}
import model.food.Food
import utility.Logger

import scala.annotation.tailrec

/**
 * A controller for battling the [[Animal]] present in the map
 */
sealed trait BattleManager {

  /**
   * For every animal that is able to see other animals, execute battles.
   * The probability of winning a battle is calculated by different factors, like the strength, size and distance.
   *
   * @return the Meat that can be released during the battles, and the list of [[Animal]] updated
   */
  def battle(): (Seq[Animal],Seq[Food])

}

object BattleManager {
  def apply(animals: Seq[Animal] = Seq.empty): BattleManager = SimpleBattleManager(animals)

  private case class SimpleBattleManager(animals: Seq[Animal]) extends BattleManager {
    private val logger = Logger

    def battle(): (Seq[Animal],Seq[Food]) = {
      @tailrec
      def _battle(animals: Seq[Animal],
                  meats: Seq[Food] = Seq.empty,
                  animalUpdated: Seq[Animal] = Seq.empty): (Seq[Animal], Seq[Food]) = animals match {
        case attacker :: t =>
          val enemyOpt = animals
            .filter(_.species.alimentationType == Carnivore)
            .filter(_.position.distance(attacker.position) < attacker.species.sight)
            .minByOption(_.position.distance(attacker.position))

          if(enemyOpt.isDefined){
            val enemy = enemyOpt.get
            val isBattleWin = List(
              calculateProbabilityFromSize(attacker, enemy),
              calculateProbabilityFromDistance(attacker, enemy),
              calculateProbabilityFromStrength(attacker, enemy)
            ).map(p => p.calculate).count(p => p) > 1

            if (isBattleWin) {
              logger.info("Attacking animal: " + attacker.species.name + " has won against " + enemy.species.name)
              _battle(t, meats :+ enemy.die(), animalUpdated :+ attacker)
            } else {
              logger.info("Defending animal: " + enemy.species.name + " has won against "  + attacker.species.name)
              _battle(t, meats :+ attacker.die(), animalUpdated :+ enemy)
            }
          } else {
            _battle(t, meats, animalUpdated :+ attacker)
          }
        case _ => (animalUpdated, meats)
      }
      _battle(animals)
    }

    /**
     * Given a [[Probability]], it increase if the defending animal is fast (deducted by it's size) and far away.
     * But it can increase if the attacking animal is near (or fast)
     *
     * @param attacker attacking animal
     * @param defender defending animal
     * @return Output probability of winning the battle
     */
     def calculateProbabilityFromDistance(attacker: Animal, defender: Animal): Probability = {
      val probability = Probability(50)
      attacker.position.distance(defender.position) match {
        case x if x <= 1 => probability.increase(50)
        case x if x <= 5 && x > 1 =>
          if (attacker.species.size != Big && defender.species.size == Big) probability.increase(20)
          else if (attacker.species.size == Big && defender.species.size == Small) probability.decrease(20)
          else return probability
        case x if x <= 10 && x > 5 =>
          if (attacker.species.size != Big && defender.species.size == Big) return probability
          else if (attacker.species.size == Big && defender.species.size == Small) probability.decrease(50)
          else return probability
        case x if x > 10 => probability.increase(75)
      }
       probability
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
      attacker.species.strength - defender.species.strength match {
        case x if x > 5 => probability.increase(50)
        case x if x > 0 && x <= 5 => probability.increase(20)
        case x if x <= 0 && x > -5 => probability.decrease(20)
        case x if x <= -5 => probability.decrease(50)
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
      probability = attacker.species.size match {
        case Big => defender.species.size match {
          case Medium => probability.increase(20)
          case Small => probability.increase(50)
          case _ => probability
        }
        case Medium => defender.species.size match {
          case Big => probability.decrease(20)
          case Small => probability.increase(20)
          case _ => probability
        }
        case Small => defender.species.size match {
          case Big => probability.decrease(80)
          case Medium => probability.decrease(50)
          case _ => probability
        }
      }
      probability
    }
  }
}