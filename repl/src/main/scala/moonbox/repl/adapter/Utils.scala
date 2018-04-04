package moonbox.repl.adapter

import org.json.JSONObject

object Utils {
  // name, type, nullable
  def parseJson(json: String): Array[(String, String, Boolean)] = {
    import scala.collection.JavaConversions._
    val schemaObject = new JSONObject(json)
    schemaObject.getJSONArray("fields").map {
      case elem: JSONObject =>
        val columnName = elem.getString("name")
        val nullable = elem.getBoolean("nullable")
        val columnType = elem.get("type") match {
          case v: JSONObject => v.getString("type")
          case s => s.toString
        }
        (columnName, columnType, nullable)
      case _ => null
    }.filter(_ != null).toArray
  }
}
