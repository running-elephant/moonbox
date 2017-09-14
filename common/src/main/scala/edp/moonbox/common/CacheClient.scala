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

package edp.moonbox.common


trait CacheClient[A] {

	val DEFAULT_FETCH_SIZE = 100

	def close: Unit

	def mapKeyValues(key: String): Iterator[(String, String)]

	def mapKeyValues(key: String, fetchSize: Int): Iterator[(String, String)]

	def mapValue(key: String, field: String): String

	def mapKeyExists(key: String, field: String): Boolean

	def mapKeys(key: String): scala.collection.mutable.Set[String]

	def mapPutKeyValue(key: String, field: String, value: String): Long

	def mapPutKeyValues(key: String, map: Map[String, String]): Unit

	def mapDeleteValue(key: String, field: String)

	def mapDeleteValues(key: String, fieldSet: Seq[String])

	def listAdd(key: String, values: String*): Long

	def listValue(key: String, position: Long): String

	def listValues(key: String, start: Long, end: Long): List[String]

	def listLength(key: String): Long

	def AllKeys(key: String): scala.collection.mutable.Set[String]

}


