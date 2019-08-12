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

package moonbox.client

import scala.collection.{immutable, mutable}

class ClientOptions(val options: CaseInsensitiveMap[String]) {

	import ClientOptions._

	def this(ops: Map[String, String]) = this(CaseInsensitiveMap(ops))

	def this() = this(Map.empty[String, String])

	private val clientKeys: immutable.Seq[String] = HOST :: PORT :: USER :: PASSWORD :: DATABASE :: READ_TIMEOUT :: FETCH_SIZE :: MAX_ROWS :: SERIALIZER :: Nil

	val host: String = options.getOrElse(HOST, "localhost")
	val port: Int = options.get(PORT).map(_.toInt).getOrElse(10010)
	val database: String = options.getOrElse(DATABASE, "default")
	val user: Option[String] = options.get(USER)
	val password: Option[String] = options.get(PASSWORD)
	val timeout: Int = options.get(READ_TIMEOUT).map(_.toInt).getOrElse(3600)
	/* time_unit: second */
	val fetchSize: Int = options.get(FETCH_SIZE).map(_.toInt).getOrElse(1000)
	val maxRows: Int = options.get(MAX_ROWS).map(_.toInt).getOrElse(Int.MinValue)
	val isLocal: Boolean = options.get(IS_LOCAL).exists(_.toBoolean)
	val serializer: String = options.getOrElse(SERIALIZER, "protobuf")
	val extraOptions: Map[String, String] = options.filter { case (k, _) => !clientKeys.contains(k) }
}

object ClientOptions {

	val HOST = "host"
	val PORT = "port"
	val USER = "user"
	val PASSWORD = "password"
	val DATABASE = "database"
	val READ_TIMEOUT = "timeout"
	/* time unit: second */
	val FETCH_SIZE = "fetchsize"
	val MAX_ROWS = "maxrows"
	val IS_LOCAL = "islocal"
	val SERIALIZER = "serializer"

	def builder() = new Builder()

	def builder(clientOptions: ClientOptions) = new Builder().options(clientOptions.options)

	class Builder {
		private val _options = mutable.Map.empty[String, String]

		def options(ops: Map[String, String]) = {
			_options ++= ops
			this
		}

		def host(name: String) = {
			_options += (HOST -> name)
			this
		}

		def port(value: Int) = {
			_options += (PORT -> value.toString)
			this
		}

		def user(name: String) = {
			_options += (USER -> name)
			this
		}

		def password(value: String) = {
			_options += (PASSWORD -> value)
			this
		}

		def database(name: String) = {
			_options += (DATABASE -> name)
			this
		}

		def timeout(seconds: Int) = {
			_options += (READ_TIMEOUT -> seconds.toString)
			this
		}

		def fetchSize(value: Int) = {
			_options += (FETCH_SIZE -> value.toString)
			this
		}

		def maxRows(value: Long) = {
			_options += (MAX_ROWS -> value.toString)
			this
		}

		def isLocal(value: Boolean) = {
			_options += (IS_LOCAL -> value.toString)
			this
		}

		def serializer(name: String) = {
			_options += (SERIALIZER -> name)
			this
		}

		def build() = new ClientOptions(_options.toMap)
	}

}
