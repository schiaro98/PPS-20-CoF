package view

import controller.{OfArea, Serializer}
import model.{Area, Habitat, HabitatType, Probability, SimpleHabitatType}
import utility.Constants

class ChooseHabitatLogic(val species:Map[String, Int]) {

  def createHabitat(ht: HabitatType, w: String, h: String, ue: String, areas: Seq[Area] = Seq.empty): Option[Habitat] =
    if (isNumber(w) && isNumber(h) && isNumber(ue) && ue.toInt >= 0  && ue.toInt <= 100) {
      if (ht == SimpleHabitatType){
        Some(Habitat(Probability(ue.toInt)))
      } else Some(Habitat(ht, Probability(ue.toInt), (w.toInt, h.toInt), areas))
    } else None


  private def isNumber(x:String): Boolean = !x.isBlank && x.nonEmpty && isAllDigits(x)

  private def isAllDigits(x: String): Boolean = x forall Character.isDigit

}