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

package edp.moonbox.core.parser

import edp.moonbox.core._
import org.apache.spark.sql._


class MbAnalyzer {

	def analyze(cmd: MbCommand): MbCommandExec = cmd match {
		case MountTable(table, options) =>
			MountTableExec(table, options)
		case UnmountTable(table) =>
			UnmountTableExec(table)
		case Select(sql) =>
			SelectExec(sql)
		case CreateViewAsSelect(view, ignore, select) =>
			CreateViewAsSelectExec(view, ignore, analyze(select).asInstanceOf[SelectExec])
		case _ => throw new Exception("unsupported command ")
	}
}
