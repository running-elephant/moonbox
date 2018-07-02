package moonbox.core.datasys.mysql

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class MysqlDataSystemProvider extends DataSystemProvider with DataSystemRegister {

	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new MysqlDataSystem(parameters)
	}

	override def shortName(): String = "mysql"

	override def dataSource(): String = "org.apache.spark.sql.execution.datasources.mbjdbc"

}
