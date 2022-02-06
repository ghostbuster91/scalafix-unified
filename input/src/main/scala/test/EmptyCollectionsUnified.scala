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
  }

  object set {
    val a = Set[Int]()
    val b: Set[Int] = Set()
    val c = Set.empty[Int]
    val d: Set[Int] = Set.empty
  }

  object map {
    val a = Map[Int, Int]()
    val b: Map[Int, Int] = Map()
    val c = Map.empty[Int, Int]
    val d: Map[Int, Int] = Map.empty
  }
}
