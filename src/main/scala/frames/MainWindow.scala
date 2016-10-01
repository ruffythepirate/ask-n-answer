package frames

import java.awt.Toolkit
import java.io.File

import services.impl.{FileService, RepositoryService}

import scala.swing.BorderPanel.Position._
import views.{EditorPanel, NavigatorPanel, SearchPanel}

import scala.swing.{BorderPanel, MainFrame}

class MainWindow extends MainFrame {


  val fileService = new FileService

  val repositoryService = new RepositoryService(fileService)

  val searchPanel = new SearchPanel()
  val navigatorPanel = new NavigatorPanel(repositoryService)
  val editorPanel = new EditorPanel()

  initializeComponents

  navigatorPanel.initializeRepositories

  private def initializeComponents = {

    val contentPane = new BorderPanel
    contentPane.contents
    contents = new BorderPanel {
      layout(searchPanel) = North
      layout(navigatorPanel) = West
      layout(editorPanel) = Center
    }

    import javax.swing.WindowConstants.EXIT_ON_CLOSE
    peer.setDefaultCloseOperation(EXIT_ON_CLOSE)

    val frameWidth = 800
    val frameHeight = 600
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
    peer.setBounds( (screenSize.getWidth().toInt - frameWidth)/2 , 0, frameWidth, frameHeight)

  }

}
