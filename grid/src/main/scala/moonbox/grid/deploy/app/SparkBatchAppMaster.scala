package moonbox.grid.deploy.app

import moonbox.catalog.JdbcCatalog

class SparkBatchAppMaster(jdbcCatalog: JdbcCatalog) extends AppMaster(jdbcCatalog) {

	override def createDriverDesc(config: Map[String, String]): DriverDesc = new SparkBatchDriverDesc(config)

	override def configTemplate: Map[String, String] = Map()

	override def typeName: String = "sparkbatch"

	override def resourceTemplate: Map[String, String] = Map()
}
