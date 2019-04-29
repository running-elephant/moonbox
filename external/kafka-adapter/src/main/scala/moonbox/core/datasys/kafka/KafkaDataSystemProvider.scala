package moonbox.core.datasys.kafka

import moonbox.core.datasys.{DataSystem, DataSystemProvider, DataSystemRegister}

class KafkaDataSystemProvider extends DataSystemProvider with DataSystemRegister {

	override def createDataSystem(parameters: Map[String, String]): DataSystem = {
		new KafkaDataSystem(parameters)
	}

	override def shortName(): String = "kafka"

	override def dataSource(): String = "kafka"
}
