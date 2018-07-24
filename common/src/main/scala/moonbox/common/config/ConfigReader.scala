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
