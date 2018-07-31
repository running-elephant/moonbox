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

package moonbox.grid.deploy.transport

import moonbox.common.MbConf
import moonbox.grid.deploy.MbService
import moonbox.grid.deploy.transport.server.JdbcServer

class TransportServer(host: String, port: Int, conf: MbConf, service: MbService) {
  val jdbcServer: JdbcServer = new JdbcServer(host, port, conf, service)
  def start(): Int = {
    jdbcServer.start()
  }

  def stop(): Unit = {
    jdbcServer.stop()
  }
}
