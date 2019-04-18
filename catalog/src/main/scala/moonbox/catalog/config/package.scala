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
