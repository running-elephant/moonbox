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

package moonbox.testkit.server

import org.scalatest.{BeforeAndAfterAll, FunSuite}

class LocalJdbcSuite extends FunSuite with BeforeAndAfterAll {

	var jdbcServer: LocalJdbc = _

	override protected def beforeAll(): Unit = {
		jdbcServer = new LocalJdbc()
	}

	override protected def afterAll(): Unit = {
		if (jdbcServer.isRunning) jdbcServer.stop()
	}

	test("start server") {
		jdbcServer.start()
		assert(jdbcServer.isRunning)
	}

	test("stop server") {
		jdbcServer.stop()
		assert(jdbcServer.isStop)
	}


}
