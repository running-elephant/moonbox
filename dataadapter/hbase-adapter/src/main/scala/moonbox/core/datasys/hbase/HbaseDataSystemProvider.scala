package moonbox.core.datasys.hbase

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class HbaseDataSystemProvider extends DataSystemProvider with DataSystemRegister {
	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new HbaseDataSystem(parameters)
	}

	override def shortName(): String = "hbase"

	override def dataSource(): String = "org.apache.spark.sql.execution.datasources.hbase"
}
