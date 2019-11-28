package moonbox.grid.deploy.app

class SparkLocalAppMaster extends AppMaster {

	override def createDriverDesc(config: Map[String, String]): DriverDesc = new SparkLocalDriverDesc(config)

	override def configTemplate: Map[String, String] = Map()

	override def typeName: String = "sparklocal"

	override def resourceTemplate: Map[String, String] = Map()
}
