package controller

import model.{Animal, Probability, Size, Type}

sealed trait BattleManager {
  val animals: Seq[Animal]

  def visibleAnimals() : Seq[(Animal, Animal)]

  def visibleAnimals(animals: Seq[Animal]) : Seq[(Animal, Animal)]

  def battle(probability: Probability) : Boolean

  def calculateProbabilityFromSize(a1: Animal, a2: Animal) : Probability

  def calculateProbabilityFromDistance(a1: Animal, a2: Animal) : Probability

  def calculateProbabilityFromStrength(a1: Animal, a2: Animal) : Probability

  def startBattle(a1: Animal, a2: Animal): Unit

  def calculateBattles(): Unit

  def isCarnivorous(animal: Animal): Boolean
}

object BattleManager {
  def apply(animals: Seq[Animal]) : BattleManager = SimpleBattleManager(animals)
  def apply() : BattleManager = SimpleBattleManager(Seq.empty)
}