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

package moonbox.grid.deploy.master.transport

import moonbox.grid.deploy.transport.server.JdbcServer
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class JdbcServerTest extends FunSuite with BeforeAndAfterAll {

  val host = "localhost"
  val port = 8080

  override def beforeAll() {

  }

  override def afterAll() {

  }

  test("testStart") {
    new JdbcServer(host, port, null, null).start()
  }

}
