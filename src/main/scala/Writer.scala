import Serializer.{deserializeManyFromFile, serializeManyToFile}
import model.{Size, Species}

sealed trait Writer

object Writer {

  /*
  TODO avrebbe senso fare una mappa di Map[Species, Int] ma
   */

  val speciesFile = "species.txt"

  def read: Seq[Species] = deserializeManyFromFile(speciesFile)(classOf[Species])

  /*
  def write(): Unit = {
    serializeManyToFile(species)(speciesFile)
  }
  */
}
