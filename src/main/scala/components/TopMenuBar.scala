package components

import constants.AppEventConstants
import services.AppEventService

import scala.swing.{Action, Menu, MenuBar, MenuItem}

class TopMenuBar (appEventService: AppEventService) extends MenuBar{

  contents += new Menu("File") {
    contents += new MenuItem(Action("Save") {
      appEventService.publishEvent(AppEventConstants.saveCurrentTopic)
    })
  }
  contents += new Menu("View")
  contents += new Menu("About")

}
