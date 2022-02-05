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

    val e = a match {
      case List() => 1
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