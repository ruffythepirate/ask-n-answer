package frames

import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.io.File

import constants.AppEventConstants
import services.impl._

import scala.swing.BorderPanel.Position._
import components.{EditorPanel, NavigationPanel, SearchPanel, TopMenuBar}

import scala.swing.event.Key.Modifier
import scala.swing.event.{Key, KeyPressed, KeyTyped}
import scala.swing.{BorderPanel, MainFrame}

class MainWindow extends MainFrame {

  import scala.concurrent.ExecutionContext.Implicits.global

  val fileService = new FileService("./main-repository")

  val notificationService = new NotificationService
  val appEventService = new AppEventService
  val repositoryService = new RepositoryService(fileService)
  val navigationService = new NavigationService(appEventService)
  val feedbackService = new FeedbackService(this)
  val topicService = new TopicService(feedbackService, appEventService)

  val topMenuBar = new TopMenuBar(appEventService)

  val searchPanel = new SearchPanel()
  val navigatorPanel = new NavigationPanel(repositoryService, navigationService, topicService, feedbackService, appEventService)
  val editorPanel = new EditorPanel(appEventService, notificationService)

  listenTo(editorPanel.textArea.keys)
  reactions += {
    case kt : KeyTyped =>
      val keyIsCtrlS = kt.peer.getKeyChar == '\u0013'

      if(keyIsCtrlS)
          appEventService.publishEvent(AppEventConstants.saveCurrentTopic)
  }

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

    menuBar = topMenuBar

    import javax.swing.WindowConstants.EXIT_ON_CLOSE
    peer.setDefaultCloseOperation(EXIT_ON_CLOSE)

    val frameWidth = 800
    val frameHeight = 600
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
    peer.setBounds( (screenSize.getWidth().toInt - frameWidth)/2 , 0, frameWidth, frameHeight)

  }

}
