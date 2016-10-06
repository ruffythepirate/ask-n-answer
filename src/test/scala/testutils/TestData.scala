package testutils

import entities.{Question, Topic, TopicSmall}

object TestData {

  def createTopic = {
    val questions = Seq(Question("What is the question", Option("And that is the answer")))
    Topic("topic", questions)
  }



}
