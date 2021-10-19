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
  for (i <- 0 to  Statistics.time){
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
    title = "Brainwave Plotter"
    peer.setContentPane(new ChartPanel(chart))
    peer.setLocationRelativeTo(null)
  }

}
  //
//  val f: Frame = new Frame {
//    title = "Choose an Habitat"
//
//    contents = new BoxPanel(Orientation.Vertical) {
//      contents += new Label("Here you can choose an habitat and personalize it")
//
//      peer.add(Box.createVerticalStrut(10))
//
//      contents += new Label("Set a quantity of unexpected events that could kill animals (From 0 to 100)")
//
//
//      peer.add(Box.createVerticalStrut(10))
//
//      contents += new Label("Width of the Habitat")
//      contents += new Label("Height of the Habitat")
//
//      peer.add(Box.createVerticalStrut(20))
//
//
//    }
//
//
//    //    this.preferredSize = new Dimension(1000, 1000)
//    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
//    centerOnScreen()
//    open()
//
//  }

