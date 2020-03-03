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

package moonbox.protocol.util

import java.util

import com.google.protobuf.Int32Value
import moonbox.message.protobuf._

object ProtoInboundMessageBuilder {

	def loginInbound(username: String, password: String): LoginInbound = {
		LoginInbound.newBuilder()
			.setUsername(username)
			.setPassword(password)
			.build()
	}

	def logoutInbound(token: String): LogoutInbound = {
		LogoutInbound.newBuilder()
			.setToken(token)
			.build()
	}

	def openSessionInbound(token: String, database: String, isLocal: Boolean, config: util.Map[String, String]): OpenSessionInbound = {
		OpenSessionInbound.newBuilder()
			.setToken(token)
			.setDatabase(database)
			.putAllConfig(config)
			.build()
	}

	def closeSessionInbound(token: String, sessionId: String): CloseSessionInbound = {
		val builder = CloseSessionInbound.newBuilder()
		if (sessionId != null) {
			builder.setSessionId(sessionId)
		} else if (token != null) {
			builder.setToken(token)
		}
		builder.build()
	}

	def interactiveQueryInbound(token: String, sessionId: String, sqls: util.List[String], fetchSize: Option[Int], maxRows: Option[Int]): InteractiveQueryInbound = {
		val builder = InteractiveQueryInbound.newBuilder()
			.setToken(token)
			.setSessionId(sessionId)
			.addAllSql(sqls)
		fetchSize.foreach(value => builder.setFetchSize(Int32Value.newBuilder().setValue(value).build()))
		maxRows.foreach(value => builder.setMaxRows(Int32Value.newBuilder().setValue(value).build()))
		builder.build()
	}

	def interactiveNextResultInbound(token: String, sessionId: String): InteractiveNextResultInbound = {
		val builder = InteractiveNextResultInbound.newBuilder()
		Option(token).foreach(builder.setToken)
		Option(sessionId).foreach(builder.setSessionId)
		builder.build()
	}

	def batchQueryInbound(username: String, password: String, sqls: util.List[String], config: util.Map[String, String]): BatchQueryInbound = {
		BatchQueryInbound.newBuilder()
			.setUsername(username)
			.setPassword(password)
			.addAllSql(sqls)
			.putAllConfig(config)
			.build()
	}

	def batchQueryProgressInbound(username: String, password: String, jobId: String): BatchQueryProgressInbound = {
		BatchQueryProgressInbound.newBuilder()
			.setUsername(username)
			.setPassword(password)
			.setJobId(jobId)
			.build()
	}

	def interactiveQueryCancelInbound(token: String, sessionId: String): InteractiveQueryCancelInbound = {
		InteractiveQueryCancelInbound.newBuilder()
			.setToken(token)
			.setSessionId(sessionId)
			.build()
	}

	def batchQueryCancelInbound(username: String, password: String, jobId: String): BatchQueryCancelInbound = {
		BatchQueryCancelInbound.newBuilder()
			.setUsername(username)
			.setPassword(password)
			.setJobId(jobId)
			.build()
	}

}
