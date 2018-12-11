package moonbox.protocol.util

import java.util

import com.google.protobuf.{Int32Value, Int64Value}
import moonbox.message.protobuf._

object ProtoInboundMessageBuilder {

  def loginInbound(username: String, password: String): LoginInbound = {
    LoginInbound.newBuilder()
      .setUesername(username)
      .setPassword(password)
      .build()
  }
  def logoutInbound(token: String): LogoutInbound = {
    LogoutInbound.newBuilder()
      .setToken(token)
      .build()
  }
  def openSessionInbound(token: String, database: String, isLocal: Boolean): OpenSessionInbound = {
    OpenSessionInbound.newBuilder()
      .setToken(token)
      .setDatabase(database)
      .setIsLocal(isLocal)
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

  def interactiveQueryInbound(token: String, sessionId: String, sqls: util.List[String], fetchSize: Option[Int], maxRows: Option[Long]): InteractiveQueryInbound = {
    val builder = InteractiveQueryInbound.newBuilder()
      .setToken(token)
      .setSessionId(sessionId)
      .addAllSql(sqls)
    fetchSize.foreach(value => builder.setFetchSize(Int32Value.newBuilder().setValue(value).build()))
    maxRows.foreach(value => builder.setMaxRows(Int64Value.newBuilder().setValue(value).build()))
    builder.build()
  }

  def interactiveNextResultInbound(token: String, sessionId: String): InteractiveNextResultInbound = {
    val builder = InteractiveNextResultInbound.newBuilder()
    Option(token).foreach(builder.setToken)
    Option(sessionId).foreach(builder.setSessionId)
    builder.build()
  }

  def batchQueryInbound(token: String, sqls: util.List[String], config: String): BatchQueryInbound = {
    BatchQueryInbound.newBuilder()
      .setToken(token)
      .addAllSql(sqls)
      .setConfig(config)
      .build()
  }

  def batchQueryProgressInbound(token: String, jobId: String): BatchQueryProgressInbound = {
    BatchQueryProgressInbound.newBuilder()
      .setToken(token)
      .setJobId(jobId)
      .build()
  }

  def cancelQueryInbound(token: String, jobId: String, sessionId: String): CancelQueryInbound = {
    val builder = CancelQueryInbound.newBuilder()
      .setToken(token)
    if (sessionId != null) {
      builder.setSessionId(sessionId)
    } else if (jobId != null) {
      builder.setJobId(jobId)
    }
    builder.build()
  }

}
