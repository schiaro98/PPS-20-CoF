package utility

import model.animal.{Big, Carnivore, Herbivore, Medium, Small, Species}

import java.awt.Color

object OursSpecies {

  val species = Seq(
    Species("Hippopotamus", Big, 30, 10, Carnivore, new Color(-8752012)),
    Species("Hyena", Small, 20, 15, Carnivore, new Color(-3398716)),
    Species("Lion", Medium, 20, 20, Carnivore, new Color(-2175844)),
    Species("Zebra", Medium, 10, 20, Herbivore, new Color(-100)),
    Species("Leopard", Small, 15, 30, Carnivore, new Color(-14433844)),
    Species("Elephant", Big, 30, 20, Herbivore, new Color(-8298180)),
  )
}
