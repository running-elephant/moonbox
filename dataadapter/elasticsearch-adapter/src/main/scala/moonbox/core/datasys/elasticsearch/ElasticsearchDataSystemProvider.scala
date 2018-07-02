package moonbox.core.datasys.elasticsearch

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class ElasticSearchDataSystemProvider extends DataSystemProvider
	with DataSystemRegister {

	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new ElasticSearchDataSystem(parameters)
	}

	override def shortName(): String = "elasticsearch"

	override def dataSource(): String = "org.elasticsearch.spark.sql"
}
