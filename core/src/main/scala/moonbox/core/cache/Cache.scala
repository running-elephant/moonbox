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

package moonbox.core.cache

trait Cache {

	def put[K, E](key: K, value: E): Long

	def pipePut[K, E](key: K, values: E*): Unit

	def putRaw(key: Array[Byte], value: Array[Byte]): Long

	def put[K, F, E](key: K, field: F, value: E): Long

	def putRaw(key: Array[Byte], field: Array[Byte], value: Array[Byte]): Long

	def get[K, F, E](key: K, field: F): E

	def getRaw(key: Array[Byte], field: Array[Byte]): Array[Byte]

	def getRange[K, E](key: K, start: Long, end: Long): Seq[E]

	def getRawRange(key: Array[Byte], start: Long, end: Long): Seq[Array[Byte]]

	def size[K](key: K): Long

	def close: Unit

	/*def put[K, E](key: K, value: TraversableOnce[E]): Unit

	def bulkPut[K, E, C <: TraversableOnce[E]](key: K, iter: TraversableOnce[C]): Unit

	def get[K, E, C <: TraversableOnce[E]](key: K, start: Long, end: Long): TraversableOnce[C]

	def getAsIterator[K, E, C <: TraversableOnce[E]](key: K, fetchSize: Int, total: Long = Long.MaxValue): Iterator[C]
	*/



}
