package utility.serializer

import model.food.{FoodCategory, Meat, Vegetable}
import model._
import model.animal.{Big, Carnivore, Herbivore, Medium, Size, Small, Type}

object StringConverter {
  def getAlimentationType(input: String): Type = {
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

  def getFoodCategory(input: String): FoodCategory = {
    input match {
      case "Meat" => Meat
      case "Vegetable" => Vegetable
      case _ => null
    }
  }
}
