package view

import controller.Serializer._
import model.{Size, Species}

sealed trait Writer

object Writer {

  /*
  TODO avrebbe senso fare una mappa di Map[Species, Int] ma
   */

  val speciesFile = "species.txt"

  val lion: Species = Species("img", "Lion", Size.Medium, 10, 10)
  val ippo: Species = Species("img", "Ippo", Size.Big, 10, 10)
  val zebra: Species = Species("img", "Zebra", Size.Small, 10, 10)

  val basicSpecies = List(lion, ippo, zebra)
  def read: Seq[Species] = deserializeManyFromFile(speciesFile)(classOf[Species])

  def write(): Unit = {
    serializeManyToFile(basicSpecies)(speciesFile)
  }

}
