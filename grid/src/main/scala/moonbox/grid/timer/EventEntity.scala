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

package moonbox.grid.timer

import java.util.Date

object EventEntity {
	val HANDLER = "handler"
	val DEFINER_ORG = "org"
	val DEFINER_NAME = "definer"
	val NAME = "name"
	val LANG = "lang"
	val SQLS = "sqls"
	val CONFIG = "config"
}

case class EventEntity(
	group: String,
	name: String,
	lang: String,
	sqls: Seq[String],
	config: Map[String, String],
	cronExpr: String,
	org: String,
	definer: String,
	start: Option[Date],
	end: Option[Date],
	desc: Option[String]
)
