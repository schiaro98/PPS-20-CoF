package view

import controller.{OfArea, Serializer}
import model.{Area, EmptyHabitatType, Habitat, HabitatType, Probability, RandomHabitatType, SimpleHabitatType}
import utility.Constants

import javax.swing.{Box, WindowConstants}
import scala.swing._
import scala.swing.event.ButtonClicked

class ChooseHabitatGUI(val l :ChooseHabitatLogic ) {

  val w: TextField = new TextField("500"){
    editable = false
  }
  val h: TextField = new TextField("500"){
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
              radios.selected.get match {
                case v : RadioButton if v == ours =>
                  val areas = Serializer(OfArea).deserializeManyFromFile(Constants.mainMap)(classOf[Area])
                  val habitat: Habitat = Habitat(getType, Probability(ue.text.toInt), Constants.mainMapDimension, areas)
                  new SimulationGui(habitat) { top.visible = true; /* close() */ } //TODO - decommentare i close()
                case v : RadioButton if v == empty => new SimulationGui(optHabitat.get) { top.visible = true; /* close() */ }
                case v : RadioButton if v == random => new SimulationGui(optHabitat.get) { top.visible = true; /* close() */ }
                case _ => throw new IllegalArgumentException
              }
            } else {
              Dialog.showMessage(contents.head, "Some input is not valid", title = "Try again!")
            }
        }
      }
    }


    //    this.preferredSize = new Dimension(1000, 1000)
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    pack()
    centerOnScreen()
    open()
  }

  def setVisibility(editable: Boolean): Unit = {
    w.editable = editable
    h.editable = editable
  }

  def getType: HabitatType = {
    case _ if ours.selected => SimpleHabitatType
    case _ if empty.selected => EmptyHabitatType
    case _ if random.selected => RandomHabitatType
  }
}
