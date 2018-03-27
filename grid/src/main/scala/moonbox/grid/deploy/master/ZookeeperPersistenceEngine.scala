package moonbox.grid.deploy.master

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import akka.actor.ActorSystem
import akka.remote.ContainerFormats.ActorRef
import akka.serialization.SerializationExtension
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.zookeeper.CreateMode

import scala.collection.JavaConversions._
import scala.reflect.ClassTag

class ZookeeperPersistenceEngine(conf: MbConf, akkaSystem: ActorSystem) extends PersistenceEngine with MbLogging {
	private val ZK_CONNECTION_TIMEOUT_MILLIS = 15000
	private val ZK_SESSION_TIMEOUT_MILLIS = 60000
	private val WORKING_DIR = conf.get(PERSIST_WORKING_DIR.key, PERSIST_WORKING_DIR.defaultValueString + "/master_status")

	private val zk = {
		val servers = conf.get(PERSIST_SERVERS.key, PERSIST_SERVERS.defaultValueString)
		val retryTimes = conf.get(PERSIST_RETRY_TIMES.key, PERSIST_RETRY_TIMES.defaultValue.get)
		val interval = conf.get(PERSIST_RETRY_WAIT.key, PERSIST_RETRY_WAIT.defaultValue.get).toInt
		val client = CuratorFrameworkFactory.newClient(servers,
			ZK_SESSION_TIMEOUT_MILLIS, ZK_CONNECTION_TIMEOUT_MILLIS,
			new ExponentialBackoffRetry(interval, retryTimes))
		client.start()
		client
	}
	override def persist(name: String, obj: Object): Unit = {
		serializeInfoFile(WORKING_DIR  + "/" + name, obj)
	}

	override def unpersist(name: String): Unit = {
		zk.delete().forPath(WORKING_DIR + "/" + name)
	}

	override def read[T: ClassTag](prefix: String): Seq[T] = {
		zk.getChildren.forPath(WORKING_DIR).filter(_.startsWith(prefix)).flatMap { name =>
			deserializeFromFile[T](name, zk.getData.forPath(WORKING_DIR + "/" + name))
		}
	}

	private def serializeInfoFile(path: String, value: AnyRef): Unit = {
		try {
			val serialized: Array[Byte] = value match {
				case actorRef: ActorRef =>
					SerializationExtension(akkaSystem).serialize(value).get
				case _ =>
					val bos = new ByteArrayOutputStream()
					val oos = new ObjectOutputStream(bos)
					oos.writeObject(value)
					oos.flush()
					oos.close()
					bos.toByteArray
			}
			zk.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, serialized)
		} catch {
			case e: Exception =>
				logWarning("Exception while serializing persist data.")
		}
	}

	private def deserializeFromFile[T: ClassTag](filename: String, bytes: Array[Byte]): Option[T] = {
		import scala.reflect._
		try {
			val clazz = classTag[T].runtimeClass.asInstanceOf[Class[T]]
			clazz.getSimpleName match {
				case "ActorRef" =>
					Some(SerializationExtension(akkaSystem).deserialize(bytes, clazz).get)
				case _ =>
					val bis: ByteArrayInputStream = new ByteArrayInputStream(bytes)
					val ois: ObjectInputStream = new ObjectInputStream(bis)
					Some(ois.readObject().asInstanceOf[T])
			}

		} catch {
			case e: Exception =>
				logWarning("Exception while reading persisted data. Delete it.")
				zk.delete().forPath(filename)
				None
		}
	}
}

