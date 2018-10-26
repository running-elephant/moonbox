/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.grid.deploy2.node

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
	private val WORKING_DIR = conf.get(PERSIST_WORKING_DIR.key, PERSIST_WORKING_DIR.defaultValueString)

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
		serializeInfoFile(name, obj)
	}

	override def unpersist(name: String): Unit = {
		zk.delete().forPath(WORKING_DIR + "/" + name)
	}

	override def read[T: ClassTag](prefix: String): Seq[T] = {
		if (exist(prefix)) {
			zk.getChildren.forPath(WORKING_DIR + "/" + prefix).flatMap { name =>
				deserializeFromFile[T](prefix + "/" + name, zk.getData.forPath(WORKING_DIR + "/" + prefix + "/" + name))
			}
		} else {
			Seq[T]()
		}
	}

	private def serializeInfoFile(path: String, value: AnyRef): Unit = {
		try {
			val serialized: Array[Byte] = SerializationExtension(akkaSystem).serialize(value).get
			/*value match {
				case actorRef: ActorRef =>
					SerializationExtension(akkaSystem).serialize(value).get
				case _ =>
					val bos = new ByteArrayOutputStream()
					val oos = new ObjectOutputStream(bos)
					oos.writeObject(value)
					oos.flush()
					oos.close()
					bos.toByteArray
			}*/
			if (exist(path)) {
				zk.delete().deletingChildrenIfNeeded().forPath(WORKING_DIR  + "/" + path)
			}
			zk.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(WORKING_DIR  + "/" + path, serialized)
		} catch {
			case e: Exception =>
				logWarning(s"Exception while serializing persist data. ${e.getMessage}")
		}
	}

	private def deserializeFromFile[T: ClassTag](filename: String, bytes: Array[Byte]): Option[T] = {
		import scala.reflect._
		try {
			val clazz = classTag[T].runtimeClass.asInstanceOf[Class[T]]
			Some(SerializationExtension(akkaSystem).deserialize(bytes, clazz).get)
			/*clazz.getSimpleName match {
				case "ActorRef" =>
					Some(SerializationExtension(akkaSystem).deserialize(bytes, clazz).get)
				case _ =>
					val bis: ByteArrayInputStream = new ByteArrayInputStream(bytes)
					val ois: ObjectInputStream = new ObjectInputStream(bis)
					Some(ois.readObject().asInstanceOf[T])
			}*/
		} catch {
			case e: Exception =>
				logWarning("Exception while reading persisted data. Delete it.")
				zk.delete().deletingChildrenIfNeeded().forPath(WORKING_DIR + "/" +filename)
				None
		}
	}

	override def exist(path: String) = {
		null != zk.checkExists().forPath(WORKING_DIR + "/" + path)
	}
}

