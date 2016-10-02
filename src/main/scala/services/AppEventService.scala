package services

case class AppEvent(name : String, data : Any)

trait AppEventService {

  def publishEvent(name : String, data : Any = null): Unit

 def subscribeToEvent(name : String, method : AppEvent => Unit)

 def unsubscribeFromEvent(name : String, method : AppEvent => Unit)
}
