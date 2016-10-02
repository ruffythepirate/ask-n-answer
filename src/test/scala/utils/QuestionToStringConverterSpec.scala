package utils

import entities.Question
import org.scalatest.FunSpec
import QuestionToStringConverter._

class QuestionToStringConverterSpec extends FunSpec {

  describe("QuestionToStringConverter") {

    describe("from question") {

      it("can translate one question into a string") {
        val q = Question("a", Some("b"))

        val result = questionToString(q)

        assert(result === "a\n\nb\n\n\n")
      }

      it("can translate multiple questions") {
        val questions = Seq(Question("a", Some("b")), Question("c", Some("d")))

        val result = questionsToString(questions)

        assert(result === "a\n\nb\n\n\nc\n\nd\n\n\n")
      }

    }

  }
}
