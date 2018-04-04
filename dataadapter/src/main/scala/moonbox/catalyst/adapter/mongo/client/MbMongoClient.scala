package moonbox.catalyst.adapter.mongo.client

import java.util.Properties

import com.mongodb.{MongoClient, MongoCredential, ServerAddress}
import moonbox.catalyst.adapter.mongo.util.MongoJDBCUtils

import scala.collection.JavaConverters._

case class MbMongoClient(props: Properties) {
  val host: String = props.getProperty(MongoJDBCUtils.HOST_KEY, "127.0.0.1")
  val port: Int = props.getProperty(MongoJDBCUtils.PORT_KEY, "27017").toInt
  val user: String = props.getProperty(MongoJDBCUtils.USER_KEY)
  val pwd: String = props.getProperty(MongoJDBCUtils.PASSWORD_KEY)
  val database: String = props.getProperty(MongoJDBCUtils.DB_NAME, "test")
  val hostsAndPortPairs = props.getProperty(MongoJDBCUtils.HOSTS_AND_PORTS, "127.0.0.1:27017")
  val mongoServers = MongoJDBCUtils.parseHostsAndPorts(hostsAndPortPairs).map { pair =>
    new ServerAddress(pair._1, pair._2.toInt)
  }

  val credential = if (user != null && pwd != null) MongoCredential.createCredential(user, database, pwd.toCharArray) else null

  val client: MongoClient = if (credential == null) new MongoClient(mongoServers.asJava) else new MongoClient(mongoServers.asJava, credential, null)

  val dbName: String = props.getProperty(MongoJDBCUtils.DB_NAME, "test")
  val collectionName: String = props.getProperty(MongoJDBCUtils.COLLECTION_NAME)
}