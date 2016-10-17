package components

import java.awt.event.ActionEvent
import java.io.FileNotFoundException
import java.util.concurrent.Executor
import javax.swing.KeyStroke

import constants.AppEventConstants
import entities.{Question, Topic, TopicSmall}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}
import services.impl.AppEventService
import services.{NotificationService, Repository}
import testutils.SynchronousExecutionContext

import scala.concurrent.{ExecutionContext, Future, Promise}

class EditorPanelSpec extends FunSpec with BeforeAndAfter with MockitoSugar with ScalaFutures with SynchronousExecutionContext {


  var appEventService: AppEventService = _
  var notificationService: NotificationService = _

  var mockRepo: Repository = _

  import testutils.TestData._

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

      it("maps command+s to save action") {
        val editor = createEditorPanel()

        val metaSAction = editor.peer.getInputMap.get(KeyStroke.getKeyStroke("meta S"))

        assert(metaSAction != null)
        assert(metaSAction.toString == "saveCurrent")
      }

      it("there is a save action for saveCurrent.") {
       val editor = createEditorPanel()

        val saveAction = editor.peer.getActionMap.get("saveCurrent")

       assert(saveAction != null)

      }

    }

    describe("listening to topic change") {
      it("should load a topic on topic change.") {
        val editor = createEditorPanel()

        val topic = createTopicSmall()
        appEventService.publishEvent(AppEventConstants.openTopic, topic)

        verify(mockRepo).getTopic(topic)
      }

      it("should not reload a topic if it is already open.") {
        val editor = createEditorPanel()
        val topic = createTopicSmall()
        val bigTopic = createTopic

        when(mockRepo.getTopic(topic)).thenReturn(Future.successful(bigTopic))

        appEventService.publishEvent(AppEventConstants.openTopic, topic)

        val text = editor.textArea.text
        editor.textArea.text = text + " some addition"
        val newText = editor.textArea.text

        appEventService.publishEvent(AppEventConstants.openTopic, topic)
        assert(editor.textArea.text === newText)
      }

      it("should be able to open two topic at the same time.") {
        // Arrange
        val editor = createEditorPanel()
        val topic1 = createTopicSmall("first")
        val topic2 = createTopicSmall("second")
        val bigTopic = createTopic

        when(mockRepo.getTopic(topic1)).thenReturn(Future.successful(bigTopic))
        when(mockRepo.getTopic(topic2)).thenReturn(Future.successful(bigTopic))

        //Act
        appEventService.publishEvent(AppEventConstants.openTopic, topic1)

        val text = editor.textArea.text
        editor.textArea.text = text + " some addition"
        val newText = editor.textArea.text
        appEventService.publishEvent(AppEventConstants.openTopic, topic2)
        appEventService.publishEvent(AppEventConstants.openTopic, topic1)

        //Assert
        assert(editor.textArea.text === newText)
      }

      it("should save a topic if Save Action is triggered is clicked.") {

        val editor = createEditorPanel()
        val topic = createTopicSmall()
        val bigTopic = createTopic

        when(mockRepo.getTopic(topic)).thenReturn(Future.successful(bigTopic))

        appEventService.publishEvent(AppEventConstants.openTopic, topic)
        editor.textArea.text += "some addition"

        appEventService.publishEvent(AppEventConstants.saveCurrentTopic)

        verify(topic.repo).save(any())
      }

      it("handles when get topic future fails") {
        val editor = createEditorPanel()

        val topic = createTopicSmall()
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
        val topic = createTopicSmall()
        val bigTopic = createTopic

        val promise = Promise[Topic]
        promise.success(bigTopic)
        when(mockRepo.getTopic(topic)).thenReturn(promise.future)

        appEventService.publishEvent(AppEventConstants.openTopic, topic)
        assert(editor.textArea.text.size > 0)
      }
    }
  }

  def createTopicSmall(name: String = "my topic") = {
    new TopicSmall(name, mockRepo)
  }

  def createEditorPanel() = {
    new EditorPanel(appEventService, notificationService)
  }
}
