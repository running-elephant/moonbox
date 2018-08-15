package moonbox.jdbc.interpreter

import java.sql.Connection
import java.util.Properties

trait ConnectionPool {

  val property: Properties

  def getConnection: Connection

}
