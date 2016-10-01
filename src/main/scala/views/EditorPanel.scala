package views

import constants.AppEventConstants
import entities.{Topic, TopicSmall}
import services.{AppEvent, AppEventService}

import scala.swing.BorderPanel.Position
import scala.swing.{BorderPanel, TextArea}
import scala.util.Success


class EditorPanel(appEventService: AppEventService) extends BorderPanel {
  import scala.concurrent.ExecutionContext.Implicits.global


  val textArea = new TextArea()

  layout( textArea ) = Position.Center

  def onTopicLoaded(topic : Topic): Unit = {

  }

  def onOpenTopic (topic : AppEvent): Unit = {
      topic.data match {
        case ts : TopicSmall => {
          ts.repo.getTopic(ts)
            .onComplete {
              case Success(topic) => onTopicLoaded(topic)
            }
        }
        case _ =>
      }
  }

  appEventService.subscribeToEvent(AppEventConstants.openTopic, onOpenTopic)

}
