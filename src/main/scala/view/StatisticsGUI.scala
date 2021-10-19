package view

import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.{ChartFactory, ChartPanel, JFreeChart}
import org.jfree.data.category.DefaultCategoryDataset
import utility.Statistics

import scala.swing._

class StatisticsGUI {

  val Foods = "Foods"
  val Attacks = "Attacks"
  val FoodEaten = "Food eaten "
  val DeathInBattle = "Death in battle"
  val deathForNaturalCause = "Death for natural cause"

  val data = new DefaultCategoryDataset
  for (i <- 0 until Statistics.time){
    data.addValue(Statistics.map(i).foods, Foods, i)
    data.addValue(Statistics.map(i).attacks, Attacks, i)
    data.addValue(Statistics.map(i).foodEaten, FoodEaten, i)
    data.addValue(Statistics.map(i).deathInBattle, DeathInBattle, i)
    data.addValue(Statistics.map(i).deathForNaturalCause, deathForNaturalCause, i)
  }

  val chart: JFreeChart = ChartFactory.createLineChart(
    "Cof Statistics", "Time", "Value",
    data, PlotOrientation.VERTICAL,
    true, true, true)

  def top: MainFrame = new MainFrame {
    title = "Cof Statistics plotter"
    peer.setContentPane(new ChartPanel(chart))
    peer.setLocationRelativeTo(null)
    minimumSize = new Dimension(500,500)
    open()
  }

}