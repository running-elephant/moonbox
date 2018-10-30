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

package moonbox.common.util

import java.io.{ByteArrayInputStream, ObjectInputStream, ObjectOutputStream, _}
import java.lang.reflect.Field
import java.net.InetAddress
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


	def getRuntimeJars(env: Map[String, String] = sys.env): List[String] = {
		if ( env.contains("MOONBOX_DEBUG_LOCAL") ) {  //debug local
			val projectPath = System.getProperty("user.dir")
			val srcDirPath = s"$projectPath" + File.separator + "assembly" +  File.separator +"target"
			val srcDirFile = new File(srcDirPath)
			val jarFileOpt = srcDirFile.listFiles().filter(_.isFile).filter(_.getName.startsWith("moonbox-assembly")).map(_.getAbsolutePath).headOption
			if(jarFileOpt.isDefined) {
				val jarFile = s"${jarFileOpt.get}"
				val destDirPath = s"$srcDirPath" + File.separator + "moonbox" + File.separator + "runtime"
				val destDirFile = new File(destDirPath)
				FileUtils.deleteDirectory(destDirFile)

				val cmd = Array("tar", "-C", srcDirPath,  "-xvf", jarFile, "moonbox" + File.separator + "runtime")
				val pro = Runtime.getRuntime.exec(cmd)
				pro.waitFor()
				val a = destDirFile.listFiles().map {
					_.getAbsolutePath
				}.filter(_.endsWith(".jar")).toList
				a
			}else{
				throw new Exception("assembly jar does not exist")
			}
		} else {
			val pluginDir: Option[String] = env.get("MOONBOX_CONF_DIR").orElse(env.get("MOONBOX_HOME").map { t => s"$t${File.separator}runtime" })
			if (pluginDir.isEmpty) {
				//TODO: local load dep jars
				throw new Exception("$MOONBOX_HOME does not exist")
			} else {
				val lib = new File(pluginDir.get)
				if (lib.exists()) {
					val confFile = lib.listFiles().filter {
						_.isFile
					}.map(_.getAbsolutePath)
					confFile.toList
				} else {
					List()
				}
			}
		}
	}

	def getYarnAppJar(env: Map[String, String] = sys.env): Option[String] = {
		if ( env.contains("MOONBOX_DEBUG_LOCAL" ) ) {
			val projectPath = System.getProperty("user.dir")
			val depLocalDir = s"$projectPath" + File.separator + "yarnapp" +  File.separator +"target"
			val file = new File(depLocalDir)
			val jarFile = file.listFiles().filter(_.isFile).filter(_.getName.startsWith("moonbox-")).map(_.getAbsolutePath).headOption
			jarFile
		} else {
			val pluginDir: Option[String] = env.get("MOONBOX_CONF_DIR").orElse(env.get("MOONBOX_HOME").map { t => s"$t${File.separator}cluster" })
			if (pluginDir.isEmpty) {
				throw new Exception("$MOONBOX_HOME does not exist")
			} else {
				val lib = new File(pluginDir.get)
				if (lib.exists()) {
					lib.listFiles().filter{_.isFile}.filter(_.getName.toLowerCase.indexOf("yarnapp") != -1).map(_.getAbsolutePath).headOption
				} else {
					None
				}
			}
		}
	}


	def updateYarnAppInfo2File(id: String, cfg: Option[String], addOrDel: Boolean, env: Map[String, String] = sys.env): Unit = {
		val appIdFile = if ( env.contains("MOONBOX_DEBUG_LOCAL" ) ) {
			val projectPath = System.getProperty("user.dir")
			val depLocalDir = s"$projectPath" + File.separator + "yarnapp" +  File.separator +"target"
			new File(depLocalDir + File.separator  + "appid" )
		} else {
			val pluginDir: Option[String] = env.get("MOONBOX_CONF_DIR").orElse(env.get("MOONBOX_HOME").map { t => s"$t${File.separator}cluster" })
			new File(pluginDir.get +  File.separator  + "appid")
		}

		val lines: Seq[String] = if(appIdFile.exists()) {
			val bufferedSource = Source.fromFile(appIdFile)
			val iter = bufferedSource.getLines()
			val idAndConfig = ArrayBuffer.empty[String]
			while(iter.hasNext) {
				idAndConfig.append(iter.next())
			}

			bufferedSource.close()
			idAndConfig
		} else { Seq.empty[String] }

		if (addOrDel) { //add
			val prevLines = lines.filter(_.indexOf(id) != -1) //find id
			if ( prevLines.isEmpty ) {
				var fw: FileWriter = null
				try {
					fw = new FileWriter(appIdFile, true)
					fw.write(id + " | " + cfg.getOrElse("") + "\n")
				}finally {
					if (fw != null) { fw.close() }
				}
			}
		} else {  //delete
			val newlines = lines.filter(_.indexOf(id) == -1)
			var fw: FileWriter = null
			try {
				fw = new FileWriter(appIdFile, true)
				newlines.foreach{ line => fw.write(line)}
			} finally {
				if (fw != null) { fw.close() }
			}
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

	def checkHost(host: String, message: String): Unit = {
		assert(host.indexOf(':') == -1, message)
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

	// copy from spark
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
