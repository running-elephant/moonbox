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

package moonbox.grid.deploy2.node

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import moonbox.common.MbConf
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import moonbox.grid.config._
import moonbox.localservice.LocalZookeeper


class ZookeeperPersistenceEngineSuite extends FunSuite with BeforeAndAfterAll {
	private val conf = new MbConf()
	    .set(PERSIST_SERVERS.key, "localhost:2181")
	private var localZookeeper: LocalZookeeper = _
	private var zookeeperPersistenceEngine: ZookeeperPersistenceEngine = _
	private var akkaSystem: ActorSystem = _
	override protected def beforeAll(): Unit = {
		localZookeeper = new LocalZookeeper()
		localZookeeper.start()
		akkaSystem = ActorSystem("test")
		zookeeperPersistenceEngine = new ZookeeperPersistenceEngine(conf, akkaSystem)
	}

	override protected def afterAll(): Unit = {
		localZookeeper.stop()
	}

	test("serialize nodeInfo") {
		val actorRef = akkaSystem.actorOf(Props(classOf[MockActor]))
		val node = new NodeInfo("node-1", "locahost", 2551, 8, 1000, actorRef)
		zookeeperPersistenceEngine.addNode(node)
		val nodes = zookeeperPersistenceEngine.readNodes()
		//assert(nodes.length == 1)
		assert(nodes.contains(node))
	}
}

class MockActor extends Actor {
	override def receive: Receive = {
		case _ =>
	}
}

case class MockBean(id: Int, name: String)
