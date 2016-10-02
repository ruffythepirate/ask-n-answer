package frames

import java.awt.Toolkit
import java.io.File

import entities.TopicSmall
import services.{NavigationService, Repository}
import services.impl.{AppEventService, FileService, NotificationService, RepositoryService}

import scala.swing.BorderPanel.Position._
import views.{EditorPanel, NavigatorPanel, SearchPanel}

import scala.swing.{BorderPanel, MainFrame}

class MainWindow extends MainFrame {

  import scala.concurrent.ExecutionContext.Implicits.global

  val fileService = new FileService("./main-repository")

  val notificationService = new NotificationService
  val appEventService = new AppEventService
  val repositoryService = new RepositoryService(fileService)
  val navigationService = new NavigationService {
    override def openTopic( topic: TopicSmall): Unit = ???
  }

  val searchPanel = new SearchPanel()
  val navigatorPanel = new NavigatorPanel(repositoryService, navigationService)
  val editorPanel = new EditorPanel(appEventService, notificationService)

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
