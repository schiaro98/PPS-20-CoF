package view

import model._

import java.awt.Dimension
import scala.swing.BorderPanel.Position.Center
import scala.swing._
import scala.swing.event.ButtonClicked

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
    val sizeField = new ComboBox[String](Seq(Small.toString, Medium.toString, Big.toString))
    val typeLabel = new Label("Type of the species")
    val typeField = new ComboBox[String](Seq("Herbivore", "Carnivore"))

    val confirm: Button = new Button("Confirm"){
      reactions += {
        case _ : ButtonClicked =>
          if(nameField.text == "Name"){
            Dialog.showMessage(contents.head, "Choose a name, please", title = "Try again!")
          } else if(logic.species.keySet.map(s => s.name).contains(nameField.text)){
            Dialog.showMessage(contents.head, "A species with the given name already exist", title = "Try again!")
          } else {
            val size: Size = sizeField.selection.item match {
              case "Big" => Big
              case "Medium" => Medium
              case "Small" => Small
              case _ => throw new IllegalArgumentException("Illegal type on Size")
            }

            val alimentationType: Type = typeField.selection.item match {
              case "Herbivore" => Herbivore
              case "Carnivore" => Carnivore
              case _ => throw new IllegalArgumentException("Illegal type on Type")
            }

            val newSpecie = logic.captionSpecies(nameField.text,
              size,
              strengthField.text,
              sightField.text,
              alimentationType
            )
            logic.addSpeciesInTheFile(newSpecie)

            closeAndUpdate()
          }
      }
    }

    val existingSpecies = new ComboBox[String](logic.species.keySet.map(s => s.name).toSeq)

    val removeSpecies: Button = new Button("Remove"){
      reactions += {
        case _ : ButtonClicked =>
          val species = logic.getSpecies(existingSpecies.selection.item).get
          logic.removeSpeciesFromFile(species)
          logic.remove(species)
          closeAndUpdate()
      }
    }

    val panel: BoxPanel = new BoxPanel(Orientation.Vertical) {
      contents addAll List(nameLabel, nameField, strengthLabel, strengthField, sightLabel, sightField, sizeLabel,
        sizeField, typeLabel, typeField, confirm, existingSpecies, removeSpecies)
      centerOnScreen()
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