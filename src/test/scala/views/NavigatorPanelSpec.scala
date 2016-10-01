package views

import javax.swing.JTree

import org.scalatest.FunSpec
import services.{Repository, RepositoryService}

import scalaswingcontrib.tree.Tree

class NavigatorPanelSpec extends FunSpec {

  describe("NavigatorPanel") {

    it("should initialize") {
      val panel = createNavigatorPanel
    }

    it("should contain a tree control") {
      val panel = createNavigatorPanel
      assert( panel.contents.filter(_.isInstanceOf[Tree[Node]]).size === 1)
    }
  }

  def createNavigatorPanel = {
     new NavigatorPanel ( new RepositoryService {
       override def getRepositories: Seq[Repository] = ???
     })
  }
}
