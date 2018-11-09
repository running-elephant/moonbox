package moonbox.repl.connector

import moonbox.repl.Utils
import moonbox.repl.connector.ConnectionType.ConnectionType
import moonbox.repl.connector.RuntimeMode.RuntimeMode

case class ConnectionState(
                            host: String,
                            port: Int,
                            connectionType: ConnectionType,
                            runtimeMode: RuntimeMode,
                            username: String,
                            token: String,
                            sessionId: String,
                            var timeout: Int, // time unit: ms
                            var fetchSize: Long,
                            var maxColumnLength: Int,
                            var maxRowsShow: Int
                          ) {
  def prettyShow(): Unit = {
    val schema = "field_name" :: "value" :: Nil
    val data = Seq("host" :: host :: Nil,
      "port" :: port :: Nil,
      "connection_type" :: connectionType :: Nil,
      "mode" :: runtimeMode :: Nil,
      "username" :: username :: Nil,
      "token" :: token :: Nil,
      "session_id" :: sessionId :: Nil,
      "timeout" :: s"$timeout ms" :: Nil,
      "fetch_size" :: fetchSize :: Nil,
      "max_col_length" :: maxColumnLength :: Nil,
      "max_rows_show" :: maxRowsShow :: Nil
    )
    print(Utils.showString(data, schema, _numRows = data.size, showPromote = false))
  }
}