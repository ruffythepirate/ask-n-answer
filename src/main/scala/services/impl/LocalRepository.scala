package services.impl

import entities.{Question, RepositoryType, Topic, TopicSmall}
import utils.{QuestionToStringConverter, TextToQuestionConvert}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}

class LocalRepository(fileService: services.FileService)(implicit ec: ExecutionContext) extends services.Repository {

  override def getTopics: Seq[TopicSmall] = {
    val files = fileService.listFiles

    files.map(file => TopicSmall(file.getName, this))
  }

  override def save(topic: Topic): Unit = {

    val questionsAsText = QuestionToStringConverter.questionsToString(topic.questions)

    fileService.saveFile(topic.name, questionsAsText)
  }

  override def getTopic(topicHead: TopicSmall): Future[Topic] = {
    Future {

      val source = fileService.openFile(topicHead.name)

      var allLines: Seq[String] = Seq.empty

      var topic : Topic = null
      try {
        allLines = source.getLines().toSeq

        val questions = TextToQuestionConvert.getQuestionsFromText(allLines)
        topic = new Topic(topicHead.name, questions)
      } finally source.close()
      topic
    }
  }



  override def getType: _root_.entities.RepositoryType.Value = RepositoryType.Local

  val currentDir = fileService.pwd

  override def name: String = s"Local ($currentDir)"
}
