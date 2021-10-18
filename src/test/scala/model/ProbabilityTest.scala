package model

import org.scalatest.funsuite.AnyFunSuite

class ProbabilityTest extends AnyFunSuite{

  test("Test 0%"){
    val nTimes = 100
    val probability: Probability = Probability(0)
    val results = calculateNTimes(nTimes, probability)
    val trues = results._1
    val falsies = results._2
    assert(trues == 0)
    assert(falsies == nTimes)
  }

  test("100%"){
    val nTimes = 100
    val probability: Probability = Probability(100)
    val results = calculateNTimes(nTimes, probability)
    val trues = results._1
    val falsies = results._2
    assert(trues == nTimes)
    assert(falsies == 0)
  }

  test("25%"){
    val probability: Probability = Probability(25)
    val results = calculateNTimes(100, probability)
    val trues = results._1
    val falsies = results._2
    assert(trues < falsies)
  }

  test("75%"){
    val probability: Probability = Probability(75)
    val results = calculateNTimes(100, probability)
    val trues = results._1
    val falsies = results._2
    assert(trues > falsies)
  }

  test("IllegalArgument"){
    assertThrows[IllegalArgumentException](Probability(1000))
    assertThrows[IllegalArgumentException](Probability(-1000))
  }

  def calculateNTimes(n: Int, probability: Probability, trues:Int =0, falsies: Int = 0): (Int, Int) = n match {
    case 0 => (trues, falsies)
    case _ => if (probability.calculate) calculateNTimes(n-1, probability, trues+1, falsies) else calculateNTimes(n-1, probability, trues, falsies+1)
  }
}
