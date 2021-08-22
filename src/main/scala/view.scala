import java.awt.Dimension
import scala.swing.BorderPanel.Position._
import scala.swing.event.ButtonClicked
import scala.swing.{BorderPanel, Button, Frame, GridPanel, TextField}

object view extends App {
  import Writer._

  val animals = scala.collection.mutable.Map[String, Int]()
  write()
  read.foreach(animal => animals(animal.name) = animal.quantity)

  new Frame {
    title = "Circle of life"
    preferredSize = new Dimension(600, 600)

    val createButton: Button = new Button {
      text = "Create new specie"
      tooltip = "Click to create new species"
    }

    val startButton: Button = new Button {
      text = "Start simulation"
      tooltip = "Click to start the simulation"
    }

    val leftPanel : BorderPanel = new BorderPanel {
      layout(createButton) = North
      layout(startButton) = South
    }

    var grid: GridPanel = new GridPanel(0, 4){
      animals.foreach(animal => {
        val nameField = new TextField(animal._1){
          editable = false
        }

        val quantityField = new TextField(animal._2.toString){
          editable = false
        }

        val increase = new Button() {
          text = "+"
          preferredSize = new Dimension(50, 50)
          reactions += {
            case _: ButtonClicked => animals(animal._1) = animals(animal._1) + 1
          }
        }

        val decrease = new Button(){
          text = "-"
          preferredSize = new Dimension(50, 50)
          reactions += {
            case _: ButtonClicked =>
              val actualValue = animals(animal._1)
              if(actualValue > 0){
                animals(animal._1) = actualValue - 1
              } else {
                animals -= animal._1
              }
          }
        }

        listenTo(increase, decrease)
        reactions += {
          case _: ButtonClicked =>
            print(animals)
            grid.revalidate()
            grid.repaint()
        }

        contents addAll List(nameField, quantityField, increase, decrease)

      })
    }


    contents = new BorderPanel {
      layout(grid) = Center
      layout(leftPanel) = West
    }

  }


}
