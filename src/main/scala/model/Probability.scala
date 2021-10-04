package model

import scala.util.Random

trait Probability {

  val probability: Int

  def calculate: Boolean
  //could use implicits for different strategies?
  //pattern strategy?

  def increase(x: Int): Probability
  def decrease(x: Int): Probability
}

object Probability {
  def apply(probability: Int): Probability = ProbabilityImpl(probability)

  private case class ProbabilityImpl(override val probability: Int) extends Probability {
    require(probability >= 0 && probability <= 100 )

    override def calculate: Boolean = probability match {
      case 0 => false
      case _ => probability - new Random().nextInt(100) > 0
    }

    override def increase(x: Int): Probability = ProbabilityImpl(probability + ((probability * x) / 100))

    override def decrease(x: Int): Probability = ProbabilityImpl(probability - ((probability * x) / 100))

  }
}