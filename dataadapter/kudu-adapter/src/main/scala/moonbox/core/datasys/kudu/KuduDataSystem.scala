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

package moonbox.core.datasys.kudu

import moonbox.core.datasys.{DataSystem, DataSystemRegister}
import org.apache.kudu.client.KuduClient
import org.apache.kudu.client.KuduClient.KuduClientBuilder

import scala.collection.JavaConversions._

class KuduDataSystem(props: Map[String, String]) extends DataSystem(props) {
	require(contains("kudu.master"))

	private def getClient: KuduClient = {
		new KuduClientBuilder(props("kudu.master")).build()
	}

	override def tableNames(): Seq[String] = {
		val kuduClient = getClient
		val tables = kuduClient.getTablesList.getTablesList
		kuduClient.close()
		tables
	}

	override def tableName(): String = {
		props("kudu.table")
	}

	override def tableProperties(tableName: String): Map[String, String] = {
		props.+("kudu.table" -> tableName)
	}

	override def test(): Boolean = {
		var client: KuduClient = null
		try {
			client = getClient
			if (client != null) {
				true
			} else {
				false
			}
		} catch {
			case e: Exception =>
				false
		} finally {
			if (client != null) {
				client.close()
			}
		}

	}
}
