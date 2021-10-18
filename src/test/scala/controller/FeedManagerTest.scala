package controller

import model._
import org.scalatest.funsuite.AnyFunSuite

import java.awt.Color

class FeedManagerTest extends AnyFunSuite{

  val animals: Seq[Animal] = Seq.fill(10)(Animal(Species("tiger", Medium, 10, 10, Carnivore, Color.WHITE), Point(0,0)))
  val foods: Seq[Food] = Seq.fill(10)(FoodInstance() Food(Color.black, 10, MeatType))

  val feedManager: FeedManager = FeedManager(animals, foods)
  test("Animals should has health and thirst value updated "){

  }
}
