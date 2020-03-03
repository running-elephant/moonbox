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

package moonbox.grid.deploy


import moonbox.grid.deploy.ConnectionType.ConnectionType

object ConnectionType extends Enumeration {
    type ConnectionType = Value

    val JDBC, CLIENT, REST, ODBC , SYSTEM = Value
}

case class ConnectionInfo(localAddress: String, remoteAddress: String, connectionType: ConnectionType) {
	override def toString: String = {
		s"$localAddress $remoteAddress $connectionType"
	}
}
