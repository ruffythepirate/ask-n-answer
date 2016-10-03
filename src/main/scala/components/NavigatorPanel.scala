package components

import entities.TopicSmall
import services.{NavigationService, Repository, RepositoryService}

import scala.swing.{BorderPanel, Button}
import scala.swing.BorderPanel.Position
import scala.swing.event.ButtonClicked
import scalaswingcontrib.event.{TreeNodeSelected, TreePathSelected}
import scalaswingcontrib.tree.{Tree, TreeModel}

case class Node( value: String, tag : Option[Any], children: Node*)

class NavigatorPanel (repositoryService: RepositoryService, navigationService : NavigationService) extends BorderPanel{

  val tree = new Tree[Node] {
      model = TreeModel[Node]()(_.children)
      renderer = Tree.Renderer(_.value)
      expandAll
      selection.mode = Tree.SelectionMode.Single
    }

  val myButton = Button("Nothing yet"){
  }

  myButton.reactions += {
    case ButtonClicked(myButton) =>
        myButton.text = "At least I was clicked"
  }

  tree.selection.reactions += {
    case TreeNodeSelected(node) => {
      node match {
        case n:Node => {
          myButton.text = s"Node ${n.value} selected..."
          n.tag match {
            case Some(ts : TopicSmall) =>
              navigationService.openTopic(ts)
            case _ =>
          }
//          navigationService.openTopic(null, topic)
        }
        case _ => {
          myButton.text = "No node selected..."
        }
      }
    }
  }


  layout(myButton) = Position.North
  layout(tree) = Position.Center



  def initializeRepositories {
    val allRepositories = repositoryService.getRepositories

    val treeModel = if(allRepositories.nonEmpty) {
      TreeModel(allRepositories.map( repoToNode ):_*)(_.children)

    } else {
      TreeModel(new Node("No Repositories Available", None))(_.children)
    }
    tree.model = treeModel

    tree.expandAll

  }

  private def repoToNode(repo : Repository): Node = {

    Node(repo.name, Some(repo), repo.getTopics.map(topicToNode):_* )
  }

  private def topicToNode(topic : TopicSmall) = {
    Node(topic.name, Some(topic), Seq.empty[Node]:_*)
  }
}
