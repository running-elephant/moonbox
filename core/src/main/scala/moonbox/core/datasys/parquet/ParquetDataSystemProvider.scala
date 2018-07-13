package moonbox.core.datasys.parquet

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class ParquetDataSystemProvider extends DataSystemProvider with DataSystemRegister {
	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new ParquetDataSystem(parameters)
	}

	override def shortName(): String = "parquet"

	override def dataSource(): String = "parquet"
}
