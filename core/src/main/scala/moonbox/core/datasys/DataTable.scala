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

package moonbox.core.datasys

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

class DataTable(iter: Iterator[Row],
	val schema: StructType,
	closeIfNeed: () => Unit) {

	def iterator: Iterator[Row] = {
		new Iterator[Row] {
			private var flag = true

			override def hasNext: Boolean = {
				val hasNext = iter.hasNext
				if (flag && !hasNext) {
					closeIfNeed()
					flag = false
				}
				hasNext
			}

			override def next(): Row = {
				iter.next()
			}
		}
	}

}
