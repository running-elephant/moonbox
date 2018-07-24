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

package moonbox.localservice

import java.util.Properties

import org.scalatest.{BeforeAndAfterAll, FunSuite}

class LocalZookeeperSuite extends FunSuite with BeforeAndAfterAll {
	var zookeeper: LocalZookeeper = _

	override protected def beforeAll(): Unit = {
		zookeeper = new LocalZookeeper(new Properties())
	}

	override protected def afterAll(): Unit = {
		if (zookeeper.isRunning) zookeeper.stop()
	}

	test("start server") {
		zookeeper.start()
		assert(zookeeper.isRunning)
	}

	test("stop server") {
		zookeeper.stop()
		assert(zookeeper.isStop)
	}

}
