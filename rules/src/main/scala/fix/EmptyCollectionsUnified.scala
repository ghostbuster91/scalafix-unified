package fix

import scalafix.v1._
import scala.meta._

class EmptyCollectionsUnified extends SemanticRule("EmptyCollectionsUnified") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t @ q"List()"        => Patch.replaceTree(t, "List.empty")
      case t @ q"List[$e]()"    => Patch.replaceTree(t, s"List.empty[$e]")
      case t @ q"Set()"         => Patch.replaceTree(t, "Set.empty")
      case t @ q"Set[$e]()"     => Patch.replaceTree(t, s"Set.empty[$e]")
      case t @ q"Map()"         => Patch.replaceTree(t, "Map.empty")
      case t @ q"Map[$k, $v]()" => Patch.replaceTree(t, s"Map.empty[$k, $v]")
      case _                    => Patch.empty
    }.asPatch
  }
}
