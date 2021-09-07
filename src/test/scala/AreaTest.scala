import model.{Area, Fertile, Probability}
import org.scalatest.funsuite.AnyFunSuite

class AreaTest extends AnyFunSuite{

  test("Create illegal Fertile Area"){
    assertThrows[IllegalArgumentException](Area(Fertile, (20,20), (15, 25)))
  }

  test("Create empty Fertile Area without name"){
    val area = Area(Fertile, (10,20), (15, 25))
    assert(area.areaType == Fertile)
  }


}
