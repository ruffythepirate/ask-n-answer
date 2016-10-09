package services.impl
import constants.AppEventConstants
import entities.{Topic, TopicSmall}
import services.Repository

class TopicService(feedbackService: services.FeedbackService, eventService: services.AppEventService) extends services.TopicService {
  override def deleteTopic(topic: TopicSmall): Unit = {
    val isOk = feedbackService.requestYesNo(s"Delete ${topic.name}?", "Are you sure you want to delete the topic?")

    if(isOk) {
      topic.repo.delete(topic)
      eventService.publishEvent(AppEventConstants.repositoryTopicsUpdated, topic.repo)
    }
  }

  override def createTopic(repo: Repository): Unit = {
    val topicName = feedbackService.requestStringInput("Create topic", "Please insert a name for the new topic.")
    if(topicName.nonEmpty) {
      repo.save(Topic(topicName, Seq.empty))
      eventService.publishEvent(AppEventConstants.repositoryTopicsUpdated, repo)
    }
  }
}
