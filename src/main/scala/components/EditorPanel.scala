package components

import constants.AppEventConstants
import entities.{Question, Topic, TopicSmall}
import services.{AppEvent, AppEventService, NotificationService}
import utils.{QuestionToStringConverter, TextToQuestionConvert}

import scala.concurrent.ExecutionContext
import scala.swing.BorderPanel.Position
import scala.swing.{BorderPanel, TextArea}
import scala.util.{Failure, Success}


class EditorPanel(appEventService: AppEventService, notificationService: NotificationService)(implicit ec: ExecutionContext) extends BorderPanel {

  var openTopics = Map[TopicSmall, String]()

  var currentTopic : Option[TopicSmall] = None
  val textArea = new TextArea()

  layout( textArea ) = Position.Center

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
    currentTopic match {
      case Some(smallTopic) =>
        val allLines = textArea.text.split("\n")
        val currentQuestions = TextToQuestionConvert.getQuestionsFromText(allLines)
        val topic = new Topic(smallTopic.name, currentQuestions)
        smallTopic.repo.save(topic)
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
