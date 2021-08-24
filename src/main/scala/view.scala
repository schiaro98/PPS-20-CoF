import java.awt.Dimension
import scala.swing.BorderPanel.Position._
import scala.swing.event.ButtonClicked
import scala.swing.{BorderPanel, Button, FlowPanel, Frame, TextField}

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

    val leftPanel: BorderPanel = new BorderPanel {
      layout(createButton) = North
      layout(startButton) = South
    }

    val flowPanel: FlowPanel = new FlowPanel {
        animals.foreach(animal => {
          val nameField = new TextField(animal._1) {
            editable = false
          }

          val quantityField = new TextField(animal._2.toString) {
            editable = false
          }

          val increase = new Button() {
            text = "+"
            reactions += {
              case _: ButtonClicked => incQuantity(animal._1)
            }
            listenTo(this)
          }

          val decrease = new Button() {
            text = "-"
            reactions += {
              case _: ButtonClicked => decQuantity(animal._1)
            }
            listenTo(this)
          }

          contents addAll List(nameField, quantityField, increase, decrease)
        })
      }

    def incQuantity(name: String): Unit = {
      if (animals.contains(name)) {
        animals(name) = animals(name) + 1
      }
      println(animals)
    }

    def decQuantity(name: String): Unit = {
      if (animals.contains(name)) {
        val actualValue = animals(name)
        if (actualValue > 0) {
          animals(name) = actualValue - 1
        } else {
          animals -= name
        }
      }
      println(animals)
    }

      contents = new BorderPanel {
        layout(flowPanel) = Center
        layout(leftPanel) = West
      }

      pack()
      centerOnScreen()
      open()
    }
}
