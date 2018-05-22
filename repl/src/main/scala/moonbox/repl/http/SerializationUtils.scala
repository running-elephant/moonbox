package moonbox.repl.http

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object SerializationUtils {

  val mapper = new ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(SerializationFeature.INDENT_OUTPUT, true)
    .registerModule(DefaultScalaModule)

  def toJson(obj: AnyRef): String = {
    mapper.writeValueAsString(obj)
  }

  def fromJson[T](json: String, clazz: Class[T])= {
    mapper.readValue(json, clazz)
  }

}
