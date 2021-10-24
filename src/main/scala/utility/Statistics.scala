package utility

object Statistics {

  private var _time: Int = 0
  var map: Map[Int, StatisticsForTimeT] = Map.empty

  /**
   *
   * @return the current time
   */
  def time:Int = _time

  /**
   * re initialize the [[Statistics]] and time
   */
  def restart():Unit = {
    _time = 0
    map = map.empty
  }

  /**
   * increment time and create an empty Map
   */
  def incTime(): Unit = {
    _time+=1
    update()
  }


  /**
   * Update the values in the current time
   *
   * @param foods to sum
   * @param attacks to sum
   * @param foodEaten to sum
   * @param deathInBattle to sum
   * @param deathForNaturalCause to sum
   */
  def update(foods: Double = 0, attacks: Double = 0, foodEaten: Double = 0, deathInBattle: Double = 0, deathForNaturalCause: Double = 0): Unit = map =
    if (map isDefinedAt _time)
      map + (time -> map(_time).sum(StatisticsForTimeT(foods, attacks, foodEaten, deathInBattle, deathForNaturalCause)))
    else
      map + (time -> StatisticsForTimeT(foods, attacks, foodEaten, deathInBattle, deathForNaturalCause))

  protected case class StatisticsForTimeT(foods: Double = 0,
                                          attacks: Double = 0,
                                          foodEaten: Double = 0,
                                          deathInBattle: Double = 0,
                                          deathForNaturalCause: Double = 0,
                                         ) {
    def sum(s: StatisticsForTimeT): StatisticsForTimeT =
      StatisticsForTimeT(foods + s.foods, attacks + s.attacks, foodEaten + s.foodEaten,
        deathInBattle + s.deathInBattle, deathForNaturalCause + s.deathForNaturalCause)
  }
}
