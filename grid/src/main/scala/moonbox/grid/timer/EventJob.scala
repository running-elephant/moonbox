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

import moonbox.common.MbLogging
import org.quartz.{Job, JobExecutionContext}

class EventJob extends Job with MbLogging {
	override def execute(ctx: JobExecutionContext): Unit = {
		val dataMap = ctx.getMergedJobDataMap
		val org = dataMap.getString(EventEntity.DEFINER_ORG)
		val definer = dataMap.getString(EventEntity.DEFINER_NAME)
		val name = dataMap.getString(EventEntity.NAME)
		val lang = dataMap.getString(EventEntity.LANG)
		val sqls = dataMap.get(EventEntity.SQLS).asInstanceOf[Seq[String]]
		val config = dataMap.get(EventEntity.CONFIG).asInstanceOf[Map[String, String]]
		val func = dataMap.get(EventEntity.HANDLER).asInstanceOf[EventHandler]
		logInfo(s"""Timed event fire as user '$definer' run sqls (${sqls.mkString(", ")})""")
		func(org, definer, lang, sqls, config + (EventEntity.NAME -> name))
	}
}
