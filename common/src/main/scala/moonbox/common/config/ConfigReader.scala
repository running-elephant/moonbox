package moonbox.common.config

import scala.collection.mutable

class ConfigReader(conf: ConfigProvider) {
	def this(conf: java.util.Map[String, String]) = this(new MapProvider(conf))

	private val bindings = new mutable.HashMap[String, ConfigProvider]()
	bind(null, conf)
	bindEnv(new EnvProvider())
	bindSystem(new SystemProvider())

	def bind(prefix: String, provider: ConfigProvider): ConfigReader = {
		bindings(prefix) = provider
		this
	}

	def bind(prefix: String, values: java.util.Map[String, String]): ConfigReader = {
		bind(prefix, new MapProvider(values))
	}

	def bindEnv(provider: ConfigProvider): ConfigReader = bind("env", provider)

	def bindSystem(provider: ConfigProvider): ConfigReader = bind("system", provider)

	def get(key: String): Option[String] = conf.get(key)
}
