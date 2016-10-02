package views

import constants.AppEventConstants
import entities.{Topic, TopicSmall}
import services.{AppEvent, AppEventService, NotificationService}
import utils.QuestionToStringConverter

import scala.concurrent.ExecutionContext
import scala.swing.BorderPanel.Position
import scala.swing.{BorderPanel, TextArea}
import scala.util.{Failure, Success}


class EditorPanel(appEventService: AppEventService, notificationService: NotificationService)(implicit ec: ExecutionContext) extends BorderPanel {


  val textArea = new TextArea()

  layout( textArea ) = Position.Center

  def onTopicLoaded(topic : Topic): Unit = {

    val text = QuestionToStringConverter.questionsToString(topic.questions)

    textArea.text = text
  }

  def onOpenTopic (topic : AppEvent): Unit = {
      topic.data match {
        case ts : TopicSmall => {
          ts.repo.getTopic(ts)
            .onComplete {
              case Success(topic) => onTopicLoaded(topic)
              case Failure(t) => notificationService.error(s"Failed to load topic ${ts.name} with error $t")
            }
        }
        case _ =>
      }
  }

  appEventService.subscribeToEvent(AppEventConstants.openTopic, onOpenTopic)

}
