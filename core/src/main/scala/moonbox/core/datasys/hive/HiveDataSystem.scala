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

package moonbox.core.datasys.hive

import moonbox.common.MbLogging
import moonbox.core.datasys.DataSystem
import org.apache.spark.sql.hive.HiveClientUtils

class HiveDataSystem(props: Map[String, String])
	extends DataSystem(props) with MbLogging {

	if (props.contains("metastore.uris")) {
		checkOptions("metastore.uris", "hivedb")
	} else {
		checkOptions("metastore.url", "metastore.driver", "metastore.user",
			"metastore.password", "hivedb")
	}


	override def tableNames(): Seq[String] = {
		val client = HiveClientUtils.getHiveClient(props)
		client.listTables(props("hivedb"))
	}

	override def tableName(): String = {
		props("hivetable")
	}

	override def tableProperties(tableName: String): Map[String, String] = {
		props.+("hivetable" -> tableName)
	}

	override def test(): Unit = {
		try {
			HiveClientUtils.getHiveClient(props)
		} catch {
			case e: Exception =>
				logError("hive test failed.", e)
				throw e
		} finally {
			// we do not close hive client here
		}
	}
}
