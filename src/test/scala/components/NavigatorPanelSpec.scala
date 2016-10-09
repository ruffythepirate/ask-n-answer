package components

import entities.TopicSmall
import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import services._
import org.mockito.Matchers.any

import scalaswingcontrib.tree.Tree

class NavigatorPanelSpec extends FunSpec with MockitoSugar with BeforeAndAfter{

  var repositoryService : RepositoryService = _
  var navigationService : NavigationService = _
  var topicService : TopicService = _
  var feedbackService : FeedbackService = _

  before {
    repositoryService = mock[RepositoryService]
    when(repositoryService.getRepositories).thenReturn(Seq[Repository]())

    navigationService = mock[NavigationService]
    topicService = mock[TopicService]
    feedbackService = mock[FeedbackService]

  }

  describe("NavigatorPanel") {

    describe("initialization") {
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

      it("initializes repository node with topics") {
        val mockRepos = Seq[Repository](
          createMockRepository("First", createTopicSmall("First"), createTopicSmall("Second"))
        )

        when(repositoryService.getRepositories).thenReturn(mockRepos)

        val panel = createNavigatorPanel
        panel.initializeRepositories

        val firstNode = panel.tree.model.roots(0)
        assert( firstNode.children.size === 2)
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

    describe("selecing a topic") {
      it("should inform the navigation service") {
        val panel = createInitializedNavigatorPanel
        panel.tree.selectPaths(Tree.Path(panel.tree.model.roots(0).children(0)))

        verify(navigationService).openTopic(any())
      }
    }

    describe("add/remove topics") {
      it("should do nothing if no repository is selected and add topic is clicked") {
        val panel = createInitializedNavigatorPanel

        panel.onAddTopicClicked()

        verifyZeroInteractions(topicService)
      }

      it("should call topic service if a repo is selected and add topic is clicked") {
        val panel = createInitializedNavigatorPanel
        panel.tree.selectPaths(Tree.Path(panel.tree.model.roots(0).children(0)))

        panel.onAddTopicClicked()
        verify(topicService).createTopic(any())
      }

      it("should call the topic service if a topic is selected and remove is clicked") {
        val panel = createInitializedNavigatorPanel
        panel.tree.selectPaths(Tree.Path(panel.tree.model.roots(0).children(0)))

        panel.onRemoveTopicClicked()
        verify(topicService).deleteTopic(any())
      }

      it("should do nothing if no repo is selected and remove is clicked.") {
        val panel = createInitializedNavigatorPanel
        panel.onRemoveTopicClicked()
        verifyZeroInteractions(topicService)
      }
    }
 }

  def createMockRepository(name : String, topics : TopicSmall*) = {
    val repoMock = mock[Repository]

    when(repoMock.getTopics).thenReturn(topics)

    when(repoMock.name).thenReturn(name)
    repoMock
  }

  def createInitializedNavigatorPanel = {
    val mockRepos = Seq[Repository](
      createMockRepository("First", createTopicSmall("First"), createTopicSmall("Second"))
    )

    when(repositoryService.getRepositories).thenReturn(mockRepos)

    val panel = createNavigatorPanel
    panel.initializeRepositories
    panel
  }

  def createTopicSmall(name : String) = {
    TopicSmall(name, null)
  }
  def createNavigatorPanel = {
     new NavigatorPanel (repositoryService, navigationService, topicService, feedbackService)
  }
}
