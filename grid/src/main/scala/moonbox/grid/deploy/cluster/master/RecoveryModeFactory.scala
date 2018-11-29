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

package moonbox.grid.deploy.cluster.master

import akka.actor.ActorSystem
import moonbox.common.MbConf

abstract class RecoveryModeFactory {
	def createPersistEngine(): PersistenceEngine

	def createLeaderElectionAgent(candidate: LeaderElectable): LeaderElectionAgent
}

class ZookeeperRecoveryModeFactory(conf: MbConf, akkaSystem: ActorSystem) extends RecoveryModeFactory {
	override def createPersistEngine(): PersistenceEngine = {
		new ZookeeperPersistenceEngine(conf, akkaSystem)
	}

	override def createLeaderElectionAgent(candidate: LeaderElectable): LeaderElectionAgent = {
		new ZooKeeperLeaderElectionAgent(candidate, conf)
	}
}

class HdfsPersistModeFactory(conf: MbConf) extends RecoveryModeFactory {
	override def createPersistEngine(): PersistenceEngine = {
		new HdfsPersistenceEngine(conf)
	}

	override def createLeaderElectionAgent(candidate: LeaderElectable): LeaderElectionAgent = {
		new MonarchyLeaderAgent(candidate)
	}
}
