package controller

import model.{Animal, FoodInstance, Type}
import utility.Point

sealed trait DestinationManager {
  /**
   * For any given animals, calculate the target of the movement
   * For an herbivore, it should point to the more near vegetables or a random location if it can't see anything
   * For a Carnivore, it should point to the more near herbivore animal, or a random location
   * @param animals to be moved into a location
   * @param resources the resources present in the map
   * @return
   */
  def calculateDestination(animals: Seq[Animal],resources: Seq[FoodInstance]) : Map[Animal, Point]
}

object DestinationManager {
  def apply(animals: Seq[Animal], resources: Seq[FoodInstance]) : DestinationManager = DestinationManagerImpl(animals, resources)

private case class DestinationManagerImpl(animals: Seq[Animal], resources: Seq[FoodInstance]) extends DestinationManager {
  override def calculateDestination(animals: Seq[Animal], resources: Seq[FoodInstance]): Map[Animal, Point] = {
    val destination: Map[Animal, Point] = Map.empty
    animals.foreach(animal => {
      val animalPos = animal.position
      animal.alimentationType match {
        case Type.Herbivore => findNextResource(animal, resources).getOrElse())
        case Type.Carnivore =>
      }
    })
  }

  private def findNextResource(animal: Animal, resources: Seq[FoodInstance]): Option[Point] = {
    var nextResource: Option[Point] = Option.empty
    resources.foreach(res => {
      val distance = res.position.distance(animal.position)
      if (distance < animal.sight){
        if(nextResource.get.distance(animal.position) > distance){
          nextResource = Some(res.position)
        }
      }
    }
    )
    nextResource
  }

  private def getRandomMovement(animal: Animal): Point = {
    val actualPosition = animal.position
    val shift: Int = 10 //TODO massimo spostamento per un animale? oppure si può anche mettere qualsiasi punto nei limiti della mappa
    actualPosition.getRandomPoint(actualPosition, actualPosition + Point(shift, shift))
  }
}

/*
 * Perchè spostare gli animali, per farli avvicinare a risorse(cibo, acqua), per farli scappare? e per farli combattere
 * con altri animali oppure casualmente. Il come ci pensa lo shiftmanager. Il destinationManager si occupa di vedere se l'animale
 * è in grado di "vedere" altre risorse o bersagli. In caso delle risorse si avvicina. Se vedo una preda mi avvicino.
 * Se vedo un attaccante scappo?
 * Ogni animale, se non ha altro motivo per muoversi, si muove randomicamente in una direzione
 *
 * Per ogni animale quindi prendo la posizione e ne tiro fuori un altra calcolata in base alle varie condizioni
 * (cose che vede o random)
 *
 * Se vedo delle risorse gli do come destinazione quella più vicine, se vedo una preda gli do come destinazione la preda
 *
 * I CARNIVORI SONO PIU' veloci, i carnivori hanno una hitbox, cioè una distanza massima entro il quale le prede vengono
 *  uccise decapitate molestate per prenderne la carne
 */
}