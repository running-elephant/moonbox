package moonbox.core.datasys.cassandra

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class CassandraDataSystemProvider extends DataSystemProvider with DataSystemRegister {
	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new CassandraDataSystem(parameters)
	}

	override def shortName(): String = "cassandra"

	override def dataSource(): String = "org.apache.spark.sql.cassandra"
}
