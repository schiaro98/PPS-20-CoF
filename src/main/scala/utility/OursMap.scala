package utility

import model.habitat._
import model.position.Point
import model.shape.RectangleArea
import model.{Probability, shape}

object OursMap {
  val areas = List(
    Area(Volcano, RectangleArea(Point(0,0), Point(100, 100))),
    Area(Volcano, shape.RectangleArea(Point(100,0), Point(200, 50))),
    Area(Volcano, shape.RectangleArea(Point(400,450), Point(500, 500))),

    Area(Rock, shape.RectangleArea(Point(200,0), Point(300, 50))),
    Area(Rock, shape.RectangleArea(Point(300,150), Point(350, 200))),
    Area(Rock, shape.RectangleArea(Point(150,450), Point(200, 500))),
    Area(Rock, shape.RectangleArea(Point(200,400), Point(250, 500))),
    Area(Rock, shape.RectangleArea(Point(470,150), Point(500, 280))),

    Area(Water, shape.RectangleArea(Point(400, 0), Point(450, 50))),
    Area(Water, shape.RectangleArea(Point(450, 0), Point(500, 100))),
    Area(Water, shape.RectangleArea(Point(0, 200), Point(200, 250))),
    Area(Water, shape.RectangleArea(Point(300, 300), Point(350, 400))),
    Area(Water, shape.RectangleArea(Point(350, 300), Point(400, 350))),
    Area(Water, shape.RectangleArea(Point(50, 400), Point(100, 450))),

    Area(Fertile, shape.RectangleArea(Point(300, 0), Point(400, 50)), Probability(50)), //1
    Area(Fertile, shape.RectangleArea(Point(100, 50), Point(450, 150)), Probability(30)),
    Area(Fertile, shape.RectangleArea(Point(0, 100), Point(100, 200)), Probability(60)),
    Area(Fertile, shape.RectangleArea(Point(100, 150), Point(300, 200)), Probability(50)),
    Area(Fertile, shape.RectangleArea(Point(450, 100), Point(500, 150)), Probability(60)), //5
    Area(Fertile, shape.RectangleArea(Point(200, 200), Point(350, 300)), Probability(60)),
    Area(Fertile, shape.RectangleArea(Point(350, 150), Point(470, 300)), Probability(20)),
    Area(Fertile, shape.RectangleArea(Point(0, 250), Point(200, 400)), Probability(80)),
    Area(Fertile, shape.RectangleArea(Point(200, 300), Point(300, 400)), Probability(60)),
    Area(Fertile, shape.RectangleArea(Point(400, 300), Point(470, 350)), Probability(60)), //10
    Area(Fertile, shape.RectangleArea(Point(470, 280), Point(500, 400)), Probability(20)),
    Area(Fertile, shape.RectangleArea(Point(350, 350), Point(470, 400)), Probability(60)),
    Area(Fertile, shape.RectangleArea(Point(0, 400), Point(50, 500)), Probability(60)),
    Area(Fertile, shape.RectangleArea(Point(100, 400), Point(200, 450)), Probability(40)),
    Area(Fertile, shape.RectangleArea(Point(50, 450), Point(150, 500)), Probability(40)), //15
    Area(Fertile, shape.RectangleArea(Point(250, 400), Point(300, 500)), Probability(20)),
    Area(Fertile, shape.RectangleArea(Point(300, 400), Point(400, 500)), Probability(40)),
    Area(Fertile, shape.RectangleArea(Point(400, 400), Point(500, 450)), Probability(20)),
  )
}
