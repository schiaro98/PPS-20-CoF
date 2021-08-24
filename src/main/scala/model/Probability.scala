package model

import scala.util.Random

trait Probability {

  val probability: Int

  def calculate: Boolean
  //could use implicits for different strategies?
  //pattern strategy?
}

object Probability {
  def apply(probability: Int): Probability = new ProbabilityImpl(probability)

  private class ProbabilityImpl(override val probability: Int) extends Probability {
    require(probability >=0 && probability <= 100 )

    override def calculate: Boolean = probability match {
      case 0 => false
      case _ => probability - new Random().nextInt(100) > 0
    }
  }
}