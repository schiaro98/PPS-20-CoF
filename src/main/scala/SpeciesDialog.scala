import model.{Size, Species}

import java.awt.Dimension
import scala.swing.BorderPanel.Position.Center
import scala.swing.event.ButtonClicked
import scala.swing.{BorderPanel, Button, ComboBox, FlowPanel, Frame, SimpleSwingApplication, TextField}

class SpeciesDialog(mng: LogicGui) extends SimpleSwingApplication {


  def top: Frame = new Frame {
    title = "Create species"
    preferredSize = new Dimension(200, 200)

    val nameField = new TextField("Name")
    val strengthField = new TextField("Strength")
    val sightField = new TextField("Sight")
    val sizeField = new ComboBox[String](Seq.empty) //TODO

    val confirm: Button = new Button("Confirm"){
      reactions += {
        case _ : ButtonClicked =>
          //TODO salvare nuova specie e togliere Size.Medium e null dalla icon
          //Eventuali controlli e chiusura frame

         /* mng.species.appended(Species(name = nameField.text,
            strength = strengthField.text.toInt,
            size = Size.Medium,
            sight = sightField.text.toInt, icon = null))*/

          top.visible = false
      }
    }

    val panel: FlowPanel = new FlowPanel() {
      contents addAll List(nameField, strengthField, sightField, sizeField, confirm)
    }

    contents = new BorderPanel() {
      layout(panel) = Center
    }
  }
}