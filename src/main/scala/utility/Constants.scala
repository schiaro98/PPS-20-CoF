package utility

import scala.swing.Dimension

object Constants {
  val FoodsFilePath = "foods.txt"


  // ------- Food -------
  val DefaultFoodQuantity = 10

  val DefaultIconOfMeat = "res/images/tiger-icon.jpg"
  val DefaultEnergyOfMeat = 20

  val DefaultIconOfVegetable ="res/images/tiger-icon.jpg"
  val DefaultEnergyOfVegetable = 10


  // ------- Animal -------
  val MaxHealth = 300
  val MaxThirst = 100

  val QuantityForBig = 8
  val QuantityForMedium = 5
  val QuantityForSmall = 2

  val MaxShift = 15

  // ------- Gui --------
  val defaultSimulationDimension = new Dimension(1000, 1000)

  val mainMap = "map.txt"
  val mainMapDimension = (500, 500)
}
