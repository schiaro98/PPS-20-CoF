import Gui.{Species, animals, choices}
import javafx.event.ActionEvent
import scalafx.application.JFXApp
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.HBox

object Gui extends JFXApp {

  /*
  TODO species are written in a Json file, to be deserialized

  La specie definisce la velocità
  una volta scelta la specie posso scegliere quanti animali di quella specie
  Dimension cambia in size
  Size è un Enum
  Vista forza e velocità devono essere equilibrate (in totale devo avere X sommando)

   */
  val choices = Seq("Lion", "Zebra", "Ippo")
  class Species(name_ : String, dimension_ : String){
    val name = new StringProperty(this, "name", name_)
    val dimension = new StringProperty(this, "dimension", dimension_)
  }

  val animals = ObservableBuffer[Species] (
    new Species("Lion", "3"),
    new Species("Zebra", "2"),
    new Species("Fox", "1")
  )

  import scenes._
  stage = new JFXApp.PrimaryStage {
    title = "Circle of life"
    this.setScene(startingScene)

    startingScene.insertButton.onAction = (_: ActionEvent) => {
      this.setScene(insertScene)
    }

    startingScene.startButton.onAction = (_: ActionEvent) => {
      this.setScene(mapScene)
    }

    insertScene.insertButton.onAction = (_: ActionEvent) => {
      this.setScene(startingScene)
    }

    insertScene.newSpecie.onAction = (_: ActionEvent) => {
      this.setScene(createSpeciesScene)
    }

    /*createSpeciesScene.confirm.onAction = (_: ActionEvent) => {
      this.setScene(insertScene)
    }*/
  }
}

object scenes {
  val insertScene = new Scene(500, 400){
    val insertButton = new Button("Insert an animal")
    insertButton.layoutX = 300
    insertButton.layoutY = 370

    val newSpecie = new Button("Create new species")
    newSpecie.layoutX = 100
    newSpecie.layoutY = 300

    val speed = new Slider()
    speed.showTickMarks = true
    speed.showTickLabels = true
    speed.setBlockIncrement(25)
    speed.value = 50
    speed.layoutX = 300
    speed.layoutY = 180
    speed.max = 100
    speed.min = 0

    val speciesMenu = new ComboBox[String]()
    speciesMenu.tooltip = new Tooltip("Select species")
    speciesMenu.layoutX = 300
    speciesMenu.layoutY = 100
    choices.foreach(spec => speciesMenu.getItems.add(spec))

    content = List(insertButton, newSpecie, speciesMenu, speed)
  }

  val startingScene = new Scene(500,400) {
    val insertButton = new Button("Insert an animal")
    insertButton.layoutX = 100
    insertButton.layoutY = 100

    val startButton = new Button("Start simulation")
    startButton.layoutY = 200
    startButton.layoutX = 100

    /*
    Nella schermata iniziale far vedere la lista di animali con la quantità
     */
    val table = new TableView[Species](animals){
      columns ++= List(
        new TableColumn[Species, String]{
          text = "Name"
          cellValueFactory = {_.value.name}
        },
        new TableColumn[Species, String]{
          text = "Dimension"
          cellValueFactory = {_.value.dimension}
        },
      )
    }
    table.layoutX = 250
    content = List(insertButton, startButton, table)
  }

  val createSpeciesScene = new Scene(500, 400){
    val heightTextArea = 20
    val widthTextArea = 100
    val hbox = new HBox()
    hbox.setSpacing(10)
    val textAreas = List("name", "sigth", "dimension", "strength")
    textAreas.foreach(ta => {
      val textArea = new TextArea(ta)
      textArea.setPrefSize(widthTextArea, heightTextArea)
      hbox.getChildren.add(textArea)
    })

    val diet = new ComboBox[String]()
    diet.tooltip = new Tooltip("Select diet")
    diet.getItems.addAll("herbivore","carnivorous")
    diet.layoutX = 100
    diet.layoutY = 350
    hbox.getChildren.add(diet)

    content add hbox
  }

  val mapScene = new Scene(500, 400) {
    /*
    TODO
     */
    val label = new Label("Scene under working...")
    content = List(label)
  }
}
