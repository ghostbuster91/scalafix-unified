package test

object ExceptionMessagePassing {
  case class MyCustomException(message: String) extends RuntimeException(message)
}
