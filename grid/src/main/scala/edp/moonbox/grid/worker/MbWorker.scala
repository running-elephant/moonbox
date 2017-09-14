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

package edp.moonbox.grid.worker

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.singleton.{ClusterSingletonProxy, ClusterSingletonProxySettings}
import com.typesafe.config.ConfigFactory
import edp.moonbox.common.EdpLogging
import edp.moonbox.core.MbConf
import edp.moonbox.grid.master.MbMaster
import org.apache.spark.sql.MbSession

import scala.concurrent.duration._


class MbWorker(param: WorkerParam) extends EdpLogging {
	import MbWorker._

	val akkaSystem = ActorSystem(MbMaster.CLUSTER_NAME, ConfigFactory.parseMap(param.akka))

	def startUp(): Unit = {
		val resources = param.conf.systemAddJars
		logInfo(s"add jars to class path: ${resources.mkString("\n")}")
		resources.foreach(MbSession.addJar)
		val endpoint = startEndpoint()
		startBackend(endpoint)
	}

	private def startBackend(endpoint: ActorRef): ActorRef = {
		akkaSystem.actorOf(Props(classOf[WorkerBackend], endpoint, param), WorkerBackend.NAME)
	}

	private def startEndpoint(): ActorRef = {
		val singletonProps = ClusterSingletonProxy.props(
			settings = ClusterSingletonProxySettings(akkaSystem)
				.withRole(MbMaster.ROLE).withBufferSize(5000)
				.withSingletonIdentificationInterval(FiniteDuration(5, SECONDS)),
			singletonManagerPath = MbMaster.SINGLETON_MANAGER_PATH)

		val endpoint = akkaSystem.actorOf(singletonProps, MbMaster.SINGLETON_PROXY_NAME)
		endpoint
	}
}

object MbWorker {
	val ROLE = "worker"
	val PATH = s"/user/$ROLE"

	def main(args: Array[String]) {
		val conf = new MbConf(true)
		val param: WorkerParam = new WorkerParam(args, conf)
		new MbWorker(param).startUp()

	}
}
