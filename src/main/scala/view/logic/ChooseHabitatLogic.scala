package view.logic

import model.animal.Species
import model.habitat.{Area, Habitat, HabitatType, SimpleHabitatType}
import model._
import utility.Constants

class ChooseHabitatLogic(val species:Map[Species, Int]) {

  def createHabitat(ht: HabitatType, w: String, h: String, ue: String, areas: Seq[Area] = Seq.empty): Option[Habitat] =
    if (isNumber(w) && isNumber(h) && isNumber(ue)
      && ue.toInt >= 0  && ue.toInt <= 10
      && w.toInt >= 0 && w.toInt <= Constants.MaxDimension
      && h.toInt >= 0 && h.toInt <= Constants.MaxDimension
    ) {
      if (ht == SimpleHabitatType){
        Some(habitat.Habitat(Probability(ue.toInt)))
      } else Some(habitat.Habitat(ht, Probability(ue.toInt), (w.toInt, h.toInt), areas))
    } else None


  private def isNumber(x:String): Boolean = !x.isBlank && x.nonEmpty && isAllDigits(x)

  private def isAllDigits(x: String): Boolean = x forall Character.isDigit

}