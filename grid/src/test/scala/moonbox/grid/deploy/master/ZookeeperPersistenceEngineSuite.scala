package moonbox.grid.deploy.master


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

	test("serialize string") {
		zookeeperPersistenceEngine.persist("string_1", "abc")
		zookeeperPersistenceEngine.persist("string_2", "bcd")
		val strings: Seq[String] = zookeeperPersistenceEngine.read[String]("string")
		assert(strings.contains("abc"))
		assert(strings.contains("bcd"))
	}

	test("serialize ActorRef") {
		val actorRef: ActorRef = akkaSystem.actorOf(Props(classOf[MockActor]))
		zookeeperPersistenceEngine.persist("actorRef_1", actorRef)
		val refs: Seq[ActorRef] = zookeeperPersistenceEngine.read[ActorRef]("actorRef")
		assert(refs.contains(actorRef))
	}

	test("serialize beans") {
		val bean1 = MockBean(1, "bean1")
		val bean2 = MockBean(2, "bean2")
		zookeeperPersistenceEngine.persist("bean1", bean1)
		zookeeperPersistenceEngine.persist("bean2", bean2)
		val beans: Seq[MockBean] = zookeeperPersistenceEngine.read[MockBean]("bean")
		assert(beans.contains(bean1))
		assert(beans.contains(bean2))
	}
}

class MockActor extends Actor {
	override def receive: Receive = {
		case _ =>
	}
}

case class MockBean(id: Int, name: String)
