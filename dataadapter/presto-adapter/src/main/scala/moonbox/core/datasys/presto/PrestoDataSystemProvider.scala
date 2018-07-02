package moonbox.core.datasys.presto

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class PrestoDataSystemProvider extends DataSystemProvider with DataSystemRegister {
	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new PrestoDataSystem(parameters)
	}

	override def shortName(): String = "presto"

	override def dataSource(): String = "org.apache.spark.sql.execution.datasources.presto"
}
