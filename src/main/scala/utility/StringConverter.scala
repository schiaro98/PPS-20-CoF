package utility

import model._

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

  // TODO: forse e' meglio lasciarle parzialmente implementate?
  def getFoodType(input: String): FoodType = {
    input match {
      case "MeatType" => MeatType
      case "VegetableType" => VegetableType
      case _ => null
    }
  }
}
