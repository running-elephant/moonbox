package moonbox.grid.deploy.master

import akka.actor.ActorSystem
import moonbox.common.MbConf

abstract class PersistModeFactory {
	def createPersistEngine(): PersistenceEngine
}

class ZookeeperPersistModeFactory(conf: MbConf, akkaSystem: ActorSystem) extends PersistModeFactory {
	override def createPersistEngine(): PersistenceEngine = {
		new ZookeeperPersistenceEngine(conf, akkaSystem)
	}
}

class HdfsPersistModeFactory(conf: MbConf) extends PersistModeFactory {
	override def createPersistEngine(): PersistenceEngine = {
		new HdfsPersistenceEngine(conf)
	}
}
