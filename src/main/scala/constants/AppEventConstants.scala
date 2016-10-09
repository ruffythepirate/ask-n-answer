package constants

object AppEventConstants extends Enumeration{

  val openTopic = "openTopic"

  val topicEdited = "topicEdited"
  val topicSaved = "topicSaved"

  val repositoryTopicsUpdated = "repositoryTopicsUpdated"

  val saveCurrentTopic = "saveCurrentTopic"
  val saveAllTopics = "saveAllTopics"
}
