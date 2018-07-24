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

package moonbox.core.execution.standalone

import org.apache.spark.sql.SaveMode
import moonbox.core.datasys.{DataSystem, Insertable}

import scala.collection.mutable

class DataTableWriter(dt: DataTable) {
	private var source: String = _
	private var mode: SaveMode = SaveMode.ErrorIfExists
	private val options = new mutable.HashMap[String, String]

	def mode(saveMode: SaveMode): DataTableWriter = {
		this.mode = saveMode
		this
	}

	def format(source: String): DataTableWriter = {
		this.source = source
		this.options += ("type" -> source)
		this
	}

	def option(key: String, value: String): DataTableWriter = {
		this.options += (key -> value)
		this
	}

	def options(options: scala.collection.Map[String, String]): DataTableWriter = {
		this.options ++= options
		this
	}

	def save(): Unit = {
		DataSystem.lookupDataSystem(options.toMap) match {
			case insertable: Insertable => insertable.insert(dt, mode)
			case _ => throw new Exception(s"Datasytem $source is not insertable")
		}
	}

}
