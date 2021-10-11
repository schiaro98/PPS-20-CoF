package controller

import model.Animal
import utility.Constants

import java.lang.Thread.sleep

case class GameLoopHandler(animals: Seq[Animal], battleManager: BattleManager, shiftManager: ShiftManager) extends Runnable {

  override def run(): Unit = {
    while(animals.lengthIs > 1) {
      val timeStart: Long = System.currentTimeMillis()

      //Prendo dallo shiftmanager il primo movimento per ogni animale
      //TODO shift manager dovrebbe gestire l'eat se l'animale finisce vicino al cibo e acqua??
      //Non credo sia proprio lo shift a dover vedere sta cosa ma vabbe

      battleManager.calculateBattles()

      //Calcolo eventi inaspettati
      //new ShapePanel
      val timeEnd: Long = System.currentTimeMillis()
      println("FPS: " + (timeEnd - timeStart))
      sleep(timeStart + Constants.tickTime - timeEnd)
    }
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
