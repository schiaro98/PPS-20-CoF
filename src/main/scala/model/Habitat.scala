package model

trait Habitat {
  val unexpectedEvents: Probability
  val dimensions: (Int, Int)
  val areas: Seq[Area]

  def checkForOverlappingAreas(areas: Seq[Area]): Boolean = areas match {
    case h :: t =>
      for (area <- areas.filterNot(elem => elem == h)) {
        if (h.topLeft._1 > area.topLeft._1 && h.topLeft._1 < area.bottomRight._1 && h.topLeft._2 > area.topLeft._2 && h.topLeft._2 < area.bottomRight._2 ||
          h.bottomRight._1 > area.topLeft._1 && h.bottomRight._1 < area.bottomRight._1 && h.bottomRight._2 > area.topLeft._2 && h.bottomRight._2 < area.bottomRight._2 ||
          h.topLeft._1 == area.topLeft._1 && h.topLeft._2 == area.topLeft._2 || h.bottomRight._1 > area.bottomRight._1 && h.bottomRight._2 < area.bottomRight._2) {
          return false
        }
      }
      checkForOverlappingAreas(t)
    case _ => true
  }

  def checkDimensionsOfAreas(areas: Seq[Area]): Boolean = areas match {
    case h :: t =>
      if (h.topLeft._1 < 0 || h.topLeft._2 < 0 ||
        h.bottomRight._1 > this.dimensions._1 || h.bottomRight._2 > this.dimensions._2) {
        return false
      }
      checkDimensionsOfAreas(t)
    case _ => true
  }

  require(checkForOverlappingAreas(areas), "overlapping areas")
  require(checkDimensionsOfAreas(areas), "areas don't fit in current habitat")
}

object Habitat {


  def apply(unexpectedEvents: Probability,
            dimensions: (Int, Int),
            areas: Seq[Area] = Seq.empty): Habitat = SimpleHabitat(unexpectedEvents, dimensions, areas)

  private case class SimpleHabitat(override val unexpectedEvents: Probability,
                                   override val dimensions: (Int, Int),
                                   override val areas: Seq[Area]) extends Habitat
}

//creare degli enum e delle factory per avere mappe sempre diverse, o mappe statiche