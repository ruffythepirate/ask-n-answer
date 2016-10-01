package views

import javax.swing.JTree

import org.scalatest.FunSpec

import scalaswingcontrib.tree.Tree

class NavigatorPanelSpec extends FunSpec {

  describe("NavigatorPanel") {

    it("should initialize") {
      val panel = new NavigatorPanel
    }

    it("should contain a tree control") {
      val panel = new NavigatorPanel
      assert( panel.contents.filter(_.isInstanceOf[Tree[Node[String]]]).size === 1)
    }
  }
}
