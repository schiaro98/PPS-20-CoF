package view

import model.{Habitat, HabitatType, Probability}

class ChooseHabitatLogic(val species:Map[String, Int]) {

  def createHabitat(ht: HabitatType, w: String, h: String, ue: String): Option[Habitat] =
    if (isNumber(w) && isNumber(h) && isNumber(ue) && ue.toInt >= 0  && ue.toInt <= 100) {
      //TODO Seq.empty l'ho messo a caso, ma senza non funziona perchè non esiste un apply adeguato
      Some(Habitat(ht, Probability(ue.toInt), (w.toInt, h.toInt), Seq.empty))
    } else None

  private def isNumber(x:String): Boolean = !x.isBlank && x.nonEmpty && isAllDigits(x)

  private def isAllDigits(x: String): Boolean = x forall Character.isDigit

}