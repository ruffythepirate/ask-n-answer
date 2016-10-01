package views

import org.scalatest.FunSpec

import scala.swing.TextField

class SearchPanelSpec extends FunSpec{

  describe("SearchPanel") {

    it("should initialize") {
      val searchPanel = new SearchPanel

    }

    it("should have a text box") {
      val searchPanel = new SearchPanel
      assert( searchPanel.contents.map(_.getClass).contains(classOf[TextField]) )
    }

  }

}
