package utils

import entities.Question

object QuestionToStringConverter {

  def questionsToString(questions : Seq[Question]) = {
    val stringBuilder = new StringBuilder()

    questions.foreach( appendQuestionToBuilder(_, stringBuilder))

    stringBuilder.toString()
  }

  private def appendQuestionToBuilder(q : Question, stringBuilder : StringBuilder): Unit = {
    stringBuilder.append(q.question)

    q.answer match {
      case Some(answer) => stringBuilder.append("\n\n"); stringBuilder.append(answer)
      case None =>
    }

    stringBuilder.append("\n\n\n")
  }

  def questionToString(question : Question ) = {
    val stringBuilder = new StringBuilder()

    appendQuestionToBuilder(question, stringBuilder)

    stringBuilder.toString()
  }

}
