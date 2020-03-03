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

package moonbox.grid.deploy.transport

import akka.actor.ActorRef
import moonbox.catalog.JdbcCatalog
import moonbox.common.{MbConf, MbLogging}
import moonbox.network.TransportContext

/**
	* moonbox tcp server, is responsible for jdbc client accessing request
	* @param host host moonbox tcp bound
	* @param port port moonbox tcp bound
	* @param conf moonbox conf
	* @param jdbcCatalog jdbcCatalog
	* @param master master actor ref
	*/
class TcpServer(host: String, port: Int, conf: MbConf, jdbcCatalog: JdbcCatalog, master: ActorRef) extends MbLogging {

	private val context = new TransportContext(new TcpServerHandler(conf, jdbcCatalog, master), true)
	private val server = context.createServer(host, port)

	def start(): Int = {
		val portBind = server.start()
		logInfo(s"TcpServer is listening on $host:${portBind}")
		portBind
	}

	def stop(): Unit = {
		server.close()
	}

}
