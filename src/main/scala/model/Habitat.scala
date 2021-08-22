package model

trait Habitat {

  val unexpectedEvent: Int
  val dimensions: (Int, Int)
  val areas: Set[Area]
}

//creare degli enum e delle factory per avere mappe sempre diverse, o mappe statiche