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

package moonbox.core.datasys.cassandra

import com.datastax.driver.core.Cluster
import moonbox.common.MbLogging
import moonbox.core.datasys.DataSystem

import scala.collection.JavaConverters._

class CassandraDataSystem(props: Map[String, String]) extends DataSystem(props) with MbLogging {

	checkOptions("spark.cassandra.connection.host")

	private def getCluster: Cluster = {
        val hosts = props("spark.cassandra.connection.host").split(",")

        if(props.contains("spark.cassandra.auth.username") && props.contains("spark.cassandra.auth.password")){
            val user = props("spark.cassandra.auth.username")
            val password = props("spark.cassandra.auth.password")

            Cluster.builder().addContactPoints(hosts:_*)
                    .withCredentials(user, password)
                    .build()
        }else{
            Cluster.builder().addContactPoints(hosts:_*).build()
        }
	}

	override def tableNames(): Seq[String] = {
        val keySpace = props("keyspace")
        val cluster: Cluster = getCluster
        val tableNames = cluster.getMetadata.getKeyspace(keySpace).getTables.asScala.map{_.getName}.toSeq
        cluster.close()
        tableNames
	}

	override def tableName(): String = {
        props("table")
	}

	override def tableProperties(tableName: String): Map[String, String] = {
        props.+("table" -> tableName)
	}

	override def test(): Unit = {
		var cluster: Cluster = null
		try {
			cluster = getCluster
		} catch {
			case e: Throwable =>
				logError("cassandra test failed.", e)
				throw e
		} finally {
			if (cluster != null) {
				cluster.close()
			}
		}
	}
}
