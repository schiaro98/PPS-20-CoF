package model

import scala.util.Random

/**
 * Trait that represent a probability.
 */
trait Probability {

  /**
   *
   * @return a boolean based on the probability taken in input.
   */
  def calculate: Boolean

  /**
   * Increase the probability of the given percentage.
   *
   * @param x the percentage used to increment the Probability.
   * @return a new Probability.
   */
  def increase(x: Int): Probability

  /**
   * Decrease the probability of the given percentage.
   *
   * @param x the percentage used to decrease the Probability.
   * @return a new Probability.
   */
  def decrease(x: Int): Probability
}

object Probability {

  /**
   * Apply method for [[Probability]].
   *
   * @param probability an [[Int]] between 0 and 100.
   * @return an implementation of [[Probability]].
   */
  def apply(probability: Int): Probability = ProbabilityImpl(probability)

  private case class ProbabilityImpl(probability: Int) extends Probability {
    require(probability >= 0 && probability <= 100 )

    override def calculate: Boolean = probability match {
      case 0 => false
      case _ => probability - new Random().nextInt(100) > 0
    }

    override def increase(x: Int): Probability = Probability((probability + ((probability * x) / 100)).min(100))

    override def decrease(x: Int): Probability = Probability((probability - ((probability * x) / 100)).max(0))
  }
}