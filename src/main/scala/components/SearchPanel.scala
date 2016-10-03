package components

import scala.swing.{BorderPanel, TextField}
import scala.swing.BorderPanel.Position._

class SearchPanel extends BorderPanel{

  val textField = new TextField()

  layout(textField) = Center

}
