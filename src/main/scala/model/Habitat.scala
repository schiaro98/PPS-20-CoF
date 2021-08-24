package model

trait Habitat {
  val unexpectedEvents: Probability
  val dimensions: (Int, Int)
  val walkableAreas: Seq[WalkableArea]
  val nonWalkableAreas: Seq[NonWalkableArea]
}

object Habitat {
  def apply(
             unexpectedEvents: Probability,
             dimensions: (Int, Int),
             walkableAreas: Seq[WalkableArea] = Seq.empty,
             nonWalkableAreas: Seq[NonWalkableArea] = Seq.empty): Habitat = new SimpleHabitat(unexpectedEvents, dimensions, walkableAreas, nonWalkableAreas)

  private class SimpleHabitat( override val unexpectedEvents: Probability,
                               override val dimensions: (Int, Int),
                               override val walkableAreas: Seq[WalkableArea],
                               override val nonWalkableAreas: Seq[NonWalkableArea]) extends Habitat {

  }
}

//creare degli enum e delle factory per avere mappe sempre diverse, o mappe statiche