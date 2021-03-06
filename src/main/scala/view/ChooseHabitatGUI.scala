package view

import controller.GameLoop
import model._
import model.habitat.{EmptyHabitatType, GridHabitatType, Habitat, HabitatType, RandomHabitatType, SimpleHabitatType}
import view.logic.ChooseHabitatLogic

import javax.swing.{Box, WindowConstants}
import scala.swing._
import scala.swing.event.ButtonClicked

class ChooseHabitatGUI(val l :ChooseHabitatLogic) {

  val w: TextField = new TextField("500"){
    editable = false
  }
  val h: TextField = new TextField("500"){
    editable = false
  }
  val ue: TextField = new TextField("1")

  val ours: RadioButton = new RadioButton("An ad hoc habitat with fertile areas and water"){
    doClick()
    reactions += {
      case _: ButtonClicked =>
        setVisibility(false)
    }
  }

  val empty: RadioButton = new RadioButton("An habitat with no area, there will be no water and food won't grow"){
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

  val grid: RadioButton = new RadioButton("An habitat with grid shaped-placed areas"){
    reactions += {
      case _: ButtonClicked =>
        setVisibility(true)
    }
  }

  val radios: ButtonGroup = new ButtonGroup{
    buttons +=  ours
    buttons += empty
    buttons += random
    buttons += grid
  }

  val f: Frame = new Frame {
    title = "Choose an Habitat"

    contents = new BoxPanel(Orientation.Vertical) {
      contents += new Label("Here you can choose an habitat and personalize it")

      peer.add(Box.createVerticalStrut(10))

      contents += new Label("Set a quantity of unexpected events that could kill animals (From 0 to 10)")
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
              startSimulation(optHabitat.get)
              close()
            } else {
              Dialog.showMessage(contents.head, "Some input is not valid", title = "Try again!")
            }
        }
      }
    }
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    centerOnScreen()
    open()
  }

  def setVisibility(editable: Boolean): Unit = {
    w.editable = editable
    h.editable = editable
  }

  def getType: HabitatType = {
    if (ours.selected) SimpleHabitatType
    else if (empty.selected) EmptyHabitatType
    else if (random.selected) RandomHabitatType
    else GridHabitatType
  }

  def startSimulation(habitat: Habitat): Unit = new Thread(GameLoop(l.species, habitat)).start()
}
