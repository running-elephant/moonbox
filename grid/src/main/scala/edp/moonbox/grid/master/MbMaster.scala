/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.grid.master

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.cluster.client.ClusterClientReceptionist
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import com.typesafe.config.ConfigFactory
import edp.moonbox.common.{EdpLogging, Util}
import edp.moonbox.core.MbConf
import edp.moonbox.grid.rest.RestServer
import edp.moonbox.grid.rest.actor.DataFetchActor

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._


class MbMaster(param: MasterParam) extends EdpLogging {
	import MbMaster._

	implicit val akkaSystem = ActorSystem(CLUSTER_NAME, ConfigFactory.parseMap(param.akka))

	val masterAddress = Util.akkaAddress(CLUSTER_NAME, param.host, param.port, RECEPTIONIST_PATH)

	val persistEngine = PersistEngine.newInstance(param.conf)

	private def init(): Unit = {
		logInfo("write master url to persist engine.")
		persistEngine.setMasterURL(masterAddress)
	}

	def startUp(): Unit = {
		logInfo("starting MbMaster ...")
		init()
		startScheduler()
		val endpoint = startEndpoint()
		val dataFetcher = startDataFetcher()
		startRestServer(endpoint, dataFetcher)
	}

	private def startScheduler(): Unit = {
		logInfo("starting scheduler ...")
		val schedulerProp = ClusterSingletonManager.props(
			singletonProps = Props(classOf[MasterBackend], param.conf, akkaSystem, persistEngine),
			PoisonPill,
			ClusterSingletonManagerSettings(akkaSystem).withRole(ROLE)
		)
		akkaSystem.actorOf(schedulerProp, SCHEDULER_NAME)
	}

	private def startEndpoint(): ActorRef = {
		val singletonProps = ClusterSingletonProxy.props(
			settings = ClusterSingletonProxySettings(akkaSystem)
				.withRole(ROLE).withBufferSize(5000)
				.withSingletonIdentificationInterval(FiniteDuration(5, SECONDS)),
			singletonManagerPath = SINGLETON_MANAGER_PATH)

		val endpoint = akkaSystem.actorOf(singletonProps, SINGLETON_PROXY_NAME)
		ClusterClientReceptionist(akkaSystem).registerService(endpoint)

		endpoint
	}

	private def startDataFetcher(): ActorRef = {
		val (host, port) = param.cacheHostPort
		val dataFetcher = akkaSystem.actorOf(Props(classOf[DataFetchActor], host, port))
		dataFetcher
	}

	private def startRestServer(endpoint: ActorRef, dataFetcher: ActorRef): Unit = {
		new RestServer(param.host, param.restPort, endpoint, dataFetcher, akkaSystem).start()
		logInfo(s"start reset server, listen on ${param.host}:${param.restPort}")
	}
}

object MbMaster {
	val CLUSTER_NAME = "moonbox"
	val ROLE = "master"
	val RECEPTIONIST_PATH ="/system/receptionist"
	val SCHEDULER_NAME = "scheduler"
	val SINGLETON_PROXY_NAME = "singleton-master-proxy"
	val SINGLETON_MANAGER_PATH = s"/user/$SCHEDULER_NAME"

	def main(args: Array[String]) {

		val conf = new MbConf(true)
		val param = new MasterParam(args, conf)
		new MbMaster(param).startUp()
	}
}
