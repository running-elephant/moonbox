package moonbox.core.datasys.cassandra

import com.datastax.driver.core.Cluster
import moonbox.core.datasys.DataSystem

import scala.collection.JavaConverters._

class CassandraDataSystem(props: Map[String, String]) extends DataSystem(props) {
	require(contains("spark.cassandra.connection.host"))

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

	override def test(): Boolean = {
		try {
            val cluster = getCluster
            cluster.getClusterName
            cluster.close()
			true
		}catch {
			case _: Exception => false
		}
	}
}
