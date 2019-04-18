/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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
