package utility

import model.habitat._
import model.position.Point
import model.shape.RectangleArea
import model._
import org.scalatest.funsuite.AnyFunSuite
import utility.serializer.{DefaultSerializer, OfArea, OfProbability, Serializer}

class SerializeMapTest extends AnyFunSuite {

  val defaultSerializer: Serializer = Serializer(DefaultSerializer)
  val areasSerializer: Serializer = Serializer(OfArea)

  val areas: Seq[Area] = OursMap.areas


  test("serialize and deserialize many RectangleArea"){
    val fileName = "rectangle-area.json"
    defaultSerializer.serializeManyToFile(List(
      shape.RectangleArea(Point(0,0), Point(100, 100)),
      shape.RectangleArea(Point(111,222), Point(333, 444)))
    )(fileName)
    val deserializedRectangleArea = defaultSerializer.deserializeManyFromFile(fileName)(classOf[RectangleArea])
    assert(deserializedRectangleArea.head == shape.RectangleArea(Point(0,0), Point(100, 100)))
    assert(deserializedRectangleArea.tail.head == shape.RectangleArea(Point(111,222), Point(333, 444)))
 }


  test("serialize and deserialize a Probability") {
    val probabilitySerializer: Serializer = Serializer(OfProbability)
    val prob = Probability(1)
    val serializedProbability = probabilitySerializer.serializeOne(prob)
    assert(serializedProbability == "{\"probability\":1}")
    probabilitySerializer.deserializeOne(serializedProbability)(classOf[Probability])
  }

  test("deserialize a fertile area") {
    val area = habitat.Area(Fertile, shape.RectangleArea(Point(300, 0), Point(400, 50)))
    val serializedArea = areasSerializer.serializeOne(area)
    val deserializedArea = areasSerializer.deserializeOne(serializedArea)(classOf[Area])
    assert(area.area == deserializedArea.area)
  }

  test("serialize and deserialize the map"){
    areasSerializer.serializeManyToFile(areas)(Constants.MainMap)
    val deserializedAreas = areasSerializer.deserializeManyFromFile(Constants.MainMap)(classOf[Area])
    assert(deserializedAreas.head.area == areas.head.area)
  }

}
