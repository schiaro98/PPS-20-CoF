package controller

import model.Animal

sealed trait ShiftManager {
  val animals: Seq[Animal]
  //ogni animale puo' star cercando qualcosa o andare verso qualcosa
  //va verso qualcosa che gli e' stato ritornato dal prolog, sia acqua o cibo
}
