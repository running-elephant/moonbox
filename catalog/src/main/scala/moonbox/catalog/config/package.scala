package moonbox.catalog

import moonbox.common.config.ConfigBuilder

package object config {
	val CATALOG_IMPLEMENTATION = ConfigBuilder("moonbox.deploy.catalog.implementation")
		.stringConf
		.createOptional

	val JDBC_CATALOG_URL = ConfigBuilder("moonbox.deploy.catalog.url")
		.stringConf
		.createOptional

	val JDBC_CATALOG_USER = ConfigBuilder("moonbox.deploy.catalog.user")
		.stringConf
		.createOptional
	val JDBC_CATALOG_PASSWORD = ConfigBuilder("moonbox.deploy.catalog.password")
		.stringConf
		.createOptional
	val JDBC_CATALOG_DRIVER = ConfigBuilder("moonbox.deploy.catalog.driver")
		.stringConf
		.createOptional

	val JDBC_CATALOG_AWAIT_TIMEOUT = ConfigBuilder("moonbox.deploy.catalog.await-timeout")
		.timeConf
		.createWithDefaultString("20s")
}
