package services.impl
import constants.AppEventConstants
import services.AppEvent

class AppEventService extends services.AppEventService{

  private var topicListeners = Map[String, Seq[AppEvent => Unit]]()

  override def publishEvent(name: String, data: Any): Unit = {
    val listeners = topicListeners(name)

    val event = AppEvent(name, data)
    listeners.foreach( fun => try {
      fun(event)
    }
    catch  {
      case _ =>
    }
    )
  }

  override def subscribeToEvent(name: String, method: (AppEvent) => Unit): Unit = {
    if(topicListeners.contains(name)) {
      topicListeners = topicListeners + (name -> (topicListeners(name) :+ method))
    } else {
      topicListeners = topicListeners + (name -> Seq(method))
    }
  }

  override def unsubscribeFromEvent(name: String, method: (AppEvent) => Unit): Unit = ???
}
