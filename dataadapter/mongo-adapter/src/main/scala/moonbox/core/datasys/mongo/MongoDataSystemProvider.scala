package moonbox.core.datasys.mongo

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class MongoDataSystemProvider extends DataSystemProvider with DataSystemRegister {
	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new MongoDataSystem(parameters)
	}

	override def  shortName(): String = "mongo"


	override def dataSource(): String = "com.mongodb.spark.sql"
}
