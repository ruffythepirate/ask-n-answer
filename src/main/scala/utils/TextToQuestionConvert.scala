package utils

import entities.Question

import scala.collection.mutable.ArrayBuffer

object TextToQuestionConvert {

  def getQuestionsFromText(allLines: Seq[String]): Seq[Question] = {
    val questions = new ArrayBuffer[Question]
    var currentIndex = 0
    while (currentIndex < allLines.size) {
      val newIndex: Int = extractNextQuestion(allLines, questions, currentIndex)
      currentIndex = newIndex
    }
    questions
  }

  def extractNextQuestion(allLines: Seq[String], questions: ArrayBuffer[Question], currentIndex: Int): Int = {
    val (newIndex, optionQuestion) = TextToQuestionConvert.getNextQuestionItem(currentIndex, allLines)
    optionQuestion match {
      case Some(question) =>
        questions += question
      case None =>
    }
    newIndex
  }

  def getNextQuestionItem(startIndex: Int, allLines: Seq[String]): (Int, Option[Question]) = {

    val(i1,  question) = getNextQuestion(startIndex, allLines)
    val(i2,  answer) = getNextAnswer(i1, allLines)
    if(question.nonEmpty) {
      if(answer.isEmpty) {
        (i2, Some(Question(question, None)))
      }
      else {
        (i2, Some(Question(question, Some(answer))))
      }
    } else
      (i2, None)
  }

  private def getNextQuestion(startIndex: Int, allLines: Seq[String]): (Int, String) = {
    val builder = new StringBuilder
    appendToQuestionBuilder(startIndex, allLines, builder)
  }

  private def appendToAnswerBuilder(index: Int, allLines: Seq[String], builder: StringBuilder, lastWasBlank : Boolean): (Int, String) = {
    if (index >= allLines.length) {
      (index, builder.toString())
    } else {
      val trimmed = allLines(index).trim
      trimmed match {
        case s if s.isEmpty => {
          if (! lastWasBlank) {
            builder.append(allLines(index))
            appendToAnswerBuilder(index + 1, allLines, builder, true)
          }
          else
            (index, builder.toString())
        }
        case _ =>  {
          builder.append(allLines(index))
          appendToAnswerBuilder(index + 1, allLines, builder, false)
        }
      }
    }
  }

  private def appendToQuestionBuilder(index: Int, allLines: Seq[String], builder: StringBuilder): (Int, String) = {
    if (index >= allLines.length) {
      (index, builder.toString())
    } else {
      val trimmed = allLines(index).trim
      trimmed match {
        case s if s.isEmpty => {
          if (builder.isEmpty)
            appendToQuestionBuilder(index + 1, allLines, builder)
          else
            (index, builder.toString())
        }
        case _ =>  {
          builder.append(allLines(index))
          appendToQuestionBuilder(index + 1, allLines, builder)
        }
      }
    }
  }

  private def getNextAnswer(startIndex: Int, allLines: Seq[String]): (Int, String) = {
    val builder = new StringBuilder
    appendToAnswerBuilder(startIndex, allLines, builder, false)
  }


}
