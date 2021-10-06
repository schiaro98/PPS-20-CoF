package controller

import model.{Habitat, Probability, RandomHabitatType}

/**
 * This class generates a playable map of animals and areas
 */
class MapGenerator(animals: Map[String, Int]) {
  val habitat: Habitat = Habitat(RandomHabitatType, Probability(10),(100, 100),Seq.empty)

}
