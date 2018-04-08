package moonbox.catalyst.adapter.jdbc

import moonbox.catalyst.jdbc.CatalystResultSetMetaData

class JdbcResultSetMetaData(map: Map[String, Int]) extends CatalystResultSetMetaData {
    override def getColumnCount = {
        map.size
    }

}
