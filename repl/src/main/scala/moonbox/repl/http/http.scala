package moonbox.repl

import org.json4s.DefaultFormats

package object http {

  import org.json4s.jackson.Serialization.writePretty
  implicit val formats = DefaultFormats

  def toJson[T <: AnyRef](obj: T) = writePretty[T](obj)
}
