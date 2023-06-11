/*
rule = EmptyCollectionsUnified
 */
package test

object EmptyCollectionsUnified {
  object list {
    val a = List[Int]()
    val b: List[Int] = List()
    val c = List.empty[Int]
    val d: List[Int] = List.empty

    // should rewrite List() to Nil when used within match case
    val e = a match {
      case List() => 1
      case _ => 2
    }

    // should not rewrite Nil when used with infix operator
    val f = 1 :: Nil

    // should not rewrite Nil when used with :: operator
    val g = ::(1, Nil)

    val h: List[Int] = Nil

    // should not rewrite Nil from match case
    val i = a match {
      case Nil => 1
      case _ => 2
    }

    // should not rewrite Nil from match case when used with infix extraction
    val j = a match {
      case _ :: Nil => 1
      case _ => 2
    }

    // should not rewrite Nil from match case when used with extraction
    val k = a match {
      case ::(_, Nil) => 1
      case _ => 2
    }

    // should not rewrite Nil when used within unapply
    val l = (a, a) match {
      case (_, Nil) => 1
      case _ => 2
    }

    // should not rewrite Nil when used within nested unapply
    val la = (a, List(a)) match {
      case (_, List(Nil)) => 1
      case _ => 2
    }

    // should rewrite List() to Nil when used within nested unapply
    val ld = (a, List(a)) match {
      case (_, List(List())) => 1
      case _ => 2
    }

    // should not rewrite Nil on alternative case match
    val lb = a match {
      case Nil | Nil => 1
      case _ => 2
    }

    // should rewrite List() to Nil on alternative case match
    val lc = a match {
      case List() | List() => 1
      case _ => 2
    }

    // should rewrite to Nil when used within unapply
    val m = (a, a) match {
      case (List(), List()) => 1
      case _ => 2
    }

    // non-empty list construction shouldn't be rewritten
    val n = List(1, 2, 3)

    // Nil in application not being :: should be replace by List.empty
    val o = List(Nil)

    // Nil in infix application not being :: should be replace by List.empty
    val q = List.empty ++ Nil

    // List() should be replace to Nil when deconstructing type
    val List(List(), r) = List(List(), List(1, 2))

    // Nil should not be replaced to List.empty when deconstructing type (nested)
    val List(List(List()), u) = List(List(List()), List(1, 2))

    // Nil should not be replaced when deconstructing type
    val List(Nil, v) = List(List(), List(1, 2))

    // Nil should not be replaced when deconstructing type (nested)
    val List(List(Nil), w) = List(List(List()), List(1, 2))

    // should respect suppresions
    // scalafix:off
    object suppresions {
      val a = List[Int]()
      val b: List[Int] = List()
      val c = List.empty[Int]
      val d: List[Int] = List.empty

      val e = a match {
        case List() => 1
        case _ => 2
      }

      val h: List[Int] = Nil

      val ld = (a, List(a)) match {
        case (_, List(List())) => 1
        case _ => 2
      }

      val lb = a match {
        case Nil | Nil => 1
        case _ => 2
      }

      val lc = a match {
        case List() | List() => 1
        case _ => 2
      }

      val m = (a, a) match {
        case (List(), List()) => 1
        case _ => 2
      }

      val o = List(Nil)

      val q = List.empty ++ Nil

      val List(List(), r) = List(List(), List(1, 2))

      val List(List(List()), u) = List(List(List()), List(1, 2))
    }
    // scalafix:on
  }

  object set {
    val a = Set[Int]()
    val b: Set[Int] = Set()
    val c = Set.empty[Int]
    val d: Set[Int] = Set.empty

    // scalafix:off
    object suppressions {
      val a = List[Int]()
      val b: List[Int] = List()
      val c = List.empty[Int]
      val d: List[Int] = List.empty
    }
    // scalafix:on
  }

  object map {
    val a = Map[Int, Int]()
    val b: Map[Int, Int] = Map()
    val c = Map.empty[Int, Int]
    val d: Map[Int, Int] = Map.empty

    // scalafix:off
    object suppressions {
      val a = Map[Int, Int]()
      val b: Map[Int, Int] = Map()
      val c = Map.empty[Int, Int]
      val d: Map[Int, Int] = Map.empty
    }
    // scalafix:on
  }
}
