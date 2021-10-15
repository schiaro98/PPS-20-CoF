import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.Point

import java.awt.Color

class VisualizableTest extends AnyFunSuite {

  val color: Color = Color.black

  test("A visualizable must provide an image") {
    val v: Visualizable = Food(color, 0)
    assert(color == v.color)
  }

  test("A species is a visualizable with more information") {
    val s: Species = Species("tiger", Medium, 10, 10, Carnivore)
    assert(color == s.color)
    assert("tiger" == s.name )
  }

  test("A Vegetable is a visualizable with energy and quantity") {
    val f: Food = Vegetable(10, Point(0,0), 10, color)
    assert(color == f.color)
    assert(10 == f.energy )
  }
}
