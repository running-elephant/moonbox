package moonbox.catalyst.adapter.mongo.client

import java.util.Properties

import com.mongodb.{MongoClient, MongoClientOptions, MongoCredential, ServerAddress}
import moonbox.catalyst.adapter.mongo.util.MongoJDBCUtils

import scala.collection.JavaConverters._

case class MbMongoClient(cli: MongoClient, props: Properties) {
  //host, port, user, password, database, collection,
  val host: String = props.getProperty(MongoJDBCUtils.HOST_KEY, "127.0.0.1")

  def port: Int = props.getProperty(MongoJDBCUtils.PORT_KEY, "27017").toInt

  val database: String = props.getProperty(MongoJDBCUtils.DB_NAME)
  val collectionName: String = Option(props.getProperty(MongoJDBCUtils.COLLECTION_KEY)).getOrElse(props.getProperty("collection"))
  private var _client: MongoClient = cli

  def client: MongoClient = {
    if (_client == null) {
      synchronized {
        if (_client == null) {
          val user: String = props.getProperty(MongoJDBCUtils.USER_KEY)
          val pwd: String = props.getProperty(MongoJDBCUtils.PASSWORD_KEY)
          val hostsAndPortPairs = props.getProperty(MongoJDBCUtils.HOSTS_AND_PORTS, "127.0.0.1:27017")
          val mongoServers = MongoJDBCUtils.parseHostsAndPorts(hostsAndPortPairs).map { pair => new ServerAddress(pair._1, pair._2.toInt) }
          val authSource: String = props.getProperty(MongoJDBCUtils.AUTH_SOURCE, database)
          val credential = if (user != null && pwd != null) MongoCredential.createCredential(user, authSource, pwd.toCharArray) else null
          _client = if (credential == null) new MongoClient(mongoServers.asJava) else new MongoClient(mongoServers.asJava, credential, MongoClientOptions.builder().build())
        }
      }
    }
    _client
  }

  def this(properties: Properties) = this(null, properties)

  def close(): Unit = {
    if (_client != null)
      _client.close()
  }
}