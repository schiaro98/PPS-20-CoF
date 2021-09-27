package controller

import model.Animal

sealed trait BattleManager {
  val animals: Seq[Animal]
}

object BattleManager {

}