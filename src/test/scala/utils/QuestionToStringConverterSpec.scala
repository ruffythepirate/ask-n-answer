package utils

import entities.Question
import org.scalatest.FunSpec
import QuestionToStringConverter._

class QuestionToStringConverterSpec extends FunSpec {

  describe("QuestionToStringConverter") {

    describe("from question") {

      it("can translate one question into a string") {
        val q = Question("a", "b")

        val result = questionToString(q)

        assert(result === "a\nb\n\n")
      }

      it("can translate multiple questions") {
        val questions = Seq(Question("a", "b"), Question("c", "d"))

        val result = questionsToString(questions)

        assert(result === "a\nb\n\nc\nd\n\n")
      }

    }

  }
}
