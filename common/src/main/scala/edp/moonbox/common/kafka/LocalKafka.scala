package edp.moonbox.common.kafka

import java.util.Properties

import edp.moonbox.common.EdpLogging
import kafka.server.{KafkaConfig, KafkaServerStartable}

class LocalKafka(zkConnect: String) extends EdpLogging {
	val props = new Properties()
	props.setProperty(KafkaConfig.ZkConnectProp, zkConnect)
	val kafkaConfig = KafkaConfig.fromProps(props)
	val kafkaServer = new KafkaServerStartable(kafkaConfig)
	def start(): Unit = {
		try {
			kafkaServer.startup()
			logInfo("Kafka Server Start.")
		} catch {
			case e: Exception => throw new RuntimeException(e)
		}
	}

	def stop(): Unit = {
		try {
			kafkaServer.shutdown()
			logInfo("Kafka Server Stop.")
		} catch {
			case e: Exception => throw new RuntimeException(e)
		}
	}
}
