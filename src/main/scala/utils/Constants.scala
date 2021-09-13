package utils

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Constants {

  //Food
  val iconOfMeat: BufferedImage = ImageIO.read(new File("res/images/tiger-icon.jpg"))
  val energyOfMeat = 20

  val iconOfVegetable: BufferedImage = ImageIO.read(new File("res/images/tiger-icon.jpg"))
  val energyOfVegetable = 10


  //Animal
  val maxHealth = 300
  val maxThirst = 100

  val quantityForBig = 8
  val quantityForMedium = 5
  val quantityForSmall = 2
}
