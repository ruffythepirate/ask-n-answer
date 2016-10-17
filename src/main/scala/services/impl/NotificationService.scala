package services.impl

class NotificationService extends services.NotificationService {
  override def info(message: String): Unit = {
    println(s"Info - $message")
    displayToast("notification")(message)
  }

  override def error(message: String): Unit = {
    println(s"Error - $message")
    displayToast("alert")(message)
  }

  private def displayToast(level : String) = {
    message : String => {
      // define your applescript command
      val command = s"""display $level "$message" with title "ask-n-answer" """
      // run the command
      val runtime = Runtime.getRuntime
      val code = Array("osascript", "-e", command)
      val process = runtime.exec(code)
    }
  }
}
