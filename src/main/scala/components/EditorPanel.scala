package components

import java.awt.event.KeyEvent
import java.awt.{Color, Toolkit}
import javax.swing.KeyStroke
import javax.swing.border.LineBorder

import constants.AppEventConstants
import entities.{Question, Topic, TopicSmall}
import services.{AppEvent, AppEventService, NotificationService}
import utils.{QuestionToStringConverter, TextToQuestionConvert}

import scala.concurrent.ExecutionContext
import scala.swing.BorderPanel.Position
import scala.swing.{Action, BorderPanel, TextArea}
import scala.util.{Failure, Success}


class EditorPanel(appEventService: AppEventService, notificationService: NotificationService)(implicit ec: ExecutionContext) extends BorderPanel {

  var openTopics = Map[TopicSmall, String]()

  var currentTopic : Option[TopicSmall] = None
  val textArea = new TextArea()

  layout( textArea ) = Position.Center

  background = Color.DARK_GRAY
  textArea.background = Color.DARK_GRAY
  textArea.foreground = Color.YELLOW
  textArea.caret.color = Color.YELLOW
  border = new LineBorder(Color.WHITE, 4  )

  private def initializeHotKeys(): Unit = {


    peer.getInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
      "saveCurrent")

    val saveCurrentAction = Action("saveCurrent") {
      saveCurrentTopic()
    }

    peer.getActionMap.put("saveCurrent", saveCurrentAction.peer)

    notificationService.info("hotkeys initialized")
  }

  initializeHotKeys()

  private def onTopicLoaded(topicSmall: TopicSmall, topic : Topic): Unit = {

    cacheCurrentTopic

    val text = QuestionToStringConverter.questionsToString(topic.questions)
    textArea.text = text

    currentTopic = Some(topicSmall)
    cacheCurrentTopic
  }

  private def onOpenTopic (appEvent : AppEvent): Unit = {
      openTopic(appEvent)
  }

  private def onSaveTopic (appEvent : AppEvent) = {
    saveCurrentTopic()
  }

  def saveCurrentTopic(): Unit = {
    currentTopic match {
      case Some(smallTopic) =>
        val allLines = textArea.text.split("\n")
        val currentQuestions = TextToQuestionConvert.getQuestionsFromText(allLines)
        val topic = new Topic(smallTopic.name, currentQuestions)
        smallTopic.repo.save(topic)

        notificationService.info(s"Topic ${smallTopic.name} has been saved.")
      case None =>
    }
  }

  private def openTopic(appEvent: AppEvent): Unit = {
    appEvent.data match {
      case ts: TopicSmall => {
        openTopic(ts)
      }
      case _ =>
    }
  }

  def openTopic(ts: TopicSmall): Unit = {
    if(! openTopics.contains(ts)) {
      ts.repo.getTopic(ts)
        .onComplete {
          case Success(topic) => {
            onTopicLoaded(ts, topic)
          }
          case Failure(t) => notificationService.error(s"Failed to load topic ${ts.name} with error $t")
        }
    } else {
      cacheCurrentTopic
      setEditorContent(ts)
    }
  }

  def setEditorContent(ts: TopicSmall): Unit = {
    currentTopic = Some(ts)
    textArea.text = openTopics(ts)
  }

  def cacheCurrentTopic: Unit = {
    currentTopic match {
      case Some(topic) => {
        openTopics = openTopics + (topic -> textArea.text)
      }
      case None =>
    }
  }

  appEventService.subscribeToEvent(AppEventConstants.openTopic, onOpenTopic)
  appEventService.subscribeToEvent(AppEventConstants.saveCurrentTopic, onSaveTopic)

}
