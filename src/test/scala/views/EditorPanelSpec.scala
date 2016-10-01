package views

import constants.AppEventConstants
import entities.TopicSmall
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}
import services.impl.AppEventService
import org.mockito.Mockito._
import services.Repository

class EditorPanelSpec extends FunSpec with BeforeAndAfter with MockitoSugar{

    var appEventService : AppEventService = _

    var mockRepo : Repository = _

    before {
      appEventService = new AppEventService

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

          val topic = createTopic
          appEventService.publishEvent(AppEventConstants.openTopic, topic)

          verify(mockRepo).getTopic(topic)
        }
      }
    }

    def createTopic = {
      new TopicSmall("my topic", mockRepo)
    }

    def createEditorPanel() = {
      new EditorPanel(appEventService)
    }
}
