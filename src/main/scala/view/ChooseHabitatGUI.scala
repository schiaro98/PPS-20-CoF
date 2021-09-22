package view

import model.{EmptyHabitatType, HabitatType, RandomHabitatType, SimpleHabitatType}

import javax.swing.Box
import scala.swing._
import scala.swing.event.ButtonClicked

class ChooseHabitatGUI(val l :ChooseHabitatLogic ) {

  val w: TextField = new TextField("1000"){
    editable = false
  }
  val h: TextField = new TextField("1000"){
    editable = false
  }
  val ue: TextField = new TextField("10")

  val ours: RadioButton = new RadioButton("The habitat that we made"){
    doClick
    reactions += {
      case _: ButtonClicked =>
        setVisibility(false)
    }
  }

  val empty: RadioButton = new RadioButton("An habitat without any area in it, food won't grow and there is no water to drink"){
    reactions += {
      case _: ButtonClicked =>
        setVisibility(true)
    }
  }

  val random: RadioButton = new RadioButton("An habitat with randomly placed areas"){
    reactions += {
      case _: ButtonClicked =>
        setVisibility(true)
    }
  }

  val radios: ButtonGroup = new ButtonGroup{
    buttons +=  ours
    buttons += empty
    buttons += random
  }

  val f: Frame = new Frame {
    title = "Choose an Habitat"

    contents = new BoxPanel(Orientation.Vertical) {
      contents += new Label("Here you can choose an habitat and personalize it")

      peer.add(Box.createVerticalStrut(10))

      contents += new Label("Set a quantity of unexpected events that could kill animals (From 0 to 100)")
      contents += ue

      contents ++= radios.buttons

      peer.add(Box.createVerticalStrut(10))

      contents += new Label("Width of the Habitat")
      contents += w
      contents += new Label("Height of the Habitat")
      contents += h

      peer.add(Box.createVerticalStrut(20))


      contents += new Button("Start Simulation") {
        reactions += {
          case _: ButtonClicked =>
            val optHabitat = l.createHabitat(getType, w.text, h.text, ue.text)
            if (optHabitat.isDefined) {
              //val habitat = optHabitat.get
              new SimulationGui(){
                top.visible = true
                close()
              }
            } else {
              Dialog.showMessage(contents.head, "Some input is not valid", title = "Try again!")
            }
        }
      }
//      println(s.selected)
    }


    //    this.preferredSize = new Dimension(1000, 1000)
    pack()
    centerOnScreen()
    open()
  }

  def setVisibility(editable: Boolean): Unit = {
    w.editable = editable
    h.editable = editable
  }

  def getType: HabitatType =
    if (ours.enabled) {
      SimpleHabitatType
    } else if (empty.enabled){
      EmptyHabitatType
    } else {
      RandomHabitatType
    }
}
