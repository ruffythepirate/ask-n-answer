package views

import entities.{Topic, TopicSmall}
import services.{FileService, Repository, RepositoryService}

import scala.swing.BorderPanel.Position
import scala.swing.BorderPanel
import scalaswingcontrib.tree.{Tree, TreeModel}

case class Node( value: String, tag : Option[Any], children: Node*)

class NavigatorPanel (repositoryService: RepositoryService) extends BorderPanel{

  val tree = new Tree[Node] {
      model = TreeModel[Node]()(_.children)
      renderer = Tree.Renderer(_.value)
    }

  layout(tree) = Position.Center

  def initializeRepositories {
    val allRepositories = repositoryService.getRepositories

    val treeModel = if(allRepositories.nonEmpty) {
      TreeModel(allRepositories.map( repoToNode ):_*)(_.children)

    } else {
      TreeModel(new Node("No Repositories Available", None))(_.children)
    }
    tree.model = treeModel
  }

  private def repoToNode(repo : Repository): Node = {

    Node(repo.name, Some(repo), repo.getTopics.map(topicToNode):_* )
  }

  private def topicToNode(topic : TopicSmall) = {
    Node(topic.name, Some(topic), Seq.empty[Node]:_*)
  }
}
