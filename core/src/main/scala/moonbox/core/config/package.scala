package moonbox.core

import moonbox.common.config.ConfigBuilder

package object config {
	val CATALOG_IMPLEMENTATION = ConfigBuilder("moonbox.catalog.implementation")
		.stringConf
		.createWithDefaultString("h2")
	val CATALOG_URL = ConfigBuilder("moonbox.catalog.url")
		.stringConf
		.createWithDefaultString("jdbc:h2:mem:testdb0;DB_CLOSE_DELAY=-1")
	val CATALOG_USER = ConfigBuilder("moonbox.catalog.user")
		.stringConf
		.createWithDefaultString("testUser")
	val CATALOG_PASSWORD = ConfigBuilder("moonbox.catalog.password")
		.stringConf
		.createWithDefaultString("testPass")
	val CATALOG_DRIVER = ConfigBuilder("moonbox.catalog.driver")
		.stringConf
		.createWithDefaultString("org.h2.Driver")
	val CATALOG_RESULT_AWAIT_TIMEOUT = ConfigBuilder("moonbox.catalog.result.await.timeout")
		.timeConf
		.createWithDefaultString("10s")

	val CACHE_ENABLE = ConfigBuilder("moonbox.cache.enable")
		.booleanConf
		.createWithDefault(true)
	val CACHE_IMPLEMENTATION = ConfigBuilder("moonbox.cache.implementation")
		.stringConf
		.createWithDefaultString("redis")
	/*val CACHE_FETCH_SIZE = ConfigBuilder("moonbox.cache.fetchSize")
		.intConf
		.createWithDefault(500)*/
	val CACHE_SERVERS = ConfigBuilder("moonbox.cache.redis.servers")
		.stringConf
		.createWithDefaultString("localhost:6379")

	val MIXCAL_IMPLEMENTATION = ConfigBuilder("moonbox.mixcal.implementation")
		.stringConf
		.createWithDefaultString("spark")
	val MIXCAL_PUSHDOWN_ENABLE = ConfigBuilder("moonbox.mixcal.pushdown.enable")
	    .booleanConf
	    .createWithDefault(true)
	val MIXCAL_COLUMN_PERMISSION_ENABLE = ConfigBuilder("moonbox.mixcal.column.permission.enable")
	    .booleanConf
	    .createWithDefault(false)
	val MIXCAL_SPARK_MASTER = ConfigBuilder("moonbox.mixcal.spark.master")
		.stringConf
		.createWithDefaultString("local[*]")
	val MIXCAL_SPARK_LOGLEVEL = ConfigBuilder("moonbox.mixcal.spark.loglevel")
		.stringConf
		.createWithDefaultString("INFO")
	val MIXCAL_SPARK_SQL_CROSSJOIN_ENABLE = ConfigBuilder("moonbox.mixcal.spark.sql.crossJoin.enable")
		.booleanConf
		.createWithDefault(false)
	val MIXCAL_SPARK_SQL_CBO_ENABLE = ConfigBuilder("moonbox.mixcal.spark.sql.cbo.enable")
		.booleanConf
		.createWithDefault(true)
}
