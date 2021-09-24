package view

import model.Size

import java.awt.Dimension
import scala.swing.BorderPanel.Position.Center
import scala.swing.event.ButtonClicked
import scala.swing._

class SpeciesGui(logic: LogicGui) extends SimpleSwingApplication {

  def top: Frame = new Frame {
    title = "Create species"
    preferredSize = new Dimension(350, 300)

    val nameLabel = new Label("Name of the species")
    val nameField = new TextField("Name")
    val strengthLabel = new Label("Strength of the species")
    val strengthField = new TextField("Strength")
    val sightLabel = new Label("Sight of the species")
    val sightField = new TextField("Sight")
    val sizeLabel = new Label("Size of the species")
    val sizeField = new ComboBox[String](Seq(Size.Small.toString, Size.Medium.toString, Size.Big.toString))

    val confirm: Button = new Button("Confirm"){
      reactions += {
        case _ : ButtonClicked =>
          if(nameField.text == "Name"){
            println("Default species, please retry...")
          } else if(logic.speciesSeq.map(species => species.name).contains(nameField.text)){
            println("Nome già assegnato")
          } else {
            val newSpecie = logic.captionSpecies(nameField.text,
              sizeField.selection.item,
              strengthField.text,
              sightField.text)

            logic.addSpecies(newSpecie)
          }
          closeAndUpdate()
      }
    }

    val existingSpecies = new ComboBox[String](logic.speciesSeq.map(species => species.name))
    val removeSpecies: Button = new Button("Remove"){
      reactions += {
        case _ : ButtonClicked =>
          val speciesName = existingSpecies.selection.item
          logic.removeSpecies(logic.getSpecies(speciesName).get)
          logic.remove(speciesName)
          closeAndUpdate()
      }
    }

    val panel: BoxPanel = new BoxPanel(Orientation.Vertical) {
      contents addAll List(nameLabel, nameField, strengthLabel, strengthField, sightLabel, sightField, sizeLabel,
        sizeField, confirm, existingSpecies, removeSpecies)
      centerOnScreen()
//      open()
    }

    contents = new BorderPanel() {
      layout(panel) = Center
    }

    def closeAndUpdate(): Unit = {
      close()
      this.visible = false
    }
  }
}