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

package moonbox.rest

import org.apache.commons.codec.Charsets
import org.json.JSONObject

import scala.annotation.tailrec
import scala.io.Source
import scala.collection.JavaConverters._

object MoonboxRest {
	private val SUBMIT_PATH = "/batch/submit"
	private val PROGRESS_PATH = "/batch/progress"
	private val CANCEL_PATH = "/batch/cancel"

	private var user: String = _
	private var password: String = _
	private var language: String = "mql"
	private var database: Option[String] = _
	private var path: String = _
	private var ql: String = null
	private var server: String = _
	private var name: Option[String] = None
	private val config = new scala.collection.mutable.HashMap[String, String]

	private var stopped = false

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

		Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
			override def run(): Unit = {
				if (!stopped) {
					cancel(url + CANCEL_PATH, jobId)
				}
			}
		}))

		loopProgress(url + PROGRESS_PATH, jobId, 1 * 60 * 1000)
	}

	private def submit(url: String): String = {
		name.foreach {
			n => config.put("name", n)
			println(s"the job name is: $n")
		}
		val jsonObject = new JSONObject()
			.put("username", user)
			.put("password", password)
			.put("sqls", readSqls().asJava)
			.put("lang", language)
			.put("config", config.toMap.asJava)

		val parameter = jsonObject.toString
		val response = HttpClient.doPost(url, parameter, Charsets.UTF_8.name())
		var jobId: String = null
		try {
			jobId = new JSONObject(response).getString("jobId")
			println(s"batch job submitted as $jobId, parameters is $parameter")
		} catch {
			case e: Exception =>
				println(s"batch job submit failed, error message is ${e.getMessage}")
		}
		jobId
	}

	private def progress(url: String, jobId: String): (String, String) = {
		val jsonObject = new JSONObject()
			.put("username", user)
			.put("password", password)
			.put("jobId", jobId)
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
				stopped = true
				System.exit(0)
			} else if (FAILED.contains(state)) {
				println("error message: " + message)
				stopped = true
				System.exit(-1)
			} else {
				Thread.sleep(interval)
			}
		}
	}

	private def cancel(url: String, jobId: String): Unit = {
		if (jobId != null) {
			val jsonObject = new JSONObject()
				.put("username", user)
				.put("password", password)
				.put("jobId", jobId)
			HttpClient.doPost(url, jsonObject.toString(), Charsets.UTF_8.name())
			println(s"Job $jobId has been canceled.")
		}
	}

	private def readSqls(): Seq[String] = {
		if (path == null) {
			ql.split(";")
		} else {
			val source = Source.fromFile(path)
			val sqls = source.mkString.split(";").filterNot(s => s == "" || s == null)
			source.close()
			sqls
		}
	}

	private def checkArgs(): Boolean = {
		if (user == null || password == null || server == null || (path == null && ql == null)) {
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
		case e :: tail if e.startsWith("-e") =>
			ql = e.stripPrefix("-e")
			parse(tail)
		case n :: tail if n.startsWith("-n") =>
			name = Some(n.stripPrefix("-n"))
			parse(tail)
		case c :: tail if c.startsWith("-C") =>
			c.stripPrefix("-C").split(",").foreach { keyvalues =>
				val kv = keyvalues.trim.split("=")
				assert(kv.length >= 2, "please check config format.")
				config.put(kv(0).trim, kv(1).trim)
			}
		case Nil =>
		case _ =>
			printUsageAndExit(1)
	}

	private def printUsageAndExit(exitCode: Int): Unit = {
		// scalastyle: off println
		System.err.println(
			"Usage: moonbox-submit [options]\n" +
				"options:\n" +
				"   -s            Connect to host:port.\n" +
				"   -u            User for login.\n" +
				"   -p            Password to use when connecting to server.\n" +
				"   -l            Mql or hql to execute.\n" +
				"   -d            Current database, optional.\n" +
				"   -f            MQL or HQL script file path.\n" +
				"   -e 			  MQL with double quotes"
		)
		System.exit(exitCode)
	}
}
