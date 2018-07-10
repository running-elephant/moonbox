package moonbox.core.datasys.hive

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class HiveDataSystemProvider extends DataSystemProvider with DataSystemRegister {
	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new HiveDataSystem(parameters)
	}

	override def shortName(): String = "hive"

	override def dataSource(): String = "hive"
}
