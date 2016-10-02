package utils

import entities.Question
import org.scalatest.FunSpec

class TextToQuestionConverterSpec extends FunSpec{

  describe("TextToQuestionConverter") {
    describe ("getting next question") {
      it("handles when there is a question and answer"){
        val q = "This is the question"
        val a = "This is the answer"
        val text = Seq[String](q, "",a)

        val result = TextToQuestionConvert.getNextQuestionItem(0, text)

        assert(result._2 === Some(Question(q, Some(a))))
        assert(result._1 === 3)
      }

      it("handles when there are no more questions") {
        val text = Seq[String]()

        val result = TextToQuestionConvert.getNextQuestionItem(0, text)

        assert(result._2 === None)
        assert(result._1 === 0)
      }

      it("handles when there is only a question") {
        val q = "This is the question"

        val text = Seq[String](q, "")

        val result = TextToQuestionConvert.getNextQuestionItem(0, text)

        assert(result._2 === Some(Question(q, None)))
        assert(result._1 === 2)
      }
    }

  }
}
