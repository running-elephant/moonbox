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

package moonbox.grid.deploy.security

import scala.collection.mutable


trait Login {
	def doLogin(username: String, password: String): Session
}

class SessionBuilder(session: Session) {
	def put(key: String, value: String) = {
		session.put(key, value)
		this
	}

	def build(): Session = {
		put(Session.KEY, Session.keysToString(session.keys.toSeq))
		session
	}
}


case class Session private() extends mutable.HashMap[String, String]

object Session {
	val KEY = "SESSION_KEY"

	def builder: SessionBuilder = new SessionBuilder(new Session)

	def keysToString(keys: Seq[String]): String = {
		keys.mkString(",")
	}

	def stringToKeys(keys: String): Seq[String] = {
		keys.split(",").toSeq
	}
}