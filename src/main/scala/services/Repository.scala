package services

import entities.{RepositoryType, TopicSmall}

trait Repository {

  def getTopics : Seq[TopicSmall]

  def getType : RepositoryType.Value

  def name : String
}
