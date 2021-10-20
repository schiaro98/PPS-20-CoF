package utility

import model.{AreaType, Rock, Volcano, Water}

import java.awt.Color
import scala.swing.Dimension

/**
 * Object used to store all the constants used in the code in one file
 */
object Constants {

  // ------- Text files -------
  val FoodsFilePath = "foods.txt"
  val MainMap = "map.txt"
  val SavedSpecies = "species.txt"

  // ------- Food -------
  val DefaultFoodQuantity = 10

  val DefaultColorOfMeat = new Color(138,0,0)
  val DefaultEnergyOfMeat = 20

  val DefaultColorOfVegetable = new Color(0,84,11)
  val DefaultEnergyOfVegetable = 10

  // ------- Animal -------
  val InitialNumForAnimals = 5

  val MaxHealth = 300
  val healthDecrease = 5

  val MaxThirst = 100
  val thirstDecrease = 2

  val QuantityForBig = 8
  val QuantityForMedium = 5
  val QuantityForSmall = 2

  val MaxShiftForHerbivore = 15
  val MaxShiftForCarnivore = 20
  val MinShift = 5

  val PixelForBig = 12
  val PixelForMedium = 10
  val PixelForSmall = 8

  val PixelForFood = 8

  val Hitbox = 5
  val waterHitbox = 10

  // ------- Gui --------
  val DefaultSimulationDimension = new Dimension(1000, 1000)
  val MainMapDimension: (Int, Int) = (500, 500)

  val DefaultStartingX = 100
  val DefaultStartingY = 100

  val OffsetX = 20
  val OffsetY = 45

  val Period = 1000
  val SpeedUpPeriod: Int = Period / 10

  // ------- Habitat ------
  val DefaultRandomSize = 10
  val DefaultGridSize = 100

  val MaxDimension = 1_000

  val NonWalkableArea: Set[AreaType] = Set(Volcano,Rock,Water) //TODO gestire questo nel model ?

  val FoodToGrowPerFertileArea: Int = 1


  //------- ResourceManager ----
  val MaxFoodInstances = 80
}
