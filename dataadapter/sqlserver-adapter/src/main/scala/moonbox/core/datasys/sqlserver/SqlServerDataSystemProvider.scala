package moonbox.core.datasys.sqlserver

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}


class SqlServerDataSystemProvider extends DataSystemProvider with DataSystemRegister {
	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new SqlServerDataSystem(parameters)
	}

	override def shortName(): String = "sqlserver"

	override def dataSource(): String = "org.apache.spark.sql.execution.datasources.mbjdbc"
}
