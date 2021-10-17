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

  val DefaultColorOfMeat = new Color(82,0,0)
  val DefaultEnergyOfMeat = 20

  val DefaultColorOfVegetable = new Color(0,84,11)
  val DefaultEnergyOfVegetable = 10


  // ------- Animal -------
  val MaxHealth = 300
  val MaxThirst = 100

  val QuantityForBig = 8
  val QuantityForMedium = 5
  val QuantityForSmall = 2

  val MaxShift = 15
  val MinShift = 5

  val PixelForBig = 12
  val PixelForMedium = 9
  val PixelForSmall = 6

  val PixelForFood = 6

  val IncCarnivoreVelocity = 5

  val hitbox = 3
  // ------- Gui --------
  val DefaultSimulationDimension = new Dimension(1000, 1000)
  val MainMapDimension: (Int, Int) = (500, 500)

  val DefaultStartingX = 100
  val DefaultStartingY = 100

  val OffsetX = 20
  val OffsetY = 45

  val Period = 3000 // 10000 = una epoca ogni 10 secondi (15 per 60fps?)

  // ------- Habitat ------
  val DefaultRandomSize = 10
  val DefaultGridSize = 100

  val NonWalkableArea: Set[AreaType] = Set(Volcano,Rock,Water)

  // ------- Various ------
  val MillisToSec = 1000
}
