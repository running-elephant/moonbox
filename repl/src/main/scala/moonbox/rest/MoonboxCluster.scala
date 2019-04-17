package moonbox.rest

import java.io.File

import com.typesafe.config.ConfigFactory
import org.apache.commons.codec.Charsets
import org.json.JSONObject

import scala.collection.JavaConverters._

object MoonboxCluster {
	def main(args: Array[String]) {
		val home = sys.env.getOrElse("MOONBOX_HOME",
			throw new Exception("MOONBOX_HOME not found in env"))
		val host = sys.env.getOrElse("MOONBOX_MASTER_HOST",
			throw new Exception("MOONBOX_MASTER_HOST not found in env."))

		val configFilePath = s"$home${File.separator}conf${File.separator}moonbox-defaults.conf"
		val file = new File(configFilePath)
		if (!file.exists()) {
			println("moonbox-defaults.conf does not exist.")
			System.exit(-1)
		}

		val config =  ConfigFactory.parseFile(file)

		val restPort = if (config.hasPath("moonbox.deploy.rest.port")) {
			config.getInt("moonbox.deploy.rest.port")
		} else 9090
		val url = s"http://$host:$restPort/management"
		var path: String = ""
		var key: String = ""

		args.headOption match {
			case Some(arg) =>
				if (arg.equalsIgnoreCase("apps")) {
					path = url + "/apps-info"
					key = "apps"
				} else if (arg.equalsIgnoreCase("workers")) {
					path = url + "/cluster-info"
					key = "cluster"
				} else {
					printUsageAndExit(-1)
				}
			case None =>
				printUsageAndExit(-1)
		}

		val response: String = HttpClient.doGet(path, Charsets.UTF_8.name())
		val string = new JSONObject(response).getJSONArray(key).asScala.mkString("\n")
		println(string)

	}

	private def printUsageAndExit(exitCode: Int): Unit = {
		// scalastyle: off println
		System.err.println(
			"Usage: moonbox-cluster.sh [options]\n" +
				"options:\n" +
				"   apps             list current running apps\n" +
				"   workers          list current running workers.\n"
		)
		System.exit(exitCode)
	}
}
