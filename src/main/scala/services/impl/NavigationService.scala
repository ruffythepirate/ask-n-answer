package services.impl
import constants.AppEventConstants
import entities.TopicSmall

class NavigationService(appEventService: services.AppEventService) extends services.NavigationService{
  override def openTopic(topic: TopicSmall) {

    appEventService.publishEvent(AppEventConstants.openTopic, topic)
  }
}
