package model

import scala.util.Random

//TODO aggiungere scaladoc in trait, object e metodi
trait Probability {

  val probability: Int

  /**
   *
   * @return a boolean based on the probability taken in input
   */
  def calculate: Boolean

  def increase(x: Int): Probability

  def decrease(x: Int): Probability
}

object Probability {

  /**
   *
   * @param probability an Int between 0 and 100
   * @return an implementation of [[Probability]]
   */
  def apply(probability: Int): Probability = ProbabilityImpl(probability)

  private case class ProbabilityImpl(override val probability: Int) extends Probability {
    //the probability needs to be between 0 and 100
    require(probability >= 0 && probability <= 100 )

    override def calculate: Boolean = probability match {
      case 0 => false
      case _ => probability - new Random().nextInt(100) > 0
    }

    override def increase(x: Int): Probability = ProbabilityImpl(probability + ((probability * x) / 100))

    override def decrease(x: Int): Probability = ProbabilityImpl(probability - ((probability * x) / 100))

  }
}