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

import java.util.Date

import akka.actor.{ActorRef, Address}
import moonbox.grid.deploy.app.{AppType, DriverDesc, DriverState}
import DriverState.DriverState
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

	case class WorkerStateResponse(id: String, drivers: Seq[(String, DriverDesc, Date)])

	case class WorkerLatestState(id: String,  drivers: Seq[(String, DriverDesc, Date)]) extends DeployMessages

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
		exception: Option[Exception])
	extends DeployMessages

	case class KillDriver(driverId: String) extends DeployMessages

	case class RegisterApplication(
		id: String,
		label: String,
		host: String,
		port: Int,
		endpoint: ActorRef,
		address: Address,
		dataPort: Int,
		appType: AppType
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

}
