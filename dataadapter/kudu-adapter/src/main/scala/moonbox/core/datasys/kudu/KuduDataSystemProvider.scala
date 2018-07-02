package moonbox.core.datasys.kudu

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class KuduDataSystemProvider extends DataSystemProvider with DataSystemRegister {
	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new KuduDataSystem(parameters)
	}

	override def shortName(): String = "kudu"

	override def dataSource(): String = "org.apache.kudu.sql.kudu"
}
