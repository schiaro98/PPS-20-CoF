import controller.BattleManager
import model.Size.{Big, Medium, Small}
import model.{Animal, Carnivorous, Herbivore, Probability, Species}
import org.scalatest.funsuite.AnyFunSuite
import utility.Point

class BattleManagerTest extends AnyFunSuite{

  val a1: Animal = Carnivorous(Species("", "tiger", Medium, 10, 10), Point(0,0))
  val a2: Animal = Carnivorous(Species("", "tiger", Small, 10, 10), Point(1,1))
  val a3: Animal = Carnivorous(Species("", "tiger", Small, 10, 10), Point(2,2))
  val herb: Animal = Herbivore(Species("", "elephant", Small, 10, 10), Point(2,2))
  val unreachableAnimal: Animal = Carnivorous(Species("", "tiger", Medium, 10, 10), Point(100,100))
  val bm: BattleManager = BattleManager()

  test("Test if two animals sees each other"){
    assert(bm.canSee(a1, a2))
    assert(bm.canSee(a2, a1))
    assert(!bm.canSee(a1, unreachableAnimal))
  }

  test("Test if animals that sees each other are returned"){
    assert(bm.visibleAnimals(Seq(a1,a2,a3,unreachableAnimal)).toSet ===
      Seq((a1,a2), (a2,a1), (a1, a3), (a2, a3), (a3,a1), (a3,a2)).toSet)
  }

  test("Test battle between animals that cannot see each other"){
    assertThrows[IllegalArgumentException](bm.startBattle(a1, unreachableAnimal))
  }

  test("Calculate Probabilities based on Strength"){
    val strongAnimal = Carnivorous(Species("Null", "tiger", Medium, 10, 10), Point(0,0))
    val normalAnimal = Carnivorous(Species("Null", "tiger", Medium, 7, 10), Point(0,0))
    val weakAnimal = Carnivorous(Species("Null", "tiger", Medium, 0, 10), Point(0,0))
    assert(bm.calculateProbabilityFromStrength(strongAnimal, weakAnimal) == Probability(75))
    assert(bm.calculateProbabilityFromStrength(weakAnimal, strongAnimal) == Probability(25))
    assert(bm.calculateProbabilityFromStrength(normalAnimal, strongAnimal) == Probability(40))
    assert(bm.calculateProbabilityFromStrength(strongAnimal, normalAnimal) == Probability(60))
  }

  test("Calculate Probabilities based on Distance"){
    val animalBig = Carnivorous(Species("Null", "tiger", Big, 10, 10), Point(0,0))
    val animalSmall = Carnivorous(Species("Null", "tiger", Small, 10, 10), Point(1,1))
    val animalNotSoFar = Carnivorous(Species("Null", "tiger", Small, 10, 10), Point(2,2))
    val farAnimal = Carnivorous(Species("Null", "tiger", Medium, 10, 10), Point(100,100))
    assert(bm.calculateProbabilityFromDistance(animalBig, animalSmall) == Probability(75))
    assert(bm.calculateProbabilityFromDistance(animalBig, farAnimal) == Probability(87))
    assert(bm.calculateProbabilityFromDistance(animalBig, animalNotSoFar) == Probability(40))
    assert(bm.calculateProbabilityFromDistance(animalSmall, animalNotSoFar) == Probability(75))
  }

  test("Calculate Probabilities based on Size"){
    val animalBig = Carnivorous(Species("Null", "tiger", Big, 10, 10), Point(0,0))
    val animalMedium = Carnivorous(Species("Null", "tiger", Medium, 10, 10), Point(0,0))
    val animalSmall = Carnivorous(Species("Null", "tiger", Small, 10, 10), Point(0,0))
    //Probabilities for big attacking animal
    assert(bm.calculateProbabilityFromSize(animalBig, animalMedium) == Probability(60))
    assert(bm.calculateProbabilityFromSize(animalBig, animalSmall) == Probability(75))

    //Probabilities for medium attacking animal
    assert(bm.calculateProbabilityFromSize(animalMedium, animalBig) == Probability(40))
    assert(bm.calculateProbabilityFromSize(animalMedium, animalSmall) == Probability(60))

    //Probabilities for small attacking animal
    assert(bm.calculateProbabilityFromSize(animalSmall, animalBig) == Probability(10))
    assert(bm.calculateProbabilityFromSize(animalSmall, animalMedium) == Probability(25))

    //Probabilities for attacking animal with same size of defending
    assert(bm.calculateProbabilityFromSize(animalSmall, animalSmall) == Probability(50))
    assert(bm.calculateProbabilityFromSize(animalMedium, animalMedium) == Probability(50))
    assert(bm.calculateProbabilityFromSize(animalBig, animalBig) == Probability(50))
  }

  test("Execute a simple battle"){
    val bm = BattleManager(Seq(a1, a2, a3))
    bm.calculateBattles()
  }

  test("Test if an herbivour can battle"){
    val bm = BattleManager(Seq(herb))
    assert(!bm.isCarnivorous(herb))
    assertThrows[IllegalArgumentException](bm.startBattle(herb, a1))
  }

  test("Test if a damaged (with a low health) animal can battle"){
    val damagedAnimal = a2.update(health = 0, a2.thirst, a2.position)
    assertThrows[IllegalArgumentException](bm.startBattle(damagedAnimal, a1))
  }
}
