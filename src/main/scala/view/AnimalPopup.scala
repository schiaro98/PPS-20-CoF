package view

import model.Animal

import java.awt._
import javax.swing._

/**
 * Small Frame used to display the information of an Animal.
 *
 * @param animal   the Animal with all his information.
 * @param location the position on the screen where to visualize the frane.
 */
class AnimalPopup(animal: Animal, location: () => Point) {

  val textArea = new JTextArea(textToDisplay(animal))
  textArea.setEditable(false)

  val f = new JFrame
  f.setUndecorated(true)
  f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
  f.getContentPane.add(textArea, BorderLayout.CENTER)
  f.setLocation(location())
  f.pack()

  /**
   * Allows to show the popup or not.
   *
   * @param makeVisible true to make the popup visualizable, false otherwise.
   */
  def setVisible(makeVisible: Boolean): Unit = {
    if (makeVisible) {
      f.setLocation(location())
    }
    SwingUtilities.invokeLater(() => f.setVisible(makeVisible))
  }

  /**
   * Dispose the Frame of the popup.
   */
  def deletePopup(): Unit = SwingUtilities.invokeLater(() =>  f.dispose())

  /**
   * Method to get the info of a animal.
   *
   * @param a the animal about which information is desired.
   * @return a formatted String with the info of the animal.
   */
  def textToDisplay(a: Animal): String = {
    s"Species: ${a.name}\nHealth: ${a.health}\nThirst: ${a.thirst}\nType: ${a.alimentationType}\nAge: ${a.age}\nStrength: ${a.strength}\nSight: ${a.sight}\nSize: ${a.size}"
  }
}