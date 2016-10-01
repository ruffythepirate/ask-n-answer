package views

import scala.swing.BorderPanel.Position
import scala.swing.{BorderPanel, TextArea}

class EditorPanel extends BorderPanel {
  val textArea = new TextArea()

  layout( textArea ) = Position.Center


}
