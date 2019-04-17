package moonbox.rest

import org.apache.commons.codec.Charsets
import org.json.JSONObject

import scala.annotation.tailrec
import scala.io.Source
import scala.collection.JavaConverters._

object MoonboxRest {
	private val SUBMIT_PATH = "/batch/submit"
	private val PROGRESS_PATH = "/batch/progress"

	private var user: String = _
	private var password: String = _
	private var language: String = "mql"
	private var database: Option[String] = _
	private var path: String = _
	private var server: String = _

	def main(args: Array[String]) {
		parse(args.toList)
		if (!checkArgs()) {
			printUsageAndExit(-1)
		}
		val url = if (server.startsWith("http://")) {
			server
		} else {
			"http://" + server
		}

		val jobId = submit(url + SUBMIT_PATH)
		println("batchId: " + jobId)

		loopProgress(url + PROGRESS_PATH, jobId, 1 * 60 * 1000)
	}

	private def submit(url: String): String = {
		val jsonObject = new JSONObject()
			.put("username", user)
			.put("password", password)
			.put("sqls", readSqls().asJava)
			.put("lang", language)
			.put("config", Map[String, String]().asJava)

		val response = HttpClient.doPost(url, jsonObject.toString, Charsets.UTF_8.name())
		new JSONObject(response).getString("jobId")
	}

	private def progress(url: String, jobId: String): (String, String) = {
		val jsonObject = new JSONObject()
			.put("username", user)
			.put("password", password)
			.put("jobId", jobId)

		println(jsonObject.toString())
		val response = HttpClient.doPost(url, jsonObject.toString, Charsets.UTF_8.name())
		val responseObject = new JSONObject(response)
		(responseObject.getString("state"), responseObject.getString("message"))
	}

	private def loopProgress(url: String, jobId: String, interval: Long): Unit = {
		val SUCCESS = "FINISHED"
		val FAILED = Seq("UNKNOWN", "KILLED", "FAILED", "ERROR")
		while (true) {
			val (state, message) = progress(url, jobId)
			if (state == SUCCESS) {
				System.exit(0)
			} else if (FAILED.contains(state)) {
				System.out.println("error message: " + message)
				System.exit(-1)
			} else {
				Thread.sleep(interval)
			}
		}
	}

	private def readSqls(): Seq[String] = {
		val source = Source.fromFile(path)
		val sqls = source.getLines().mkString(" ").split(";").filterNot(s => s == "" || s == null)
		println(sqls)
		source.close()
		sqls
	}

	private def checkArgs(): Boolean = {
		if (user == null || password == null || server == null || path == null) {
			false
		} else {
			true
		}
	}

	@tailrec
	private def parse(args: List[String]): Unit = args match {
		case u :: tail if u.startsWith("-u") =>
			user = u.stripPrefix("-u")
			parse(tail)
		case s :: tail if s.startsWith("-s") =>
			server = s.stripPrefix("-s")
			parse(tail)
		case p :: tail if p.startsWith("-p") =>
			password = p.stripPrefix("-p")
			parse(tail)
		case l :: tail if l.startsWith("-l") =>
			language = l.stripPrefix("-l")
			parse(tail)
		case d :: tail if d.startsWith("-d") =>
			database = Some(d.stripPrefix("-d"))
			parse(tail)
		case f :: tail if f.startsWith("-f") =>
			path = f.stripPrefix("-f")
			parse(tail)
		case Nil =>
		case _ =>
			printUsageAndExit(1)
	}

	private def printUsageAndExit(exitCode: Int): Unit = {
		// scalastyle: off println
		System.err.println(
			"Usage: moonbox [options]\n" +
				"options:\n" +
				"   -s            Connect to host:port.\n" +
				"   -u            User for login.\n" +
				"   -p            Password to use when connecting to server.\n" +
				"   -l            Mql or hql to execute.\n" +
				"   -d            Current database, optional.\n" +
				"   -f            Mql or hql script file path.\n"
		)
		System.exit(exitCode)
	}
}
