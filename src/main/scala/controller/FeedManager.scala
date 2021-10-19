package controller

import model._
import utility.Constants

import scala.annotation.tailrec

sealed trait FeedManager {

  /**
   * Consume the nearest resource of the animal in the map
   * It manage also the decrease of health and thirst
   * @return a pair of the sequence of the animals updated and of the food still eatable
   */
  def consumeResources(): (Seq[Animal],Seq[FoodInstance])
}

object FeedManager {
  def apply(animals: Seq[Animal], resources: Seq[FoodInstance], habitat: Habitat = Habitat()): FeedManager = SimpleFeedManager(animals, resources, habitat)

  private case class SimpleFeedManager(animals: Seq[Animal], resources: Seq[FoodInstance], habitat: Habitat) extends FeedManager {

    override def consumeResources():(Seq[Animal],Seq[FoodInstance]) = {

      @tailrec
      def _consumeResources(animals: Seq[Animal],
                            resources: Seq[FoodInstance],
                            updatedAnimals: Seq[Animal] = Seq.empty) : (Seq[Animal],Seq[FoodInstance]) = animals match {
        case h :: t =>

          val nearestWaterArea = findNearestWaterZone(h, habitat)
          val myAnimal = if(nearestWaterArea.isDefined){
            h.drink()
          } else h

          //Contiene la risorsa più vicina all'animale, deve essere dentro alla hitbox
          val nearestResource = resources
            .filter(_.position.distance(myAnimal.position) < Constants.hitbox)
            .minByOption(_.position.distance(myAnimal.position))

          //Se l'animale ha un cibo mangiabile nell'hitbox
          if(nearestResource.isDefined) {

            //Se il cibo è una verdura e l'animale erbivoro
            // //oppure è una carne e l'animale carnivoro
            //Provo a consumare la risorsa
            nearestResource.get match {
              case x: FoodInstance if x.foodType == Meat && myAnimal.alimentationType == Carnivore || x.foodType == Vegetable && myAnimal.alimentationType == Herbivore =>

                //updatedAnimal contiene l'animale con i valori aggiornati dopo aver mangiato

                //remainedFood contiene un Option vuoto se non è rimasto cibo
                // o un option di FoodInstance se il cibo è avanzato
                val (updatedAnimal ,remainedFood) = myAnimal.eat(x)
                println(updatedAnimal + "has eat")
                //Se il cibo è avanzato, ciclo la lista di animali rimanenti,
                // togliendo il cibo vecchio (Con quantità piena) e mettendo quello avanzato
                if(remainedFood.isDefined) {
                  _consumeResources(t, resources.filterNot(_ == x) :+ remainedFood.get , updatedAnimals :+ updatedAnimal)
                } else {
                  _consumeResources(t, resources.filterNot(_ == x), updatedAnimals :+ updatedAnimal)
                }
              case _ =>
                println("ERROR: Trying to do something impossible")
                (updatedAnimals, resources)
            }
          } else {
            //Se l'animale NON ha un cibo mangiabile nell'hitbox
            //ciclo sugli animali rimanenti, sul cibo e aggiungo un animale agli aggiornati
            _consumeResources(t, resources, updatedAnimals :+ myAnimal)
          }
        case _ =>
          //Se finisco la lista, ritorno i cibi avanzati
          (updatedAnimals, resources)

      }
      _consumeResources(animals, resources)
    }

    def findNearestWaterZone(animal: Animal, h: Habitat): Option[Point] = {
      h.areas
        .filter(_.areaType == Water)
        .map(rectangle => rectangle.area.topLeft)
        .filter(_.distance(animal.position) < Constants.hitbox)
        .minByOption(_.distance(animal.position))
    }
  }
}