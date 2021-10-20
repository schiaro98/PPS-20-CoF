package utility

import org.scalatest.funsuite.AnyFunSuite

class StatisticsTest extends AnyFunSuite{

  //We are testing a singleton so run all tests together or they won't pass

  test("Test add first stats"){
    Statistics.restart()
    Statistics.update(deathForNaturalCause = 1)
    Statistics.update(0,0,0,0,41)
    assert(Statistics.map(0).deathForNaturalCause == 42)
  }

  test("test add time 1"){
    Statistics.restart()
    Statistics.incTime()
    Statistics.update(foods = 1)
    assert(Statistics.map(Statistics.time).foods == 1)
  }

  test("Adding others value in time 1"){
    Statistics.update(foods = 1)
    Statistics.update(foodEaten = 1)
    assert(Statistics.map(Statistics.time).foods == 2 )
    assert(Statistics.map(Statistics.time).foodEaten == 1 )
  }

}
