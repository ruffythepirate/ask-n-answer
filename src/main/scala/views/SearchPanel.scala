package views

import scala.swing.{Button, Panel, TextField}

class SearchPanel extends Panel{

  val textField = new TextField()

  private val myContents = (new Content += new Button())
  override def contents = myContents
}
