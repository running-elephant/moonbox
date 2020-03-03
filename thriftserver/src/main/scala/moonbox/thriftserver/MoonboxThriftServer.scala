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

package moonbox.thriftserver

import java.io.File
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

import moonbox.common.MbLogging
import org.apache.hadoop.hive.conf.HiveConf
import org.apache.hadoop.hive.conf.HiveConf.ConfVars
import org.apache.hive.service.CompositeService
import org.apache.hive.service.cli.thrift.{ThriftBinaryCLIService, ThriftHttpCLIService}

import scala.annotation.tailrec
import scala.collection.{immutable, mutable}

private[thriftserver] class MoonboxThriftServer(serverConf: mutable.Map[String, String]) extends CompositeService("MoonboxThriftServer") with MbLogging {

  private val started = new AtomicBoolean(false)
  private var bindPort: Int = _
  private val hiveConf: HiveConf = initHiveConfig(serverConf)

  override def init(hiveConf: HiveConf) {
    val moonboxSQLCLIService = new MoonboxCLIService(this, serverConf)
    addService(moonboxSQLCLIService)
    val thriftCliService = if (isHTTPTransportMode(hiveConf)) {
      new ThriftHttpCLIService(moonboxSQLCLIService)
    } else {
      new ThriftBinaryCLIService(moonboxSQLCLIService)
    }
    addService(thriftCliService)
    super.init(hiveConf)
    bindPort = thriftCliService.getPortNumber
  }

  private def isHTTPTransportMode(hiveConf: HiveConf): Boolean = {
    val transportMode = hiveConf.getVar(ConfVars.HIVE_SERVER2_TRANSPORT_MODE)
    transportMode.toLowerCase(Locale.ROOT).equals("http")
  }

  def getServerPort: Int = bindPort

  /* return bind port */
  override def start() = {
    init(hiveConf)
    super.start()
    started.getAndSet(true)
    val host = serverConf.getOrElse(THRIFT_SERVER_HOST_KEY, "localhost")
    logInfo(s"Moonbox thrift server started at $host:$getServerPort, ")
  }

  override def stop(): Unit = {
    if (started.getAndSet(false)) {
      super.stop()
    }
  }
  override def getHiveConf() = hiveConf

  private def initHiveConfig(serverConf: mutable.Map[String, String]): HiveConf = {

    val supportedMode: immutable.Seq[String] = /*"binary" ::*/ "http" :: Nil
    val mode = serverConf.getOrElse(THRIFT_SERVER_MODE_KEY, "http")
    assert(supportedMode.contains(mode))

    val fileSeparator: String = File.separator
    val scratchDir = Option(System.getenv("MOONBOX_HOME")) match {
      case Some(dir) => dir + fileSeparator + "tmp" + fileSeparator + "thriftserver"
      case None => new File(System.getProperty("user.dir")).getAbsoluteFile.getParent + fileSeparator + "tmp" + fileSeparator + "thriftserver"
    }
    val hiveConf = new HiveConf()
    hiveConf.setVar(ConfVars.SCRATCHDIR, scratchDir)
    //    hiveConf.setBoolVar(ConfVars.HIVE_START_CLEANUP_SCRATCHDIR, true)
    hiveConf.setVar(ConfVars.METASTORECONNECTURLKEY, s"jdbc:derby:$scratchDir;databaseName=metastore_db;create=true")
    hiveConf.setVar(ConfVars.HIVE_SERVER2_TRANSPORT_MODE, mode)
    hiveConf.setVar(ConfVars.HIVE_SERVER2_AUTHENTICATION, "NONE")
    hiveConf.setBoolVar(ConfVars.HIVE_SERVER2_THRIFT_HTTP_COOKIE_AUTH_ENABLED, false)
    hiveConf.setVar(ConfVars.HIVE_SERVER2_THRIFT_BIND_HOST, serverConf.getOrElse(THRIFT_SERVER_HOST_KEY, ""))
    hiveConf.setIntVar(ConfVars.HIVE_SERVER2_THRIFT_HTTP_PORT, serverConf.get(THRIFT_SERVER_PORT_KEY).map(_.toInt).getOrElse(10001))
    hiveConf.setVar(ConfVars.HIVE_SERVER2_THRIFT_HTTP_PATH, THRIFT_SERVER_HTTP_PATH)
    hiveConf.setIntVar(ConfVars.HIVE_SERVER2_THRIFT_MAX_MESSAGE_SIZE, 1024 * 1024 * 1024)
    hiveConf
  }

}

object MoonboxThriftServer extends MbLogging {
  private val serverConf: mutable.Map[String, String] = mutable.Map.empty

  def main(args: Array[String]): Unit = {
    try{
      parse(args.toList)
      logInfo("Starting moonbox thrift server ...")
      new MoonboxThriftServer(serverConf).start()
    } catch {
      case e: Exception =>
        logError("Start moonbox thrift server failed: " + e.getMessage)
        System.exit(1)
    }
  }

  @tailrec
  private def parse(args: List[String]): Unit = args.map(_.toLowerCase(Locale.ROOT)) match {
    case ("-h" | "--host") :: value :: tail =>
      serverConf.put(THRIFT_SERVER_HOST_KEY, value)
      parse(tail)
    case ("-p" | "--port") :: value :: tail =>
      serverConf.put(THRIFT_SERVER_PORT_KEY, value)
      parse(tail)
    case ("--mbhost") :: value :: tail =>
      serverConf.put(MOONBOX_SERVER_HOST_KEY, value)
      parse(tail)
    case ("--mbport") :: value :: tail =>
      serverConf.put(MOONBOX_SERVER_PORT_KEY, value)
      parse(tail)
    case ("-m" | "--mode") :: value :: tail =>
      serverConf.put(THRIFT_SERVER_MODE_KEY, value)
      parse(tail)
    case Nil =>
    case _ => throw new Exception("Invalid arguments.")
  }
}
