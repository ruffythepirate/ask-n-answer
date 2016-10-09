package services

import entities.TopicSmall
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

class TopicServiceSpec extends FunSpec with BeforeAndAfter with MockitoSugar {

  var sut : impl.TopicService = _

  var feedbackService : services.FeedbackService = _
  var repo : services.Repository = _
  var topicSmall : TopicSmall = _

  before {
    feedbackService = mock[FeedbackService]
    repo = mock[Repository]

    topicSmall = new TopicSmall("random", repo)

    sut = new impl.TopicService(feedbackService)
  }

  describe("TopicService") {

    it("initialies") {

    }

    describe("asks for a filename when creating topic") {
      it("does nothing if no string is given") {
        when(feedbackService.requestStringInput(any(), any())).thenReturn("")
        sut.createTopic(repo)
        verifyZeroInteractions(repo)
      }

      it("saves a new topic if a string is given") {
        when(feedbackService.requestStringInput(any(), any())).thenReturn("my filename")
        sut.createTopic(repo)
        verify(repo).save(any())
      }
    }

    describe("asks yes no when deleting a topic") {
      it("removes the topic if yes") {
        when(feedbackService.requestYesNo(any(), any())).thenReturn(true)
        sut.deleteTopic(topicSmall)
        verify(repo).delete(topicSmall)
      }

      it("does nothing if no") {
        when(feedbackService.requestYesNo(any(), any())).thenReturn(false)
        sut.deleteTopic(topicSmall)
        verifyZeroInteractions(repo)
      }
    }
  }

}
