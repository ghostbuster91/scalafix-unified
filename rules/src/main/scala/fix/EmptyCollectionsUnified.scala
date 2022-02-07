package fix

import scalafix.v1._

import scala.meta._

class EmptyCollectionsUnified extends SemanticRule("EmptyCollectionsUnified") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t @ q"List[$e]()" => Patch.replaceTree(t, s"List.empty[$e]")
      case t @ Term.Name("List") =>
        t.parent match {
          // val b: List[Int] = List()
          case Some(p @ Term.Apply(q, Nil)) =>
            Patch.replaceTree(p, "List.empty")
          //  case (List(), List()) =>
          //  case List() =>
          case Some(p @ p"List()") => Patch.replaceTree(p, "Nil")
          case _ => Patch.empty
        }
      case t @ q"Nil" =>
        t.parent match {
          // ::(_, Nil)
          case Some(Term.Apply(_, _)) => Patch.empty
          // 1 :: Nil
          case Some(Term.ApplyInfix(_)) => Patch.empty
          // case Nil =>
          case Some(Case(_)) => Patch.empty
          // case _ :: Nil =>
          case Some(p"$_ $_ (..$_)") => Patch.empty
          // case ::(_, Nil) =>
          case Some(p"$_(..$_)") => Patch.empty
          // case (_, Nil) =>
          case Some(p"(..$_)") => Patch.empty
          case _ => Patch.replaceTree(t, s"List.empty")
        }
      case t @ q"Set()" => Patch.replaceTree(t, "Set.empty")
      case t @ q"Set[$e]()" => Patch.replaceTree(t, s"Set.empty[$e]")
      case t @ q"Map()" => Patch.replaceTree(t, "Map.empty")
      case t @ q"Map[$k, $v]()" => Patch.replaceTree(t, s"Map.empty[$k, $v]")
      case _ => Patch.empty
    }.asPatch
  }
}
