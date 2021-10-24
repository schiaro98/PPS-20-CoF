package utility

import java.awt.Color

/**
 * Object used to store all the constants used in the code in one file
 */
object Constants {

  // ------- Text files -------
  val FoodsFilePath = "foodTypes.json"
  val MainMap = "map.json"
  val SavedSpecies = "species.json"

  // ------- Food -------
  val DefaultFoodQuantity = 10

  val DefaultColorOfMeat = new Color(138,0,0)
  val DefaultEnergyOfMeat = 20

  val DefaultColorOfVegetable = new Color(0,84,11)
  val DefaultEnergyOfVegetable = 10

  val PixelForFood = 8

  // ------- Animal -------
  val InitialNumForAnimals = 5

  val MaxHealth = 300
  val HealthDecrease = 5

  val MaxThirst = 100
  val ThirstDecrease = 2

  val QuantityForBig = 8
  val QuantityForMedium = 5
  val QuantityForSmall = 2

  val MaxShiftForHerbivore = 15
  val MaxShiftForCarnivore = 20
  val MinShift = 5

  val ProbabilityForYoung = 0
  val ProbabilityForAdult = 20
  val ProbabilityForOld = 40

  val PixelForBig = 12
  val PixelForMedium = 10
  val PixelForSmall = 8

  val HitBox = 5

  // ------- Gui --------
  val MainMapDimension: (Int, Int) = (500, 500)

  val PopupOffsetX = 20
  val PopupOffsetY = 45

  val Period = 1000
  val SpeedUpPeriod: Int = Period / 10

  // ------- Habitat ------
  val DefaultGridSize = 16
  val MaxDimension = 600

  //------- ResourceManager ----
  val MaxFoods = 80
  val InitialFoods = 50
}
