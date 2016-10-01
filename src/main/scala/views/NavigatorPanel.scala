package views

import scala.swing.BorderPanel.Position
import scala.swing.BorderPanel
import scalaswingcontrib.tree.{Tree, TreeModel}

case class Node[A](value: A, children: Node[A]*)

class NavigatorPanel extends BorderPanel{


  val menuItems = Node("Hobbies", Node("Skateboarding"))

  val tree = new Tree[Node[String]] {
      model = TreeModel(menuItems)(_.children)
      renderer = Tree.Renderer(_.value)
    }

  layout(tree) = Position.Center
}
