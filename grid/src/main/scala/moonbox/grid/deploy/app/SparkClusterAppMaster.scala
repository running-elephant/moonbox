package moonbox.grid.deploy.app

import moonbox.catalog.JdbcCatalog

class SparkClusterAppMaster(jdbcCatalog: JdbcCatalog) extends AppMaster(jdbcCatalog) {

	override def createDriverDesc(config: Map[String, String]): DriverDesc = new SparkClusterDriverDesc(config)

	override def configTemplate: Map[String, String] = Map()

	override def typeName: String = "sparkcluster"

	override def resourceTemplate: Map[String, String] = Map()

	override def onWorkerExit(driverRunner: DriverRunner): Unit = {
		driverRunner.kill()
	}
}
