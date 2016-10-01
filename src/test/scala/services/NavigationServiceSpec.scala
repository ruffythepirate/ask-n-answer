package services

import constants.AppEventConstants
import entities.TopicSmall
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}
import org.mockito.Mockito._

class NavigationServiceSpec extends FunSpec with BeforeAndAfter with MockitoSugar{

  var appEventService : AppEventService = _

  var sut : NavigationService = _

  before {
    appEventService = mock[AppEventService]
    sut = new impl.NavigationService(appEventService)
  }

  describe("NavigationService") {
    describe("open topic") {
      it("should send an event when opening topic") {
        val topic = TopicSmall("Hello", null)
        sut.openTopic(topic)
        verify(appEventService).publishEvent(AppEventConstants.openTopic, topic)
      }
    }

  }



}
