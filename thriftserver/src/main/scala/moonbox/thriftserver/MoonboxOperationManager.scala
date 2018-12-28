package moonbox.thriftserver

import java.util.concurrent.ConcurrentHashMap
import java.util.{Map => JMap}

import moonbox.client.MoonboxClient
import moonbox.common.MbLogging
import org.apache.hive.service.cli._
import org.apache.hive.service.cli.operation.{ExecuteStatementOperation, Operation, OperationManager}
import org.apache.hive.service.cli.session.HiveSession

/**
  * Executes queries using Moonbox SQL, and maintains a list of handles to active queries.
  */
class MoonboxOperationManager() extends OperationManager with MbLogging {

  val handleToOperation = ReflectionUtils.getSuperField[JMap[OperationHandle, Operation]](this, "handleToOperation")
  val sessionHandleToMbClient = new ConcurrentHashMap[SessionHandle, MoonboxClient]()

  @throws[HiveSQLException]
  override def newExecuteStatementOperation(parentSession: HiveSession,
                                            statement: String,
                                            confOverlay: JMap[String, String],
                                            async: Boolean): ExecuteStatementOperation = synchronized {
    val sessionHandle = parentSession.getSessionHandle
    if (sessionHandleToMbClient.containsKey(sessionHandle)) {
      val client = sessionHandleToMbClient.get(sessionHandle)
      val operation = new MoonboxExecuteStatementOperation(client, parentSession, statement, confOverlay, false)
      handleToOperation.put(operation.getHandle, operation)
      logDebug(s"Created Operation for $statement with session=$parentSession")
      operation
    } else throw new HiveSQLException("Unknown Hive sessionHandle.")
  }
}
