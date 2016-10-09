package components

import entities.TopicSmall
import services._

import scala.swing.{BorderPanel, Button, FlowPanel}
import scala.swing.BorderPanel.Position
import scala.swing.event.ButtonClicked
import scalaswingcontrib.event.{TreeNodeSelected, TreePathSelected}
import scalaswingcontrib.tree.{Tree, TreeModel}

case class Node(value: String, tag: Option[Any], children: Node*)

class NavigatorPanel(repositoryService: RepositoryService, navigationService: NavigationService, topicService: TopicService,
                     feedbackService: FeedbackService) extends BorderPanel {

  private var selectedNode : Option[Node] = None

  val tree = new Tree[Node] {
    model = TreeModel[Node]()(_.children)
    renderer = Tree.Renderer(_.value)
    expandAll
    selection.mode = Tree.SelectionMode.Single
  }

  tree.selection.reactions += {
    case TreeNodeSelected(node) => {
      node match {
        case n: Node => {
          selectedNode = Some(n)
          n.tag match {
            case Some(ts: TopicSmall) =>
              navigationService.openTopic(ts)
            case _ =>
          }
        }
        case _ => {
          selectedNode = None
        }
      }
    }
  }

  def selectedRepository: Option[Repository] = {
    selectedNode.flatMap(_.tag match {
      case Some(ts : TopicSmall) => Some(ts.repo)
      case Some(repo : Repository) => Some(repo)
      case _ => None
    })
  }

  def selectedTopic: Option[TopicSmall] = {
    selectedNode.flatMap(_.tag match {
      case Some(ts : TopicSmall) => Some(ts)
      case _ => None
    })
  }

  val buttonPanel = new FlowPanel()
  val addTopicButton = Button("+")(onAddTopicClicked)
  val deleteTopicButton = Button("-")(onRemoveTopicClicked)

  def onAddTopicClicked(): Unit = {
    selectedRepository match {
      case Some(repo) =>
        topicService.createTopic(repo)
      case None =>
    }
  }

  def onRemoveTopicClicked(): Unit = {
    selectedTopic match {
      case Some(topic) =>
        topicService.deleteTopic(topic)
      case None =>
    }
  }

  buttonPanel.contents += addTopicButton
  buttonPanel.contents += deleteTopicButton

  layout(tree) = Position.Center
  layout(buttonPanel) = Position.South


  def initializeRepositories {
    val allRepositories = repositoryService.getRepositories

    val treeModel = if (allRepositories.nonEmpty) {
      TreeModel(allRepositories.map(repoToNode): _*)(_.children)

    } else {
      TreeModel(new Node("No Repositories Available", None))(_.children)
    }
    tree.model = treeModel

    tree.expandAll

  }

  private def repoToNode(repo: Repository): Node = {

    Node(repo.name, Some(repo), repo.getTopics.map(topicToNode): _*)
  }

  private def topicToNode(topic: TopicSmall) = {
    Node(topic.name, Some(topic), Seq.empty[Node]: _*)
  }
}
