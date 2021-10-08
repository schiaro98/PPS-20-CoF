package view

import java.awt._
import javax.swing._

class AnimalPopup(textToDisplay: String, location: () => Point) {

  val textArea = new JTextArea(textToDisplay)
  textArea.setEditable(false)

  val f = new JFrame
  f.setUndecorated(true)
  f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
  f.getContentPane.add(textArea, BorderLayout.CENTER)
  f.setLocation(location())

  def setText(text: String): Unit = {
    SwingUtilities.invokeLater(() => textArea.setText(text))
  }

  def setVisible(makeVisible: Boolean): Unit = {
    if (makeVisible) {
      f.setLocation(location())
      SwingUtilities.invokeLater(() => {f.pack(); f.setVisible(true)})
    } else {
      SwingUtilities.invokeLater(() => f.setVisible(false))
    }
  }
}