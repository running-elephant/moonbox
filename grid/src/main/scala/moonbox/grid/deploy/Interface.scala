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

trait Interface

object Interface extends Interface {
	sealed trait Inbound extends Interface
	sealed trait Outbound extends Interface

	case class LoginInbound(username: String, password: String) extends Inbound
	case class LoginOutbound(token: Option[String] = None, error: Option[String] = None) extends Outbound

	case class LogoutInbound(token: String) extends Inbound
	case class LogoutOutbound(error: Option[String]) extends Outbound

	case class OpenSessionInbound(token: String, database: Option[String], config: Map[String, String]) extends Inbound
	case class OpenSessionOutbound(sessionId: Option[String] = None, workerHost: Option[String] = None, workerPort: Option[Int] = None, error: Option[String] = None) extends Outbound

	case class CloseSessionInbound(token: String, sessionId: String) extends Inbound
	case class CloseSessionOutbound(error: Option[String]) extends Outbound

	case class ResultData(cursor: String, schema: String, data: Seq[Seq[Any]], hasNext: Boolean)

	case class InteractiveQueryInbound(
		token: String,
		sessionId: String,
		sqls: Seq[String],
		fetchSize: Int = 1000,
		maxRows: Int = 10000) extends Inbound
	case class InteractiveQueryOutbound(
		error: Option[String] = None,
		data: Option[ResultData] = None) extends Outbound

	case class InteractiveNextResultInbound(token: String, sessionId: String) extends Inbound

	case class InteractiveNextResultOutbound(
		error: Option[String] = None,
		data: Option[ResultData] = None) extends Outbound

	// batch mode
	// support cluster runtime engine only
	// support asynchronous
	case class BatchQueryInbound(username: String, password: String, lang: String, sqls: Seq[String], config: Map[String, String]) extends Inbound
	case class BatchQueryOutbound(
		jobId: Option[String] = None,
		error: Option[String] = None) extends Outbound

	case class BatchQueryProgressInbound(username: String, password: String, jobId: String) extends Inbound
	case class BatchQueryProgressOutbound(
		message: String,
		state: Option[String]) extends Outbound

	// interactive and batch
	case class BatchQueryCancelInbound(username: String, password: String, jobId: String) extends Inbound
	case class InteractiveQueryCancelInbound(token: String, sessionId: String) extends Inbound
	case class CancelQueryOutbound(error: Option[String] = None) extends Outbound


	// service
	case class SampleInbound(username: String, password: String, sql: String, database: Option[String]) extends Inbound
	case class SampleOutbound(success: Boolean, schema: Option[String] = None, data: Option[Seq[Seq[Any]]] = None, message: Option[String] = None) extends Outbound

	case class TranslationInbound(username: String, password: String, sql: String, database: Option[String]) extends Inbound
	case class TranslationOutbound(success: Boolean, message: Option[String] = None, sql: Option[String] = None) extends Outbound

	case class VerifyInbound(username: String, password: String, sqls: Seq[String], database: Option[String]) extends Inbound
	case class VerifyOutbound(success: Boolean, message: Option[String] = None, result: Option[Seq[VerifyResult]] = None) extends Outbound
	case class VerifyResult(success: Boolean, message: Option[String])

	case class SchemaInbound(username: String, password: String, sql: String, database: Option[String]) extends Inbound
	case class SchemaOutbound(success: Boolean, schema: Option[String] = None, message: Option[String] = None) extends Outbound

	case class TableResourceInbound(username: String, password: String, sqls: Seq[String], database: Option[String]) extends Inbound
	case class TableResourceOutbound(success: Boolean, message: Option[String] = None, result: Option[Seq[ResourceResult]] = None) extends Outbound

	case class ResourceResult(success: Boolean, message: Option[String] = None, inputTables: Option[Seq[String]] = None, outputTable: Option[String] = None, functions: Option[Seq[String]] = None)

	case class LineageInbound(username: String, password: String, sqls: Seq[String], database: Option[String]) extends Inbound
	case class Dag(dag_table: String, dag_col: String)
	case class LineageOutbound(success: Boolean, dags: Option[Seq[Dag]] = None, message: Option[String] = None) extends Outbound

	// management
	case object ClusterInfoInbound extends Inbound
	case class ClusterInfoOutbound(cluster: Seq[Seq[String]]) extends Outbound

	case object AppsInfoInbound extends Inbound
	case class AppsInfoOutbound(apps: Seq[Seq[String]]) extends Outbound
}


