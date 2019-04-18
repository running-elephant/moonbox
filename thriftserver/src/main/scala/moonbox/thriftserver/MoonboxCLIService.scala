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

import java.util.{List => JList}

import moonbox.common.MbLogging
import moonbox.thriftserver.ReflectionUtils._
import org.apache.hadoop.hive.conf.HiveConf
import org.apache.hive.service.cli._

import scala.collection.mutable

class MoonboxCLIService(server: MoonboxThriftServer, serverConf: mutable.Map[String, String]) extends CLIService(null) with ReflectedCompositeService with MbLogging {

  val SPARK_CONTEXT_VERSION = "2.2.0"
  private var moonboxSessionManager: MoonboxSessionManager = _

  override def init(hiveConf: HiveConf) {
    logInfo("MoonboxCLIService initializing ...")
    setSuperField(this, "hiveConf", hiveConf)

    moonboxSessionManager = new MoonboxSessionManager(server.getHiveConf(), serverConf)
    setSuperField(this, "sessionManager", moonboxSessionManager)
    addService(moonboxSessionManager)
    initCompositeService(hiveConf)
  }

  override def getInfo(sessionHandle: SessionHandle, getInfoType: GetInfoType): GetInfoValue = {
    getInfoType match {
      case GetInfoType.CLI_SERVER_NAME => new GetInfoValue("Spark SQL")
      case GetInfoType.CLI_DBMS_NAME => new GetInfoValue("Spark SQL")
      case GetInfoType.CLI_DBMS_VER => new GetInfoValue(SPARK_CONTEXT_VERSION)
      case _ => super.getInfo(sessionHandle, getInfoType)
    }
  }

}

