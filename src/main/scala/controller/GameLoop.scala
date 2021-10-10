package controller

import model.{Animal, FoodInstance, Habitat}

class GameLoop(val animalsInMap : Seq[Animal], val foodInMap: Seq[FoodInstance], val habitat: Habitat){

}
