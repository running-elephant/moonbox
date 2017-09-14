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

package edp.moonbox.grid.rest.entities


sealed trait Entity

case class SqlQuery(sessionId: Option[String], sqlList: Seq[String]) extends Entity
case class SqlProgress(sessionId: Option[String], jobId: String) extends Entity
case class SqlResult(sessionId: Option[String], jobId: String, offset: Long, size: Long) extends Entity
case class SqlCancel(sessionId: Option[String], jobId: String) extends Entity
case object SqlOpenSession extends Entity
case class SqlCloseSession(sessionId: String) extends Entity

case class Response(jobId: String, message: String) extends Entity
