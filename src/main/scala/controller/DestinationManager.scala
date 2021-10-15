package controller

import model._
import utility.Point

sealed trait DestinationManager[P <: Placeable] {
  /**
   * For any given animals, calculate the target of the movement
   * For an herbivore, it should point to the more near vegetables or a random location if it can't see anything
   * For a Carnivore, it should point to the more near herbivore animal, or a random location
   * @return
   */
  def calculateDestination() : Map[Animal, Point]

}

object DestinationManager {
  def apply[P <: Placeable](animals: Seq[Animal], resources: Seq[P], habitat: Habitat) : DestinationManager[P] =
    DestinationManagerImpl(animals, resources, habitat)

private case class DestinationManagerImpl[P <: Placeable](animals: Seq[Animal], food: Seq[P], habitat: Habitat) extends DestinationManager[P] {

  override def calculateDestination(): Map[Animal, Point] = {
    var destination: Map[Animal, Point] = Map.empty
    animals.foreach(animal => {
      if(animal.thirst < 50){
        destination = destination + (animal -> findNearestWaterZone(animal, habitat)
          .getOrElse(throw new IllegalStateException("There's no water in the habitat")))
      } else {
        val point = animal.alimentationType match {
          case Herbivore => findNearestResource(animal, food.filter(resource => resource.isInstanceOf[Vegetable]))
            .getOrElse(getLegalRandomPoint(habitat))
          case Carnivore =>
            findNearestResource(animal, animals.filter(animal => animal.alimentationType == Herbivore)).getOrElse(
              findNearestResource(animal,
                food.filter(resource => resource.isInstanceOf[Meat]))
                .getOrElse(getLegalRandomPoint(habitat)))
        }
        destination = destination + (animal -> point)
      }
    })
    destination
  }

   def findNearestResource[P <: Placeable](animal: Animal, resources: Seq[P]): Option[Point] = {
     resources
       .filter(resource => resource.position.distance(animal.position) < animal.sight)
       .minByOption(resources => resources.position.distance(animal.position)) match {
       case Some(value) => Some(value.position)
       case None => Option.empty
     }
   }

  def getLegalRandomPoint(h: Habitat): Point = {
    val p = Point(0,0).getRandomPoint(h.dimensions)
    if (h.areas.filterNot(a => a.areaType == Fertile).count(a => a.contains(p)) == 0) p else getLegalRandomPoint(h)
  }

  def findNearestWaterZone(animal:Animal, h: Habitat) : Option[Point] = {
    h.areas
      .filter(area => area.areaType == Water)
      .map(area => area.area.topLeft)
      .minByOption(point => point.distance(animal.position))
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