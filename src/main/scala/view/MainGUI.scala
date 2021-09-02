package view

import java.awt.Dimension
import scala.swing.BorderPanel.Position._
import scala.swing._
import scala.swing.event.ButtonClicked

/*
TODO
Il pulsante crea nuove specie dovrebbe aprire una finestra/dialog che permette di inserire alcuni campi e creare un animale
Dopo la creazione si preme un pulsante e la nuova specie può essere scelta tra le latre nel menu cb

Il pulsante start dovrebbe caricare gli animali scelti nella simulazione
 */
object MainGUI extends SimpleSwingApplication {

  val logic = new LogicGui("species.txt")
  logic.initialize()

  override def top: Frame = new Frame {
    title = "Circle of life"
    preferredSize = new Dimension(450, 200)

    val createButton: Button = new Button("Create new specie") {
      tooltip = "Click to create new species"
      reactions += {
        case _: ButtonClicked => new SpeciesGui(logic).top.visible = true
      }
    }

    val startButton: Button = new Button("Start simulation") {
      tooltip = "Click to start the simulation"
    }

    val leftPanel: BorderPanel = new BorderPanel {
      layout(createButton) = North
      layout(startButton) = South
    }

    val grid: GridPanel = new GridPanel(0, 4) {

      def initGrid(animals: Map[String, Int]): Unit = {

        animals.foreach(animal => {

          val nameField = new TextField(animal._1) {
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

        val speciesOnFile = logic.speciesSeq
        val cb: ComboBox[String] = new ComboBox[String](speciesOnFile.map(species => species.name) diff logic.species.keySet.toSeq ) //diff

        val chooseButton = new Button("Add") {
          reactions += {
            case _: ButtonClicked =>
              println(logic.species)
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
      layout(leftPanel) = Center
      layout(grid) = West
    }

    pack()
    centerOnScreen()
    open()

  }
}
