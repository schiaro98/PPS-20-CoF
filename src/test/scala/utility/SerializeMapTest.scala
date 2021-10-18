package utility

import model._
import org.scalatest.funsuite.AnyFunSuite

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import scala.reflect.io.File

class SerializeMapTest extends AnyFunSuite {

  val defaultSerializer: Serializer = Serializer(DefaultSerializer)
  val areasSerializer: Serializer = Serializer(OfArea)

  val areas = List(
    Area(Volcano, RectangleArea(Point(0,0), Point(100, 100))),
    Area(Volcano, RectangleArea(Point(100,0), Point(200, 50))),
    Area(Volcano, RectangleArea(Point(400,450), Point(500, 500))),

    Area(Rock, RectangleArea(Point(200,0), Point(300, 50))),
    Area(Rock, RectangleArea(Point(300,150), Point(350, 200))),
    Area(Rock, RectangleArea(Point(150,450), Point(200, 500))),
    Area(Rock, RectangleArea(Point(200,400), Point(250, 500))),
    Area(Rock, RectangleArea(Point(470,150), Point(500, 280))),

    Area(Water, RectangleArea(Point(400, 0), Point(450, 50))),
    Area(Water, RectangleArea(Point(450, 0), Point(500, 100))),
    Area(Water, RectangleArea(Point(0, 200), Point(200, 250))),
    Area(Water, RectangleArea(Point(300, 300), Point(350, 400))),
    Area(Water, RectangleArea(Point(350, 300), Point(400, 350))),
    Area(Water, RectangleArea(Point(50, 400), Point(100, 450))),

    Area(Fertile, RectangleArea(Point(300, 0), Point(400, 50)), Probability(50)), //1
    Area(Fertile, RectangleArea(Point(100, 50), Point(450, 150)), Probability(30)),
    Area(Fertile, RectangleArea(Point(0, 100), Point(100, 200)), Probability(60)),
    Area(Fertile, RectangleArea(Point(100, 150), Point(300, 200)), Probability(50)),
    Area(Fertile, RectangleArea(Point(450, 100), Point(500, 150)), Probability(60)), //5
    Area(Fertile, RectangleArea(Point(200, 200), Point(350, 300)), Probability(60)),
    Area(Fertile, RectangleArea(Point(350, 150), Point(470, 300)), Probability(20)),
    Area(Fertile, RectangleArea(Point(0, 250), Point(200, 400)), Probability(80)),
    Area(Fertile, RectangleArea(Point(200, 300), Point(300, 400)), Probability(60)),
    Area(Fertile, RectangleArea(Point(400, 300), Point(470, 350)), Probability(60)), //10
    Area(Fertile, RectangleArea(Point(470, 280), Point(500, 400)), Probability(20)),
    Area(Fertile, RectangleArea(Point(350, 350), Point(470, 400)), Probability(60)),
    Area(Fertile, RectangleArea(Point(0, 400), Point(50, 500)), Probability(60)),
    Area(Fertile, RectangleArea(Point(100, 400), Point(200, 450)), Probability(40)),
    Area(Fertile, RectangleArea(Point(50, 450), Point(150, 500)), Probability(40)), //15
    Area(Fertile, RectangleArea(Point(250, 400), Point(300, 500)), Probability(20)),
    Area(Fertile, RectangleArea(Point(300, 400), Point(400, 500)), Probability(40)),
    Area(Fertile, RectangleArea(Point(400, 400), Point(500, 450)), Probability(20)),
  )

  test("serialize many RectangleArea"){
    val fileName = "rectangle-area.txt"
    defaultSerializer.serializeManyToFile(List(
      RectangleArea(Point(0,0), Point(100, 100)),
      RectangleArea(Point(111,222), Point(333, 444)))
    )(fileName)
    val path = Path.of("res"+File.separator+"serialization"+File.separator+fileName)
    val json = Files.readString(path, StandardCharsets.UTF_8)
    assert(json === "{\"topLeft\":{\"x\":0,\"y\":0},\"bottomRight\":{\"x\":100,\"y\":100}}{\"topLeft\":{\"x\":111,\"y\":222},\"bottomRight\":{\"x\":333,\"y\":444}}")
  }

  test("deserialize many RectangleArea"){
    val fileName = "rectangle-area.txt"
    val deserializedRectangleArea = defaultSerializer.deserializeManyFromFile(fileName)(classOf[RectangleArea])
    assert(deserializedRectangleArea.head == RectangleArea(Point(0,0), Point(100, 100)))
    assert(deserializedRectangleArea.tail.head == RectangleArea(Point(111,222), Point(333, 444)))
  }

  test("serialize and deserialize a Probability") {
    val probabilitySerializer: Serializer = Serializer(OfProbability)
    val prob = Probability(1)
    val serializedProbability = probabilitySerializer.serializeOne(prob)
    assert(serializedProbability == "{\"probability\":1}")
    val deserializedProbability = probabilitySerializer.deserializeOne(serializedProbability)(classOf[Probability])
    assert(prob.probability == deserializedProbability.probability)
  }

  test("deserialize a fertile area") {
    val area = Area(Fertile, RectangleArea(Point(300, 0), Point(400, 50)))
    val serializedArea = areasSerializer.serializeOne(area)
    val deserializedArea = areasSerializer.deserializeOne(serializedArea)(classOf[Area])
    assert(area.area == deserializedArea.area)
  }

  test("serialize the map"){
    areasSerializer.serializeManyToFile(areas)(Constants.MainMap)
    val path = Path.of("res"+File.separator+"serialization"+File.separator+Constants.MainMap)
    val json = Files.readString(path, StandardCharsets.UTF_8)
    assert(json != "")
  }

  test("deserialize the map") {
    val deserializedAreas = areasSerializer.deserializeManyFromFile(Constants.MainMap)(classOf[Area])
    assert(deserializedAreas.head.area == areas.head.area)
  }
}
