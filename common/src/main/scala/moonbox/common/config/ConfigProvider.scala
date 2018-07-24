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


trait ConfigProvider {
	def get(key: String): Option[String]
}

class EnvProvider extends ConfigProvider {
	override def get(key: String): Option[String] = sys.env.get(key)
}

class SystemProvider extends ConfigProvider {
	override def get(key: String): Option[String] = sys.props.get(key)
}

class MapProvider(conf: java.util.Map[String, String]) extends ConfigProvider {
	override def get(key: String): Option[String] = Option(conf.get(key))
}

class MbConfigProvider(conf: java.util.Map[String, String]) extends ConfigProvider {
	import ConfigEntry._
	override def get(key: String): Option[String] = {
		if (key.startsWith("moonbox.")) {
			Option(conf.get(key)).orElse(defaultValueString(key))
		} else {
			None
		}
	}

	private def defaultValueString(key: String): Option[String] = {
		findEntry(key) match {
			case e: ConfigEntryWithDefault[_] => Option(e.defaultValueString)
			case e: ConfigEntryWithDefaultString[_] => Option(e.defaultValueString)
			case e: FallbackConfigEntry[_] => get(e.fallback.key)
			case _ => None
		}
	}
}


