package view

import model.Species
import utility.Constants

import java.awt.Dimension
import javax.swing.WindowConstants
import scala.swing.BorderPanel.Position._
import scala.swing._
import scala.swing.event.ButtonClicked

object StartingGUI {

  val logic = new LogicGui(Constants.SavedSpecies)
  logic.initialize()

  val frame: Frame = new Frame {
    title = "Circle of life"
    preferredSize = new Dimension(600, 250)

    val createButton: Button = new Button("Create new specie") {
      tooltip = "Click to create new species"
      reactions += {
        case _: ButtonClicked => new SpeciesGui(logic) {
          top.visible = true
        }
      }
    }

    val startButton: Button = new Button("Choose the habitat") {
      tooltip = "Click to choose the habitat"
      reactions += {
        case _: ButtonClicked =>
          new ChooseHabitatGUI(new ChooseHabitatLogic(logic.species))
          close()
      }
    }

    val rigthPanel: BorderPanel = new BorderPanel {
      layout(createButton) = North
      layout(startButton) = South
    }

    val grid: GridPanel = new GridPanel(0, 4) {

      def initGrid(animals: Map[Species, Int]): Unit = {

        animals.foreach(animal => {

          val nameField = new TextField(animal._1.name) {
            editable = false
          }

          val quantityField = new TextField(animal._2.toString) {
            editable = false
          }

          val increase = new Button("+") {
            reactions += {
              case _: ButtonClicked =>
                logic.increase(animal._1)
                updateGrid()
            }
          }

          val decrease = new Button("-") {
            reactions += {
              case _: ButtonClicked =>
                logic.decrease(animal._1)
                updateGrid()
            }
          }

          contents addAll List(nameField, quantityField, increase, decrease)
        })

        val allNames = logic.getAllSpecies.map(species => species.name)
        val deletedByGui = logic.species.keySet.map(species => species.name).toSeq
        val cb: ComboBox[String] = new ComboBox[String](allNames diff deletedByGui)

        val chooseButton = new Button("Add") {
          reactions += {
            case _: ButtonClicked =>
              if (cb.selection.item != null) {
                logic.increase(cb.selection.item)
                updateGrid()
              }
          }
        }

        contents addAll List(cb, chooseButton)
      }

      def updateGrid(): Unit = {
        grid.peer.removeAll()
        grid.revalidate()
        grid.repaint()
        initGrid(logic.species)
      }

      initGrid(logic.species)
    }

    contents = new BorderPanel() {
      layout(rigthPanel) = Center
      layout(grid) = West
    }

    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    centerOnScreen()
    open()
  }
}
