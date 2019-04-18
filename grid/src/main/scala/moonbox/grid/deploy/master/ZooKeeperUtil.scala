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

package moonbox.grid.deploy.master

import moonbox.common.{MbConf, MbLogging}
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry

object ZooKeeperUtil extends MbLogging {
	private val ZK_CONNECTION_TIMEOUT_MILLIS = 15000
	private val ZK_SESSION_TIMEOUT_MILLIS = 10000
	private val RETRY_WAIT_MILLIS = 1000
	private val MAX_RECONNECT_ATTEMPTS = 3

	def newClient(conf: MbConf): CuratorFramework = {
		val ZK_URL = conf.get(moonbox.grid.config.RECOVERY_ZOOKEEPER_URL)
		val zk = CuratorFrameworkFactory.newClient(ZK_URL,
			ZK_SESSION_TIMEOUT_MILLIS, ZK_CONNECTION_TIMEOUT_MILLIS,
			new ExponentialBackoffRetry(RETRY_WAIT_MILLIS, MAX_RECONNECT_ATTEMPTS))
		zk.start()
		zk
	}
}
