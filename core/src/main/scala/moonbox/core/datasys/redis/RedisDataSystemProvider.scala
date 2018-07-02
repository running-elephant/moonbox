package moonbox.core.datasys.redis

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class RedisDataSystemProvider extends DataSystemProvider with DataSystemRegister {

	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new RedisDataSystem(parameters)
	}

	override def shortName(): String = "redis"

	override def dataSource(): String = "org.apache.spark.sql.execution.datasources.redis"
}
