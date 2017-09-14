/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.core.split


object PlatformFactory {
	def newInstance(props: Map[String, String]): Platform = {
		val datasource: String = props.getOrElse("type", throw new Exception)
		datasource.toLowerCase() match {
			case "mysql" | "oracle" | "jdbc" => new JdbcPlatform(props)
			case "es5" | "es" => new EsV5Platform(props)
			case _ => throw new Exception("unknown platform")
		}
	}
}
