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

package moonbox.grid

import akka.actor.ActorRef
import moonbox.core.command.MbCommand
import moonbox.protocol.app.JobState.JobState

case class JobInfo(	  jobId: String,
					  localSessionId: Option[String] = None, 	//local
					  clusterSessionId: Option[String] = None, 	//remote
					  cmds: Seq[MbCommand],
					  var seq: Int = -1,
					  isLocal: Boolean,
					  config: Option[String] = None,
					  var status: JobState,
					  var errorMessage: Option[String],
					  username: Option[String] = None,
					  submitTime: Long,
					  var updateTime: Long,
					  client: ActorRef)
