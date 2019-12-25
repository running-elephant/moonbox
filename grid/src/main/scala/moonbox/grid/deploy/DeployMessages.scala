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

package moonbox.grid.deploy

import akka.actor.{ActorRef, Address}
import moonbox.common.util.Utils
import moonbox.grid.deploy.app.DriverDesc
import moonbox.grid.deploy.app.DriverState.DriverState
import moonbox.grid.deploy.rest.entities.Node
import moonbox.grid.deploy.security.Session
import moonbox.grid.timer.EventEntity


sealed trait DeployMessages extends Serializable

object DeployMessages {

	case object ElectedLeader extends DeployMessages

	case object RevokedLeadership extends DeployMessages

	case class BeginRecovery() extends DeployMessages

	case object CompleteRecovery extends DeployMessages

	case object CheckForWorkerTimeOut extends DeployMessages

	case class RegisterWorker(
		id: String,
		host: String,
		port: Int,
		worker: ActorRef,
		address: Address) extends DeployMessages {
	}

	case class MasterChanged(masterRef: ActorRef) extends DeployMessages

	case class WorkerSchedulerStateResponse(workerId: String, driverIds: Seq[String])

	case class WorkerLatestState(workerId: String, driverIds: Seq[String]) extends DeployMessages

	case class Heartbeat(workerId: String, worker: ActorRef) extends DeployMessages

	case class ReconnectWorker(masterRef: ActorRef) extends DeployMessages

	sealed trait RegisterWorkerResponse

	case class RegisteredWorker(masterAddress: ActorRef) extends DeployMessages with RegisterWorkerResponse

	case object MasterInStandby extends DeployMessages with RegisterWorkerResponse with RegisterApplicationResponse

	case class RegisterWorkerFailed(message: String) extends DeployMessages with RegisterWorkerResponse

	case object SendHeartbeat extends DeployMessages

	case class LaunchDriver(driverId: String, desc: DriverDesc) extends DeployMessages

	case class DriverStateChanged(
		driverId: String,
		state: DriverState,
		appId: Option[String],
		exception: Option[Exception],
		time: Long = Utils.now
	) extends DeployMessages

	case class KillDriver(driverId: String) extends DeployMessages

	case class RegisterApplication(
		driverId: String,
		host: String,
		port: Int,
		endpoint: ActorRef,
		address: Address,
		dataPort: Int,
		appType: String
	)

	sealed trait RegisterApplicationResponse

	case class RegisteredApplication(masterRef: ActorRef) extends RegisterApplicationResponse

	case class RegisterApplicationFailed(message: String) extends RegisterApplicationResponse

	case class ApplicationStateResponse(driverId: String) extends DeployMessages

	case class RegisterTimedEvent(event: EventEntity)

	sealed trait RegisterTimedEventResponse

	case class RegisteredTimedEvent(masterRef: ActorRef) extends RegisterTimedEventResponse

	case class RegisterTimedEventFailed(message: String) extends RegisterTimedEventResponse

	case class UnregisterTimedEvent(group: String, name: String)

	sealed trait UnregisterTimedEventResponse

	case class UnregisteredTimedEvent(masterRef: ActorRef) extends UnregisterTimedEventResponse

	case class UnregisterTimedEventFailed(message: String) extends UnregisterTimedEventResponse


	case class RequestSubmitDriver(driverName: String, driverDesc: DriverDesc) extends DeployMessages

	case class SubmitDriverResponse(
		master: ActorRef, success: Boolean, driverId: Option[String], message: String) extends DeployMessages

	case class RequestKillDriver(driverId: String) extends DeployMessages

	case class KillDriverResponse(master: ActorRef, driverId: String, success: Boolean, message: String)
			extends DeployMessages

	case class RequestDriverStatus(driverId: String) extends DeployMessages

	case class DriverStatusResponse(found: Boolean, driverId: String, driverType: Option[String], startTime: Option[Long], state: Option[DriverState], updateTime: Option[Long],
		workerId: Option[String], workerHostPort: Option[String], exception: Option[Exception])

	case class RequestAllDriverStatus(pattern: Option[String] = None) extends DeployMessages

	case class AllDriverStatusResponse(driverStatus: Seq[DriverStatusResponse], exception: Option[Exception]) extends DeployMessages

	// rest server to master
	case object RequestMasterAddress extends DeployMessages

	case class MasterAddress(master: String, restServer: Option[String], tcpServer: Option[String]) extends DeployMessages

	case object RequestClusterState extends DeployMessages

	case class ClusterStateResponse(nodes: Seq[Node]) extends DeployMessages

	// jdbc server to master
	case class RequestApplicationAddress(session: Session, appType: String, appName: Option[String]) extends DeployMessages

	case class ApplicationAddressResponse(found: Boolean,
		host: Option[String] = None, port: Option[Int] = None, exception: Option[Exception] = None) extends DeployMessages

}
