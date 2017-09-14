/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.common

import java.io.{FileInputStream, IOException, InputStreamReader, _}
import java.net.InetAddress
import java.nio.charset.StandardCharsets
import java.util.Properties

import akka.actor.{ActorRef, ActorSystem}
import akka.serialization.SerializationExtension

import scala.collection.JavaConverters._
import scala.reflect.ClassTag
import scala.util.Try


object Util extends EdpLogging {

	def datasourceRelationProvider(source: String): String = {
		source.toLowerCase match {
			case "mysql" | "oracle" => "jdbc"
			case "hbase" => "org.apache.spark.sql.execution.datasource.hbase"
			case "es5" | "es2" | "elasticsearch" => "org.elasticsearch.spark.sql "
			case "parquet" => "parquet"
			case "json" => "json"
			case "csv" => "csv"
		}
	}

	def getPluginJars(env: Map[String, String] = sys.env): List[String] = {
		val plginDir: Option[String] = env.get("MOONBOX_CONF_DIR").orElse(env.get("MOONBOX_HOME").map {t => s"$t${File.separator}plugin"})
		if(plginDir.isEmpty) {
			//TODO
			throw new Exception("$MOONBOX_HOME does not exist")
		} else {
			val lib = new File(plginDir.get)
			if (lib.exists()) {
				val confFile = lib.listFiles().filter {_.isFile}.map (_.getAbsolutePath)
				confFile.toList
			} else {
				List()
			}
		}
	}

	def getOrException(props: Map[String, String], key: String): String = {
		props.getOrElse(key, throw new Exception(s"$key is not defined"))
	}

	def akkaAddress(clusterName: String,
	                hostname: String,
	                port: Int,
	                path: String): String = {
		s"akka.tcp://$clusterName@$hostname:$port$path"
	}

	def serialize[T](data: T)(implicit system: ActorSystem): Array[Byte] = {
		data match {
			case ref: AnyRef =>
				val identifier: Try[Array[Byte]] = SerializationExtension(system).serialize(ref)
				if (identifier.isSuccess) {
					identifier.get
				} else {
					Array[Byte]()
				}
			case _ =>
				val os: ByteArrayOutputStream = new ByteArrayOutputStream()
				val oos: ObjectOutputStream = new ObjectOutputStream(os)
				oos.writeObject(data)
				oos.flush()
				oos.close()
				os.toByteArray
		}
	}

	def deserialize[T: ClassTag](bytes: Array[Byte])(implicit system: ActorSystem): T = {
		import scala.reflect._
		val clazz: Class[T] = classTag[T].runtimeClass.asInstanceOf[Class[T]]
		clazz match {
			case ref: AnyRef =>
				val triedRef: Try[T] = SerializationExtension(system).deserialize(bytes, clazz)
				if (triedRef.isSuccess) {
					triedRef.get
				}
				else {
					null.asInstanceOf[T]
				}
			case _ => //TODO: how to adjust anyVal
				val fis: ByteArrayInputStream = new ByteArrayInputStream(bytes)
				val ois: ObjectInputStream = new ObjectInputStream(fis)
				val obj: T = ois.readObject().asInstanceOf[T]
				obj
		}
	}

	def actorRef2SocketString(actorRef: ActorRef): String = {
		val address = actorRef.path.address
		s"${address.host.get}:${address.port.get}"
	}

	def getSystemProperties: Map[String, String] = {
		System.getProperties.stringPropertyNames().asScala
			.map(key => (key, System.getProperty(key))).toMap
	}

	def getLocalHostName: String = {
		InetAddress.getLocalHost.getHostName
	}

	def getIpByName(host: String): String = {
		InetAddress.getByName(host).getHostAddress
	}


	def getPropertiesFromFile(filename: String): Map[String, String] = {
		val file = new File(filename)
		require(file.exists(), s"Properties file $file does not exist")
		require(file.isFile(), s"Properties file $file is not a normal file")

		val inReader: InputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
		try {
			val properties = new Properties()
			properties.load(inReader)
			properties.stringPropertyNames().asScala.map(
				k => (k, properties.getProperty(k).trim)
			).toMap
		} catch {
			case e: IOException =>
				throw new Exception(s"Failed when loading moonbox properties from $filename", e)
		} finally {
			inReader.close()
		}
	}

	def getDefaultPropertiesFile(env: Map[String, String] = sys.env): String = {

		val confDir: Option[String] = env.get("MOONBOX_CONF_DIR").orElse(env.get("MOONBOX_HOME").map {t => s"$t${File.separator}conf"})
		if(confDir.isEmpty) {
			//TODO log can not find conf dir
			null
		} else {
			val confFile: Option[String] = confDir.map { t => new File(s"$t${File.separator}moonbox-defaults.conf") }
				.filter(_.isFile)
				.map(_.getAbsolutePath)
			if(confFile.isEmpty) {
				// TODO log can not find conf file moonbox-defaults.conf
				null
			} else confFile.get
		}
	}

	def classForName(className: String): Class[_] = {
		Class.forName(className)
	}

}
