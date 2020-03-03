package moonbox.network

import moonbox.common.config.ConfigBuilder

package object config {
	val PORT_MAX_RETRY_TIMES = ConfigBuilder("moonbox.network.port.maxRetries")
			.intConf
			.createWithDefault(16)
}
