package moonbox.jdbc.interpreter

import java.io.{ByteArrayOutputStream, PrintStream}
import java.nio.charset.StandardCharsets
import java.sql._
import java.util.Properties
import java.util.concurrent.{ConcurrentHashMap => Jmap}

import org.apache.zeppelin.interpreter.Interpreter.FormType
import org.apache.zeppelin.interpreter.{Interpreter, InterpreterContext, InterpreterResult}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

class MoonboxInterpreter(property: Properties) extends Interpreter(property) {

  val log = LoggerFactory.getLogger(this.getClass)

  val DEFAULT_PRIFIX = "default"
  val URL_KEY = "url"
  val USER_KEY = "user"
  val PASSWORD_KEY = "password"
  val MAX_LINE_KEY = "max_count"
  val TIMEOUT_KEY = "timeout"

  var maxResultsLine = 1000
  var timeout = 60 * 30 // time unit: s

  var pool: ConnectionPool = _
  val baseProps = new Properties()
  val idToConnection: Jmap[String, Connection] = new Jmap[String, Connection]()
  val idToStatement: Jmap[String, Statement] = new Jmap[String, Statement]()

  val WHITESPACE = ' '
  val NEWLINE = '\n'
  val TAB = '\t'
  val TABLE_MAGIC_TAG = "%table "
  val EXPLAIN_PREDICATE = "EXPLAIN "

  override def cancel(interpreterContext: InterpreterContext): Unit = {
    val id = interpreterContext.getParagraphId
    if (idToStatement.containsKey(id)) {
      val stat = idToStatement.get(id)
      stat.cancel()
      stat.close()
    }
  }

  override def getFormType: Interpreter.FormType = FormType.SIMPLE

  override def getProgress(interpreterContext: InterpreterContext): Int = 0

  override def close(): Unit = {
    log.info("Closing statement")
    idToStatement.asScala.foreach(_._2.close())
    idToStatement.clear()
    log.info("Closing connection")
    idToConnection.asScala.foreach(_._2.close())
    idToConnection.clear()
  }

  override def interpret(s: String, interpreterContext: InterpreterContext): InterpreterResult = {
    var interpreterResult: InterpreterResult = new InterpreterResult(InterpreterResult.Code.SUCCESS)
    var connection: Connection = null
    var statement: Statement = null
    var resultSet: ResultSet = null
    try {
      val url = baseProps.getProperty(URL_KEY)
      log.info("Getting connection ...")
      connection = {
        var c = pool.getConnection
        if (c == null) {
          c = DriverManager.getConnection(url, baseProps)
        }
        c
      }
      //      connection = DriverManager.getConnection(url, baseProps)
      if (connection == null) {
        interpreterResult = new InterpreterResult(InterpreterResult.Code.ERROR, "Getting connection error")
        throw new SQLException("Getting connection error")
      }
      idToConnection.put(interpreterContext.getParagraphId, connection)
      log.info("Creating statement ...")
      statement = connection.createStatement()
      if (statement == null) {
        interpreterResult = new InterpreterResult(InterpreterResult.Code.ERROR, "Creating statement error")
        throw new SQLException("Creating statement error")
      }
      idToStatement.put(interpreterContext.getParagraphId, statement)
      try {
        statement.setQueryTimeout(getQueryTimeout())
        if (statement.execute(s)) {
          resultSet = statement.getResultSet
          log.info("Interpreting the resultSet ...")
          interpreterResult.add(getResultString(resultSet, true))
        } else {
          log.info("Query succeed, no resultSet")
          interpreterResult.add(InterpreterResult.Type.TEXT, "Query executed successfully.")
        }
      } finally {
        if (resultSet != null)
          resultSet.close()
        if (statement != null) {
          statement.close()
          idToStatement.remove(interpreterContext.getParagraphId)
        }
        if (connection != null) {
          connection.close()
          idToConnection.remove(interpreterContext.getParagraphId)
        }
      }
    } catch {
      case e: Exception =>
        log.error(s"$s execute error: ${e}")
        val os = new ByteArrayOutputStream()
        val ps = new PrintStream(os)
        e.printStackTrace(ps)
        val errorMsg = new String(os.toByteArray, StandardCharsets.UTF_8)
        interpreterResult = new InterpreterResult(InterpreterResult.Code.ERROR, errorMsg)
    }
    interpreterResult
  }

  override def open(): Unit = {
    log.debug("Properties: " + property.toString)
    property.stringPropertyNames().asScala.map { key =>
      baseProps.setProperty(key.stripPrefix(DEFAULT_PRIFIX + "."), this.property.getProperty(key))
    }
    initPool(baseProps)
  }

  private def initPool(props: Properties): Unit = {
    if (pool == null) {
      synchronized {
        if (pool == null) {
          pool = new ConnectionPoolImpl(props)
        }
      }
    }
  }

  private def getDefaultKey(key: String): String = {
    DEFAULT_PRIFIX + "." + key
  }

  private def getResultString(resultSet: ResultSet, isTableType: Boolean): String = {
    val md = resultSet.getMetaData
    val msg = if (isTableType) new StringBuilder(TABLE_MAGIC_TAG) else new StringBuilder()
    for (i <- 1 to md.getColumnCount) {
      msg.append(replaceReservedChars(md.getColumnName(i)))
      if (i != md.getColumnCount)
        msg.append(TAB)
    }
    msg.append(NEWLINE)
    var rowCount = 0
    while (resultSet.next() && rowCount < getMaxResultLine()) {
      for (i <- 1 to md.getColumnCount) {
        val col = resultSet.getObject(i)
        val value = if (col == null) "null" else col.toString
        msg.append(replaceReservedChars(value))
        if (i != md.getColumnCount)
          msg.append(TAB)
      }
      msg.append(NEWLINE)
      rowCount += 1
    }
    msg.toString()
  }

  private def getQueryTimeout(): Int = {
    val _timeout = baseProps.getProperty(TIMEOUT_KEY) // time unit: s
    if (_timeout != null)
      timeout = _timeout.toInt
    timeout
  }

  private def getMaxResultLine(): Int = {
    val max = baseProps.getProperty(MAX_LINE_KEY)
    if (max != null)
      maxResultsLine = max.toInt
    maxResultsLine
  }

  private def replaceReservedChars(str: String): String = {
    if (str != null)
      str.replace(TAB, WHITESPACE).replace(NEWLINE, WHITESPACE)
    else
      ""
  }
}
