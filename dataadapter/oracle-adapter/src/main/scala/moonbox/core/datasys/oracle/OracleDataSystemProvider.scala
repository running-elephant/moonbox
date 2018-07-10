package moonbox.core.datasys.oracle

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}


class OracleDataSystemProvider extends DataSystemProvider with DataSystemRegister {
	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new OracleDataSystem(parameters)
	}

	override def shortName(): String = "oracle"

	override def dataSource(): String = "org.apache.spark.sql.execution.datasources.mbjdbc"
}
