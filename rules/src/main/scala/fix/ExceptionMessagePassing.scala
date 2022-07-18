package fix

import scalafix.v1._

import scala.annotation.tailrec
import scala.meta._

class ExceptionMessagePassing extends SemanticRule("ExceptionMessagePassing") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case clazz @ Defn.Class(_, _, _, _, Template(_, Init(_, _, Nil) :: _, _, _)) =>
        clazz.symbol.info.get.signature match {
          case ClassSignature(typeParameters, parents, self, declarations) =>
            println(s"Kasper1: ${parents}")
            println(s"Kornel ${simpleDealias(parents.head)}")
            parents.head match {
              case TypeRef(prefix, symbol, typeArguments) =>
                println(s"Kasper: ${}")
                val cl = this.getClass.getClassLoader
                println(s"Kamil ${toFqn(symbol)}")
                println(s"Kamil ${cl.loadClass("RuntimeException")}")
                symbol.info.get.signature match {
                  case ValueSignature(tpe) => println("ValueSignature")
                  case ClassSignature(typeParameters, parents, self, declarations) => println("ClassSignature")
                  case MethodSignature(typeParameters, parameterLists, returnType) => println("MethodSignature")
                  case TypeSignature(typeParameters, lowerBound, upperBound) =>
                    lowerBound match {
                      case TypeRef(prefix, symbol, typeArguments) => println(s"TypeRef ${symbol}")
                      case SingleType(prefix, symbol) => println("SingleType")
                      case ThisType(symbol) => println("ThisType")
                      case SuperType(prefix, symbol) => println("SuperType")
                      case ConstantType(constant) => println("ConstantType")
                      case IntersectionType(types) => println("IntersectionType")
                      case UnionType(types) => println("UnionType")
                      case WithType(types) => println("WithType")
                      case StructuralType(tpe, declarations) => println("StructuralType")
                      case AnnotatedType(annotations, tpe) => println("AnnotatedType")
                      case ExistentialType(tpe, declarations) => println("ExistentialType")
                      case UniversalType(typeParameters, tpe) => println("UniversalType")
                      case ByNameType(tpe) => println("ByNameType")
                      case RepeatedType(tpe) => println("RepeatedType")
                      case NoType => println("NoType")
                    }
                  case NoSignature => println("NoSignature")
                }
                println(s"Kasper: ${symbol.info.get.symbol}")

                println(SymbolMatcher.normalized("java.lang.RuntimeException").matches(symbol.normalized))
                println(s"Kasper: ${typeArguments}")
              case SingleType(prefix, symbol) => println("SingleType")
              case ThisType(symbol) => println("ThisType")
              case SuperType(prefix, symbol) => println("SuperType")
              case ConstantType(constant) => println("ConstantType")
              case IntersectionType(types) => println("IntersectionType")
              case UnionType(types) => println("UnionType")
              case WithType(types) => println("WithType")
              case StructuralType(tpe, declarations) => println("StructuralType")
              case AnnotatedType(annotations, tpe) => println("AnnotatedType")
              case ExistentialType(tpe, declarations) => println("ExistentialType")
              case UniversalType(typeParameters, tpe) => println("UniversalType")
              case ByNameType(tpe) => println("ByNameType")
              case RepeatedType(tpe) => println("RepeatedType")
              case NoType => println("NoType")
            }
            Patch.empty
          case _ => Patch.empty
        }
      case t => Patch.empty
    }.asPatch
  }

  def toFqn(symbol: Symbol) =
    symbol.value.replaceAll("\\.$", "\\$").replaceAll("/", ".").stripSuffix("#").stripPrefix("_root_.")

  def simpleDealias(tpe: SemanticType)(implicit doc: SemanticDocument): SemanticType = {
    def dealiasSymbol(symbol: Symbol): Symbol =
      symbol.info.get.signature match {
        case TypeSignature(_, lowerBound @ TypeRef(_, dealiased, _), upperBound) if lowerBound == upperBound =>
          dealiased
        case _ =>
          symbol
      }
    tpe match {
      case TypeRef(prefix, symbol, typeArguments) =>
        TypeRef(prefix, dealiasSymbol(symbol), typeArguments.map(simpleDealias))
    }
  }

}
