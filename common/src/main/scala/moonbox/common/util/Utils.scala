/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

package moonbox.common.util

import java.io.{ByteArrayInputStream, ObjectInputStream, ObjectOutputStream, _}
import java.lang.reflect.Field
import java.net.{Inet4Address, InetAddress, NetworkInterface}
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern
import java.util.{Collections, Date, Properties, Map => JMap}

import com.typesafe.config.{Config, ConfigFactory}
import moonbox.common.MbLogging
import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object Utils extends MbLogging {

	def classForName(className: String): Class[_] = {
		Class.forName(className)
	}

	def getEnv = System.getenv()

	def getEnv(key: String): String = getEnv(key)

	def setEnv(key: String, value: String): Unit = {
		setEnv(Map(key -> value))
	}

	def setEnv(newEnv: Map[String, String]): Unit = {
		setEnv(newEnv.asJava)
	}

	def setEnv(newEnv: JMap[String, String]): Unit = {
		try {
			val processEnvironmentClass = classForName("java.lang.ProcessEnvironment")
			val theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment")
			theEnvironmentField.setAccessible(true)
			val env = theEnvironmentField.get(null).asInstanceOf[JMap[String, String]]
			env.putAll(newEnv)

			val theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment")
			theCaseInsensitiveEnvironmentField.setAccessible(true)
			val ciEnv = theCaseInsensitiveEnvironmentField.get(null).asInstanceOf[JMap[String, String]]
			ciEnv.putAll(newEnv)
		} catch {
			case e: NoSuchFieldException =>
				val classes: Array[java.lang.Class[_]] = classOf[Collections].getDeclaredClasses
				val env: JMap[String, String] = System.getenv()
				for (cl <- classes; if "java.util.Collections$UnmodifiableMap".equals(cl.getName)) {
					val field: Field = cl.getDeclaredField("m")
					field.setAccessible(true)
					val map: JMap[String, String] = field.get(env).asInstanceOf[JMap[String, String]]
					map.clear()
					map.putAll(newEnv)
				}
		}

	}

	def serialize[T](o: T): Array[Byte] = {
		val bos = new ByteArrayOutputStream()
		val oos = new ObjectOutputStream(bos)
		oos.writeObject(o)
		oos.close()
		bos.toByteArray
	}

	def deserialize[T](bytes: Array[Byte]): T = {
		val bis = new ByteArrayInputStream(bytes)
		val ois = new ObjectInputStream(bis)
		ois.readObject.asInstanceOf[T]
	}

	def getFormattedClassName(obj: AnyRef): String = {
		obj.getClass.getSimpleName.replace("$", "")
	}

	def getSystemProperties: Map[String, String] = {
		System.getProperties.stringPropertyNames().asScala
			.map(key => (key, System.getProperty(key))).toMap
	}

	def getMoonboxHome: String = {
		sys.env.getOrElse[String]("MOONBOX_HOME",
			throw new Exception("MOONBOX_HOME does not config."))
	}

	def getMoonboxHomeOption: Option[String] = {
		sys.env.get("MOONBOX_HOME")
	}

	def getDefaultLogConfig(env: Map[String, String] = sys.env): Option[String] = {
		val configDir: Option[String] = env.get("MOONBOX_CONF_DIR").orElse(env.get("MOONBOX_HOME")
			.map {t => s"$t${File.separator}conf"})
		if (configDir.isEmpty) {
			System.err.println("conf directory doesn't exist.")
		}
		val configFile = configDir.map (t => new File(s"$t${File.separator}log4j.properties"))
			.filter(_.isFile).map(_.getAbsolutePath)
		if(configFile.isEmpty) {
			System.err.println("log4j.properties doesn't exist.")
		}
		configFile
	}

	def getDefaultPropertiesFile(env: Map[String, String] = sys.env): Option[String] = {

		val configDir: Option[String] = env.get("MOONBOX_CONF_DIR").orElse(env.get("MOONBOX_HOME")
			.map {t => s"$t${File.separator}conf"})
		if (configDir.isEmpty) {
			logWarning("conf directory doesn't exist.")
		}
		val configFile = configDir.map (t => new File(s"$t${File.separator}moonbox-defaults.conf"))
			.filter(_.isFile).map(_.getAbsolutePath)
		if(configFile.isEmpty) {
			logWarning("moonbox-defaults.conf doesn't exist.")
		}
		configFile
	}

	def typesafeConfig2Map(config: Config): Map[String, String] = {
		val map: Map[String, String] = config.entrySet().asScala.map({ entry =>
			entry.getKey -> entry.getValue.unwrapped().toString
		})(collection.breakOut)
		map
	}

	def getConfigFromFile(filename: String): Config = {
		val file = new File(filename)
		require(file.exists, s"Properties file $file does not exist")
		require(file.isFile, s"Properties file $file is not a normal file")
		val config: Config = ConfigFactory.parseFile(file)
		ConfigFactory.load(config)
	}

	def getPropertiesFromFile(filename: String): Map[String, String] = {
		val file = new File(filename)
		require(file.exists, s"Properties file $file does not exist")
		require(file.isFile, s"Properties file $file is not a normal file")

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


	def delete (file: File) {
		if (file == null) {
			return
		} else if (file.isDirectory) {
			val files: Array[File] = file.listFiles
			if (files != null) {
				for (f <- files) delete(f)
			}
			file.delete
		} else {
			file.delete
		}
	}

	private val customHostname: Option[String] = sys.env.get("MOONBOX_LOCAL_HOSTNAME")

	private lazy val localIpAddress: InetAddress = findLocalInetAddress()

	private def findLocalInetAddress(): InetAddress = {
		val defaultIpOverride = System.getenv("MOONBOX_LOCAL_IP")
		if (defaultIpOverride != null) {
			InetAddress.getByName(defaultIpOverride)
		} else {
			val address = InetAddress.getLocalHost
			if (address.isLoopbackAddress) {
				// Address resolves to something like 127.0.1.1, which happens on Debian; try to find
				// a better address using the local network interfaces
				// getNetworkInterfaces returns ifs in reverse order compared to ifconfig output order
				// on unix-like system. On windows, it returns in index order.
				// It's more proper to pick ip address following system output order.
				val activeNetworkIFs = NetworkInterface.getNetworkInterfaces.asScala.toSeq
				val reOrderedNetworkIFs = activeNetworkIFs.reverse

				for (ni <- reOrderedNetworkIFs) {
					val addresses = ni.getInetAddresses.asScala
						.filterNot(addr => addr.isLinkLocalAddress || addr.isLoopbackAddress).toSeq
					if (addresses.nonEmpty) {
						val addr = addresses.find(_.isInstanceOf[Inet4Address]).getOrElse(addresses.head)
						// because of Inet6Address.toHostName may add interface at the end if it knows about it
						val strippedAddress = InetAddress.getByAddress(addr.getAddress)
						// We've found an address that looks reasonable!
						logWarning("Your hostname, " + InetAddress.getLocalHost.getHostName + " resolves to" +
							" a loopback address: " + address.getHostAddress + "; using " +
							strippedAddress.getHostAddress + " instead (on interface " + ni.getName + ")")
						logWarning("Set MOONBOX_LOCAL_IP if you need to bind to another address")
						return strippedAddress
					}
				}
				logWarning("Your hostname, " + InetAddress.getLocalHost.getHostName + " resolves to" +
					" a loopback address: " + address.getHostAddress + ", but we couldn't find any" +
					" external IP address!")
				logWarning("Set MOONBOX_LOCAL_IP if you need to bind to another address")
			}
			address
		}
	}

	def checkHost(host: String): Unit = {
		assert(host != null && host.indexOf(':') == -1, s"Expected hostname but get $host")
	}

	def checkPort(address: String): Unit = {
		assert(address != null && address.indexOf(":") != -1, s"Expected host:port but get $address")
	}

	def getIpByName(host: String): String = {
		InetAddress.getByName(host).getHostAddress
	}

	def localHostName(): String = {
		customHostname.getOrElse(localIpAddress.getHostAddress)
	}

	def now: Long = System.currentTimeMillis()

	def allEquals[T](data: Seq[T]): Boolean = data match {
		case Nil => true
		case head :: Nil => true
		case head :: tails => head == tails.head && allEquals(tails)
	}

	def formatDate(time: Long): String =  {
		val simpleFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		simpleFormat.format(new Date(time))
	}

	def formatDate(date: Date): String =  {
		val simpleFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		simpleFormat.format(date)
	}

	def escapeLikeRegex(pattern: String): String = {
		val in = pattern.toIterator
		val out = new StringBuilder()

		def fail(message: String) = throw new Exception(
			s"the pattern '$pattern' is invalid, $message")

		while (in.hasNext) {
			in.next match {
				case '\\' if in.hasNext =>
					val c = in.next
					c match {
						case '_' | '%' | '\\' => out ++= Pattern.quote(Character.toString(c))
						case _ => fail(s"the escape character is not allowed to precede '$c'")
					}
				case '\\' => fail("it is not allowed to end with the escape character")
				case '_' => out ++= "."
				case '%' => out ++= ".*"
				case c => out ++= Pattern.quote(Character.toString(c))
			}
		}
		"(?s)" + out.result() // (?s) enables dotall mode, causing "." to match new lines
	}

	def filterPattern(source: Seq[String], pattern: String): Seq[String] = {
		source.filter(name => pattern.r.pattern.matcher(name).matches())
	}

}
