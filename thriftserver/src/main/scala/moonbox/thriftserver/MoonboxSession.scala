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

import java.util

import moonbox.thriftserver.ReflectionUtils._
import org.apache.hadoop.hive.conf.HiveConf
import org.apache.hadoop.hive.ql.session.SessionState
import org.apache.hive.service.cli.session.HiveSessionImpl
import org.apache.hive.service.cli.thrift.TProtocolVersion
class MoonboxSession(protocol: TProtocolVersion,
                     username: String,
                     password: String,
                     hiveConf: HiveConf,
                     ipAddress: String
                     ) extends HiveSessionImpl(protocol, username, password, hiveConf,ipAddress) {

  override def open(sessionConfMap: util.Map[String, String]) = {
    val sessionState = new SessionState(hiveConf, username)
    sessionState.setUserIpAddress(ipAddress)
    sessionState.setIsHiveServerQuery(true)
    val lastAccessTime: Long = System.currentTimeMillis
    setSuperField(this, "lastIdleTime", lastAccessTime.asInstanceOf[AnyRef])
    setSuperField(this, "sessionState", sessionState)
  }
}
