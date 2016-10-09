package services

trait FeedbackService {

  def requestYesNo(title: String, body: String) : Boolean

  def requestStringInput(title: String, body: String) : String
}
