package edp.moonbox.common.elasticsearch

import java.util
import java.util.Properties

import edp.moonbox.common.EdpLogging
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.node.{InternalSettingsPreparer, Node}
import org.elasticsearch.plugins.Plugin
import org.elasticsearch.transport.Netty3Plugin

class LocalElasticsearch(clusterName: String,
                         homePath: String,
                         datPath: String,
                         httpRange: String,
                         transportRange: String,
                         hasSlave: Boolean) extends EdpLogging {

	val (settings, plugins) = init()

	val node = new PluginConfigurableNode(settings, plugins)

	private def init(): (Settings, util.Collection[java.lang.Class[_ <: Plugin]]) = {
		val props: Properties = new Properties()
		props.setProperty("path.home", homePath)
		props.setProperty("path.data", datPath)
		props.setProperty("http.port", httpRange)
		props.setProperty("transport.tcp.port", transportRange)
		props.setProperty("cluster.name", clusterName)
		props.setProperty("transport.type", "local")
		props.setProperty("http.type", "netty3")
		props.setProperty("script.inline", "true")
		props.setProperty("node.ingest", "true")
		val settings: Settings = Settings.builder().put(props).build()
		val plugins: util.Collection[java.lang.Class[_ <: Plugin]] = util.Arrays.asList(classOf[Netty3Plugin])
		(settings, plugins)
	}

	def start(): Unit = {
		try {
			node.start()
			logInfo("Elasticsearch Server Start.")
		} catch {
			case e: Exception => throw new RuntimeException(e)
		}
	}

	def stop(): Unit = {
		if (!node.isClosed) {
			node.close()
			logInfo("Elasticsearch Server Stop.")
		} else {
			logWarning("Elasticsearch Server Is Not Running")
		}
	}

	def isStop: Boolean = node.isClosed

	def isRunning: Boolean = !isStop

	// for using protected construction
	class PluginConfigurableNode(settings: Settings,
	                             plugins: util.Collection[java.lang.Class[_ <: Plugin]])
		extends Node(InternalSettingsPreparer.prepareEnvironment(settings, null), plugins)
}
