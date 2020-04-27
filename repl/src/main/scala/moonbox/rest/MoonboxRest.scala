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
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object MoonboxRest {
  private val SUBMIT_PATH = "/batch/submit"
  private val PROGRESS_PATH = "/batch/progress"
  private val CANCEL_PATH = "/batch/cancel"

  private val sqlSplitter = ';'

  private var user: String = _
  private var password: String = _
  private var language: String = "mql"
  private var database: Option[String] = _
  private var path: String = _
  private var ql: String = null
  private var server: String = _
  private var name: Option[String] = None
  private var isPool: Boolean = false
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

    //todo job pool progress
    if (isPool) {
      submitJobPool(url + SUBMIT_PATH)
    } else {
      val jobId = submitJob(url + SUBMIT_PATH)
      Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
        override def run(): Unit = {
          if (!stopped) {
            cancel(url + CANCEL_PATH, jobId)
          }
        }
      }))
      loopProgress(url + PROGRESS_PATH, jobId, 1 * 30 * 1000)
    }
  }

  private def submitJob(url: String): String = {
    name.foreach {
      n =>
        config.put("name", n)
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

  private def submitJobPool(url: String): Unit = {
    name.foreach {
      n =>
        config.put("name", n)
        println(s"submit job pool, job name prefix is $n")
    }
    val jsonObject = new JSONObject()
      .put("username", user)
      .put("password", password)
      .put("sqls", readSqls().asJava)
      .put("lang", language)
      .put("config", config.toMap.asJava)
      .put("isPool", isPool)

    val parameter = jsonObject.toString
    println(parameter)
    val response = HttpClient.doPost(url, parameter, Charsets.UTF_8.name())
    try {
      val jobIds = new JSONObject(response).getJSONArray("jobIds").asScala
      val message =
        s"""
           |batch job pool submitted success, parameters is $parameter,
           |jobId sequence size is ${jobIds.size},
           |jobId sequence: ${jobIds.mkString(", ")}
         """.stripMargin
      println(message)
    }
    catch {
      case e: Exception =>
        println(s"batch job pool submit failed, error message is ${e.getMessage}")
        System.exit(-1)
    }
  }

  private def progress(url: String, jobId: String): (String, String, String) = {
    val jsonObject = new JSONObject()
      .put("username", user)
      .put("password", password)
      .put("jobId", jobId)
    val response = HttpClient.doPost(url, jsonObject.toString, Charsets.UTF_8.name())
    val responseObject = new JSONObject(response)
    if (responseObject.has("appId"))
      (responseObject.getString("appId"), responseObject.getString("state"), responseObject.getString("message"))
    else
      (null, responseObject.getString("state"), responseObject.getString("message"))
  }

  private def loopProgress(url: String, jobId: String, interval: Long): Unit = {
    val SUCCESS = "FINISHED"
    val FAILED = Seq("UNKNOWN", "KILLED", "FAILED", "ERROR")
    var appIdInitial: String = null
    while (true) {
      try {
        val (appId, state, message) = progress(url, jobId)
        if (appId != null && appIdInitial == null) {
          appIdInitial = appId
          println("YarnApplicationId: " + appIdInitial)
        }
        println(s"State: $state")
        if (state == SUCCESS) {
          stopped = true
          System.exit(0)
        } else if (FAILED.contains(state)) {
          println("ERROR MESSAGE: " + message)
          stopped = true
          System.exit(-1)
        } else {
          Thread.sleep(interval)
        }
      } catch {
        case e: Exception =>
          println("Request for job state failed, ", e)
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
      splitSql(ql, sqlSplitter)
    } else {
      val source = Source.fromFile(path)
      val sqls = splitSql(source.mkString, sqlSplitter).filterNot(s => s == "" || s == null)
      source.close()
      sqls
    }
  }

  private def splitSql(sql: String, splitter: Char): Seq[String] = {
    val splitIndex = new ArrayBuffer[Int]()
    var doubleQuoteCount = 0
    var singleQuoteCount = 0
    var parenthesisCount = 0
    for ((char, idx) <- sql.toCharArray.zipWithIndex) {
      if (char == splitter) {
        if (parenthesisCount == 0 && doubleQuoteCount % 2 == 0 && singleQuoteCount % 2 == 0) splitIndex += idx
      }
      if (char == ''') singleQuoteCount += 1
      if (char == '"') doubleQuoteCount += 1
      if (char == '(') {
        if (singleQuoteCount % 2 == 0 && doubleQuoteCount % 2 == 0)
          parenthesisCount += 1
      }
      if (char == ')') {
        if (singleQuoteCount % 2 == 0 && doubleQuoteCount % 2 == 0)
          parenthesisCount -= 1
      }
    }
    splits(sql, splitIndex.toArray, 0).map(_.stripPrefix(splitter.toString).trim).filter(_.length > 0)
  }

  private def splits(sql: String, idxs: scala.Array[Int], offset: Int): Seq[String] = {
    if (idxs.nonEmpty) {
      val head = idxs.head
      val (h, t) = sql.splitAt(head - offset)
      h +: splits(t, idxs.tail, head)
    } else sql :: Nil
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
    case pool :: tail if pool.startsWith("-isPool") =>
      isPool = pool.stripPrefix("-isPool").toBoolean
      parse(tail)
    case c :: tail if c.startsWith("-C") =>
      config ++= parseSparkConfig(c.stripPrefix("-C"))
    case Nil =>
    case _ =>
      printUsageAndExit(1)
  }


  private def parseSparkConfig(config: String): Map[String, String] = {
    val map = mutable.HashMap.empty[String, String]

    val splitArray = config.split(",")

    assert(splitArray.nonEmpty, "please check config format.")

    var kv: Array[String] = null
    var key: String = null
    var value: StringBuilder = new StringBuilder

    for (element <- splitArray) {
      if (element.contains("=")) {
        if (key != null) {
          map.put(key, value.toString)
          key = null
          value.clear()

          kv = element.split("=")
          key = kv(0)
          value ++= element.stripPrefix(s"$key=")
        } else {
          kv = element.split("=")
          key = kv(0)
          value ++= element.stripPrefix(s"$key=")
        }
      } else {
        value ++= s",$element"
      }
    }

    if (key != null) {
      map.put(key, value.toString())
    }

    map.toMap
  }

  private def printUsageAndExit(exitCode: Int): Unit = {
    // scalastyle: off println
    System.err.println(
      "Usage: moonbox-submit [options]\n" +
        "options:\n" +
        "   -s            Connect to host:port.\n" +
        "   -u            User for login.\n" +
        "   -p            Password to use when connecting to server.\n" +
        "   -n            Job Name.\n" +
        "   -isPool       Submit job pool.\n" +
        "   -l            Mql or hql to execute.\n" +
        "   -d            Current database, optional.\n" +
        "   -f            MQL or HQL script file path.\n" +
        "   -e 			      MQL with double quotes.\n" +
        "   -C            Execution configuration, format as key=value,key1=value1...\n"
    )
    System.exit(exitCode)
  }
}
