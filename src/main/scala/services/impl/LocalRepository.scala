package services.impl

import entities.{Question, RepositoryType, Topic, TopicSmall}
import utils.TextToQuestionConvert

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}

class LocalRepository(fileService: services.FileService)(implicit ec: ExecutionContext) extends services.Repository {

  override def getTopics: Seq[TopicSmall] = {
    val files = fileService.listFiles

    files.map(file => TopicSmall(file.getName, this))
  }

  override def getTopic(topicHead: TopicSmall): Future[Topic] = {
    Future {

      val source = fileService.openFile(topicHead.name)

      var allLines: Seq[String] = Seq.empty

      var topic : Topic = null
      try {
        allLines = source.getLines().toSeq

        val questions = getQuestionsFromText(allLines)
        topic = new Topic(topicHead.name, questions)
      } finally source.close()
      topic
    }
  }

  def getQuestionsFromText(allLines: Seq[String]): ArrayBuffer[Question] = {
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

  override def getType: _root_.entities.RepositoryType.Value = RepositoryType.Local

  val currentDir = fileService.pwd

  override def name: String = s"Local ($currentDir)"
}
