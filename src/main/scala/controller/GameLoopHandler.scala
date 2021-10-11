package controller

import model.Animal
import utility.Constants

import java.lang.Thread.sleep

case class GameLoopHandler(animals: Seq[Animal]) extends Runnable {
  override def run(): Unit = {
    while(animals.lengthIs > 1) {
      val timeStart: Long = System.currentTimeMillis()
      //Prendo dallo shiftmanager il primo movimento per ogni animale
      //TODO shift manager dovrebbe gestire l'eat se l'animale finisce vicino al cibo e acqua??
      //Battle manager calcola possibili
      //Calcolo eventi inaspettati
      //new ShapePanel
      val timeEnd: Long = System.currentTimeMillis()
      println("FPS: " + (timeEnd - timeStart))
      sleep(timeStart + Constants.tickTime - timeEnd)
      //sleep
    }
  }
}
