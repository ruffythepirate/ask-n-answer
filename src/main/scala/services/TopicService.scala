package services

import entities.TopicSmall

trait TopicService {

  def createTopic(repo : Repository)

  def deleteTopic(topic : TopicSmall)

}
