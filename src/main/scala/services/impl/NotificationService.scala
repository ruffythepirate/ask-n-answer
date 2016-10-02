package services.impl

class NotificationService extends services.NotificationService {
  override def info(message: String): Unit = {
    println(s"Info - $message")
  }

  override def error(message: String): Unit = {
    println(s"Error - $message")
  }
}
