/*
rule = ExceptionMessagePassing
 */
package test

object ExceptionMessagePassing {
  case class MyCustomException(override val getMessage: String) extends RuntimeException
}
