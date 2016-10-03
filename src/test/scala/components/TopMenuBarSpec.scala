package components

import org.scalatest.{BeforeAndAfter, FunSpec}

import scala.swing.MenuItem
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import services.AppEventService

class TopMenuBarSpec extends FunSpec with BeforeAndAfter with MockitoSugar{

  var cut : TopMenuBar = _
  var appEventService : AppEventService = _



  describe("TopMenuBar") {
    before {
      appEventService = mock[AppEventService]
      cut = new TopMenuBar(appEventService)
    }
    describe("initialization") {

      it("can be initialized") {
      }

      val topLevelMenus = Seq("File", "View", "About")

      topLevelMenus.foreach( menu => {
        it(s"contains a $menu main category") {
          assert(cut !== null)
          assert(cut.contents.map(_.asInstanceOf[MenuItem]).map(_.text).contains(menu))
        }
      })
    }
  }
}
