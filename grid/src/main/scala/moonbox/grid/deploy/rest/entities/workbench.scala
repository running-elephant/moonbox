package moonbox.grid.deploy.rest.entities

case class WorkbenchUser(user: String, password: String)

case class ExecuteRequest(consoleId: String, sql: String, props: Map[String, String])

case class ExecuteResponse(result: Option[ExecuteResult] = None, info: String)

case class ExecuteResult(columns: List[String], data: List[Map[String, Any]], size: Int)

case class ReconnectRequest(props: Map[String, String])

case class CancelRequest(consoleId: String)

case class Query(name: String, text: String, description: Option[String] = None)
