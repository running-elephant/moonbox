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

package moonbox.grid.deploy.messages

import moonbox.grid.deploy.Interface.Dag


sealed trait Message extends Serializable

object Message {

	// control
	sealed trait JobMessage extends Message

	case class OpenSession(org: String, username: String,
		database: Option[String], config: Map[String, String]) extends JobMessage

	case class OpenSessionResponse(
		sessionId: Option[String], workerHost: Option[String] = None, workerPort: Option[Int] = None, message: String) extends JobMessage

	case class CloseSession(sessionId: String) extends JobMessage

	case class CloseSessionResponse(
		sessionId: String,
		success: Boolean,
		message: String) extends JobMessage

	// for interactive
	case class JobQuery(sessionId: String, sqls: Seq[String], fetchSize: Int, maxRows: Int) extends JobMessage

	case class JobQueryResponse(
		success: Boolean,
		schema: String,
		data: Seq[Seq[Any]], // TODO
		hasNext: Boolean,
		message: String
	) extends JobMessage

	case class JobQueryNextResult(sessionId: String) extends JobMessage
	case class JobQueryNextResultResponse(
		success: Boolean,
		schema: String,
		data: Seq[Seq[Any]], // TODO
		hasNext: Boolean,
		message: String) extends JobMessage

	case class InteractiveJobCancel(sessionId: String) extends JobMessage
	case class InteractiveJobCancelResponse(success: Boolean, message: String) extends JobMessage

	// for batch
	case class JobSubmit(org: String, username: String, lang: String, sqls: Seq[String], config: Map[String, String]) extends JobMessage
	case class JobSubmitResponse(jobId: Option[String], message: String) extends JobMessage


	case class JobProgress(jobId: String) extends JobMessage
	case class JobProgressState(jobId: String, submitTime: Long, state: String, message: String) extends JobMessage

	case class BatchJobCancel(jobId: String) extends JobMessage
	case class BatchJobCancelResponse(jobId: String, success: Boolean, message: String) extends JobMessage

	// service
	sealed trait ServiceMessage extends Message

	case class SampleRequest(org: String, username: String, sql: String, database: Option[String]) extends ServiceMessage
	sealed trait SampleResponse extends ServiceMessage
	case class SampleFailed(message: String) extends SampleResponse
	case class SampleSuccessed(schema: String, data: Seq[Seq[Any]]) extends SampleResponse

	case class VerifyRequest(org: String, username: String, sqls: Seq[String], database: Option[String]) extends ServiceMessage
	case class VerifyResponse(success: Boolean, message: Option[String] = None, result: Option[Seq[(Boolean, Option[String])]] = None) extends ServiceMessage

	case class TranslateRequest(org: String, username: String, sql: String, database: Option[String]) extends ServiceMessage
	case class TranslateResponse(success: Boolean, message: Option[String] = None, sql: Option[String] = None) extends ServiceMessage

	case class TableResourcesRequest(org: String, username: String, sqls: Seq[String], database: Option[String]) extends ServiceMessage
	case class TableResourcesResponses(
		success: Boolean,
		message: Option[String] = None,
		result: Option[Seq[TableResourcesResponse]] = None) extends ServiceMessage

	sealed trait TableResourcesResponse
	case class TableResourcesFailed(message: String) extends TableResourcesResponse
	case class TableResourcesSuccessed(inputTables: Seq[String], outputTable: Option[String], functions: Seq[String]) extends TableResourcesResponse

	case class SchemaRequest(org: String, username: String, sql: String, database: Option[String]) extends ServiceMessage
	sealed trait SchemaResponse extends ServiceMessage
	case class SchemaFailed(message: String) extends SchemaResponse
	case class SchemaSuccessed(schema: String) extends SchemaResponse

	case class LineageRequest(org: String, username: String, sqls: Seq[String], database: Option[String]) extends ServiceMessage
	sealed trait LineageResponse extends ServiceMessage
	case class LineageFailed(message: String) extends LineageResponse
	case class LineageSuccessed(dags: Seq[Dag]) extends LineageResponse

	// management
	sealed trait ManagementMessage extends Message
	case object ClusterInfoRequest extends ManagementMessage
	case class ClusterInfoResponse(cluster: Seq[Seq[String]]) extends ManagementMessage
	case object AppsInfoRequest extends ManagementMessage
	case class AppsInfoResponse(apps: Seq[Seq[String]]) extends ManagementMessage
}
