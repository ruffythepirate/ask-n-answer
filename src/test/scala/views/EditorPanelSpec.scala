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
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.concurrent.duration._

class EditorPanelSpec extends FunSpec with BeforeAndAfter with MockitoSugar with ScalaFutures{

  import scala.concurrent.ExecutionContext.Implicits.global

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

          val promise = Promise[Topic]
            val future = promise.failure(new FileNotFoundException()).future

          when(mockRepo.getTopic(topic)).thenReturn(future)

          appEventService.publishEvent(AppEventConstants.openTopic, topic)

          verify(notificationService).error(any())
        }

        it("sets the text in editor panel based on the questions") {
          val editor = createEditorPanel()

          assert(editor.textArea.text.size === 0)
          val topic = createTopicSmall
          val bigTopic = createTopic

          val promise = Promise[Topic]
          promise.success(bigTopic)
          when(mockRepo.getTopic(topic)).thenReturn(promise.future)

          appEventService.publishEvent(AppEventConstants.openTopic, topic)

          whenReady(promise.future)
          {
            case topic =>
              assert(editor.textArea.text.size > 0)
          }
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
