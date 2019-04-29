package moonbox.core.datasys.kafka


import moonbox.common.MbLogging
import moonbox.core.datasys.DataSystem


class KafkaDataSystem(props: Map[String, String]) extends DataSystem(props) with MbLogging {

	val SERVERS = "kafka.bootstrap.servers"
	val SUBSCRIBE = "subscribe"
	val TOPIC = "topic"
	val STARTING_OFFSETS = "startingOffsets"
	val ENDING_OFFSETS = "endingOffsets"

	checkOptions(SERVERS, TOPIC)

	override def tableNames(): Seq[String] = {
		Seq.empty
	}

	override def tableName(): String = {
		props(TOPIC)
	}

	override def tableProperties(tableName: String): Map[String, String] = {
		props.+(TOPIC -> props(TOPIC), SUBSCRIBE -> props(TOPIC))
	}
}
