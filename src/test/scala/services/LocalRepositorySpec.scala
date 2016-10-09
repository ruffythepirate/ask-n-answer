package services

import entities.TopicSmall
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}
import services.impl.LocalRepository
import testutils.SynchronousExecutionContext

import scala.io.Source

class LocalRepositorySpec extends FunSpec with BeforeAndAfter with MockitoSugar with ScalaFutures with SynchronousExecutionContext {
  import org.mockito.Matchers.any
  import testutils.TestData._

  var fileService: FileService = _

  var sut: LocalRepository = _

  before {
    fileService = mock[FileService]

    sut = new LocalRepository(fileService)
  }

  describe("LocalRepository") {
    describe("get topic") {

      val examples = Seq(
        ("can digest a file with one question" -> ("this is? \n\n yep, a one question file." -> 1)),
        ("can digest a file with two questions" -> ("this is? \n\n yep, a two question file.\n\n\nsecond question\n\nsecond answ" -> 2)),
        ("can digest a file with two questions, one without answer" -> ("this is? \n\n yep, a two question file.\n\n\nsecond question" -> 2)),
        ("can digest a file with two questions, two without answers" -> ("this is?\n\n\nsecond question" -> 2)))

      examples.foreach(tpl => {
        it(tpl._1) {
          val fileContent = tpl._2._1
          val topicSmall = createTopicSmall

          when(fileService.openFile(topicSmall.name)).thenReturn(createSource(fileContent))

          val future = sut.getTopic(topicSmall)

          whenReady(future) {
            case topic =>
              assert(topic.questions.length === tpl._2._2)
          }
        }
      })
    }

    describe("save") {
      it("can save a file") {
        val topicSmall = createTopicSmall

        val topic = createTopic
        sut.save(topic)

        verify(fileService).saveFile(any(), any())
      }
    }
  }

  def createSource(text: String): Source = {
    Source.fromString(text)
  }

  def createTopicSmall = {
    new TopicSmall("my topic", sut)
  }
}
