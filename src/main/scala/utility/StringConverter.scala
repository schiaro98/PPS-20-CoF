package utility

import model.{Big, Carnivore, Herbivore, Medium, Size, Small, Type}

object StringConverter {
  def getAlimentationType(input: String) : Type = {
    input match {
      case "Herbivore" => Herbivore
      case "Carnivore" => Carnivore
      case _ => null
    }
  }

  def getSize(input: String): Size = {
    input match {
      case "Big" => Big
      case "Medium" => Medium
      case "Small" => Small
      case _ => null
    }
  }
}
