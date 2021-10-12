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


