package services

import entities.{RepositoryType, Topic, TopicSmall}

import scala.concurrent.Future

trait Repository {

  def getTopic(topicHead : TopicSmall) : Future[Topic]

  def getTopics : Seq[TopicSmall]

  def getType : RepositoryType.Value

  def save(topic : Topic) : Unit

  def name : String
}
