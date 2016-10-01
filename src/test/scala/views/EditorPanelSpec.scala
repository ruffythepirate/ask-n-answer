package views

import java.io.FileNotFoundException

import constants.AppEventConstants
import entities.{Question, Topic, TopicSmall}
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}
import services.impl.AppEventService
import org.mockito.Mockito._
import services.{NotificationService, Repository}
import org.mockito.Matchers.any

import scala.concurrent.Future

class EditorPanelSpec extends FunSpec with BeforeAndAfter with MockitoSugar {

    var appEventService : AppEventService = _
    var notificationService : NotificationService = _

    var mockRepo : Repository = _

    before {
      appEventService = new AppEventService

      notificationService = mock[NotificationService]

      mockRepo = mock[Repository]
    }

    describe("EditorPanel") {
      describe("initialization") {
        it("should initialize") {
          createEditorPanel
        }

      }

      describe("listening to topic change") {
        it("should load a topic on topic change.") {
          val editor = createEditorPanel()

          val topic = createTopicSmall
          appEventService.publishEvent(AppEventConstants.openTopic, topic)

          verify(mockRepo).getTopic(topic)
        }

        it("handles when get topic future fails") {
          val editor = createEditorPanel()

          val topic = createTopicSmall
          val bigTopic = createTopic

          when(mockRepo.getTopic(topic)).thenReturn(Future.failed(new FileNotFoundException()))

          appEventService.publishEvent(AppEventConstants.openTopic, topic)

          verify(notificationService).error(any())
        }

        it("sets the text in editor panel based on the questions") {
          val editor = createEditorPanel()

          assert(editor.textArea.text.size === 0)
          val topic = createTopicSmall
          val bigTopic = createTopic

          when(mockRepo.getTopic(topic)).thenReturn(Future.successful(bigTopic))

          appEventService.publishEvent(AppEventConstants.openTopic, topic)
          assert(editor.textArea.text.size > 0)
        }
      }
    }

 def createTopic = {
   val questions = Seq( Question("What is the question", "And that is the answer"))
   Topic ("topic", questions)
 }

    def createTopicSmall = {
      new TopicSmall("my topic", mockRepo)
    }

    def createEditorPanel() = {
      new EditorPanel(appEventService, notificationService)
    }
}
