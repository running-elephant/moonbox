package moonbox.client

import scala.collection.mutable

class ClientOptions(val options: CaseInsensitiveMap[String]) {

  import ClientOptions._

  def this(ops: Map[String, String]) = this(CaseInsensitiveMap(ops))
  def this() = this(Map.empty[String, String])

  val host = options.getOrElse(HOST, "localhost")
  val port = options.get(PORT).map(_.toInt).getOrElse(10010)
  val database = options.getOrElse(DATABASE, "default")
  val user = options.get(USER)
  val password = options.get(PASSWORD)
  val timeout = options.get(READ_TIMEOUT).map(_.toInt).getOrElse(3600) /* time_unit: second */
  val fetchSize = options.get(FETCH_SIZE).map(_.toInt).getOrElse(1000)
  val maxRows = options.get(MAX_ROWS).map(_.toLong).getOrElse(10000L)
  val isLocal = options.get(IS_LOCAL).forall(_.toBoolean)
  val serializer = options.getOrElse(SERIALIZER, "protobuf")
}

object ClientOptions {

  val HOST = "host"
  val PORT = "port"
  val USER = "user"
  val PASSWORD = "password"
  val DATABASE = "database"
  val READ_TIMEOUT = "timeout"  /* time unit: second */
  val FETCH_SIZE = "fetchsize"
  val MAX_ROWS = "maxrows"
  val IS_LOCAL = "islocal"
  val SERIALIZER = "serializer"

  def builder() = new Builder()
  def builder(clientOptions: ClientOptions) = new Builder().options(clientOptions.options)

  class Builder {
    private val _options = mutable.Map.empty[String, String]
    def options(ops: Map[String, String]) = {
      _options ++= ops
      this
    }
    def host(name: String) = {
      _options += (HOST -> name)
      this
    }
    def port(value: Int) = {
      _options += (PORT -> value.toString)
      this
    }
    def user(name: String) = {
      _options += (USER -> name)
      this
    }
    def password(value: String) = {
      _options += (PASSWORD -> value)
      this
    }
    def database(name: String) = {
      _options += (DATABASE -> name)
      this
    }
    def timeout(seconds: Int) = {
      _options += (READ_TIMEOUT -> seconds.toString)
      this
    }
    def fetchSize(value: Int) = {
      _options += (FETCH_SIZE -> value.toString)
      this
    }
    def maxRows(value: Long) = {
      _options += (MAX_ROWS -> value.toString)
      this
    }
    def isLocal(value: Boolean) = {
      _options += (IS_LOCAL -> value.toString)
      this
    }
    def serializer(name: String) = {
      _options += (SERIALIZER -> name)
      this
    }
    def build() = new ClientOptions(_options.toMap)
  }
}
