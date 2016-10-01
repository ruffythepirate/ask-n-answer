package views

import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import services.{Repository, RepositoryService}

import scalaswingcontrib.tree.Tree

class NavigatorPanelSpec extends FunSpec with MockitoSugar with BeforeAndAfter{

  var repositoryService : RepositoryService = _

  before {
    repositoryService = mock[RepositoryService]
    when(repositoryService.getRepositories).thenReturn(Seq[Repository]())

  }

  describe("NavigatorPanel") {

    it("should initialize") {
      val panel = createNavigatorPanel
    }

    it("should contain a tree control") {
      val panel = createNavigatorPanel
      assert( panel.contents.filter(_.isInstanceOf[Tree[Node]]).size === 1)
    }

    it("should get repositories on initializeRepositories") {
      val panel = createNavigatorPanel
      panel.initializeRepositories
      verify(repositoryService).getRepositories
    }

    it("initializes tree with returned repositories") {
      val mockRepos = Seq[Repository](
        createMockRepository("First"),
        createMockRepository("Second")
      )

      when(repositoryService.getRepositories).thenReturn(mockRepos)

      val panel = createNavigatorPanel
      panel.initializeRepositories

      assert( panel.tree.model.size === 2)
    }

    it("tells the user that there are no repos if list is empty") {
      val mockRepos = Seq[Repository](
      )

      when(repositoryService.getRepositories).thenReturn(mockRepos)

      val panel = createNavigatorPanel
      panel.initializeRepositories

      assert( panel.tree.model.filter(_ == Node("No Repositories Available", None)).size === 1)
    }
  }

  def createMockRepository(name : String) = {
    val repoMock = mock[Repository]

    when(repoMock.name).thenReturn(name)
    repoMock
  }

  def createNavigatorPanel = {
     new NavigatorPanel (repositoryService)
  }
}
