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

package moonbox.thriftserver

import java.sql.Connection
import java.util.concurrent.ConcurrentHashMap
import java.util.{Map => JMap}

import moonbox.common.MbLogging
import org.apache.hive.service.cli._
import org.apache.hive.service.cli.operation.{ExecuteStatementOperation, Operation, OperationManager}
import org.apache.hive.service.cli.session.HiveSession

/**
  * Executes queries using Moonbox SQL, and maintains a list of handles to active queries.
  */
class MoonboxOperationManager() extends OperationManager with MbLogging {

  val handleToOperation = ReflectionUtils.getSuperField[JMap[OperationHandle, Operation]](this, "handleToOperation")
  val sessionHandleToMbJdbcConnection = new ConcurrentHashMap[SessionHandle, Connection]()

  @throws[HiveSQLException]
  override def newExecuteStatementOperation(parentSession: HiveSession,
                                            statement: String,
                                            confOverlay: JMap[String, String],
                                            async: Boolean): ExecuteStatementOperation = synchronized {
    val sessionHandle = parentSession.getSessionHandle
    if (sessionHandleToMbJdbcConnection.containsKey(sessionHandle)) {
      val connection = sessionHandleToMbJdbcConnection.get(sessionHandle)
      val operation = new MoonboxExecuteStatementOperation(connection, parentSession, statement, confOverlay, false)
      handleToOperation.put(operation.getHandle, operation)
      logDebug(s"Created Operation for $statement with session=$parentSession")
      operation
    } else throw new HiveSQLException("Unknown Hive sessionHandle.")
  }
}
