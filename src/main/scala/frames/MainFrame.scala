package frames

import java.awt.{BorderLayout, Toolkit}
import javax.swing.{JFrame, JPanel}

import views.{EditorPanel, NavigatorPanel, SearchPanel}

class MainFrame extends JFrame {


  val searchPanel = new SearchPanel()
  val navigatorPanel = new NavigatorPanel()
  val editorPanel = new EditorPanel()

  initializeComponents

  private def initializeComponents = {

    val contentPane = new JPanel(new BorderLayout());
    contentPane.add(searchPanel, BorderLayout.NORTH);
    contentPane.add(navigatorPanel, BorderLayout.WEST);
    contentPane.add(editorPanel, BorderLayout.CENTER);

    setContentPane(contentPane)

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    val frameWidth = 800
    val frameHeight = 600
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
    setBounds( (screenSize.getWidth().toInt - frameWidth)/2 , 0, frameWidth, frameHeight)

  }

}
