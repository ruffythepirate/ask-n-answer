package services.impl

import javax.swing
import javax.swing.JOptionPane

import scala.swing.MainFrame

class FeedbackService(mainForm: MainFrame) extends services.FeedbackService{
  override def requestYesNo(title: String, body: String): Boolean = {
    val dialogResult = JOptionPane.showConfirmDialog (mainForm.peer, body, title,JOptionPane.YES_NO_OPTION )
    dialogResult == JOptionPane.YES_OPTION
  }

  override def requestStringInput(title: String, body: String): String = {

    JOptionPane.showInputDialog(
      mainForm.peer,
      body,
      title,
      JOptionPane.PLAIN_MESSAGE)
  }
}
