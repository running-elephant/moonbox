/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

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
