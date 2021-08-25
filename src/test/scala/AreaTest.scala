import model.{NonWalkableArea, Probability, Rock, WalkableArea, Water}
import org.scalatest.funsuite.AnyFunSuite

class AreaTest extends AnyFunSuite{

  test("Create empty WalkableArea with name"){
    val name = "my classic walkable area"
    val area = WalkableArea(name, (10,20), (15, 25), Probability(30), Probability(20))
    assert(area.name == name)
  }

  test("Create empty WalkableArea without name"){
    val area = WalkableArea((10,20), (15, 25), Probability(30), Probability(20))
    assert(area.name == "Simple walkable area")
  }


  test("Create empty NonWalkableArea with name"){
    val name = "my classic non walkable area"
    val area = NonWalkableArea(name, (10,20), (15, 25), Water)
    assert(area.name == name)
  }

  test("Create empty NonWalkableArea without name"){
    val area = NonWalkableArea((10,20), (15, 25), Rock)
    assert(area.name == "Simple non walkable area")
  }
}
