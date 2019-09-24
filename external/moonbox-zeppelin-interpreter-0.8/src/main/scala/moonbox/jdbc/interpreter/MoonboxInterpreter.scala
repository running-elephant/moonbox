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

package moonbox.jdbc.interpreter

import java.io.{ByteArrayOutputStream, PrintStream}
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.sql._
import java.util.Properties
import java.util.concurrent.{ConcurrentHashMap => Jmap}

import org.apache.zeppelin.interpreter.Interpreter.FormType
import org.apache.zeppelin.interpreter.{Interpreter, InterpreterContext, InterpreterResult}
import org.apache.zeppelin.scheduler.SchedulerFactory
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

class MoonboxInterpreter(property: Properties) extends Interpreter(property) {

  val log = LoggerFactory.getLogger(this.getClass)

  val DEFAULT_PREFIX = "default"
  val URL_KEY = "url"
  val USER_KEY = "user"
  val PASSWORD_KEY = "password"
  val MAX_LINE_KEY = "max_count"
  val TIMEOUT_KEY = "timeout"
  val MAX_CONCURRENCY_KEY = "maxConcurrency"

  val DEFAULT_MAX_CONCURRENCY = 10
  var DEFAULT_MAX_RESULT_LINE = 1000
  var timeout = 60 * 30 // time unit: s

  val baseProps = new Properties()
  val idToConnection: Jmap[String, Connection] = new Jmap[String, Connection]()
  val idToStatement: Jmap[String, Statement] = new Jmap[String, Statement]()

  val WHITESPACE = ' '
  val NEWLINE = '\n'
  val TAB = '\t'
  val TABLE_MAGIC_TAG = "%table "

  override def getScheduler = SchedulerFactory.singleton.createOrGetParallelScheduler("interpreter_" + this.hashCode, getMaxConcurrency())

  override def cancel(interpreterContext: InterpreterContext): Unit = {
    val id = interpreterContext.getParagraphId
    if (idToStatement.containsKey(id)) {
      val stat = idToStatement.get(id)
      stat.cancel()
      stat.close()
      idToStatement.remove(id)
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

  private val KEY_SHA = "SHA"
  private val hexDigits = (0 to 9).++('a' to 'f').map(_.toString)

  def encryptSHA(data: String): String = {
    if (data == null || data.equals("")) {
      ""
    } else {
      val sha = MessageDigest.getInstance(KEY_SHA)
      sha.update(data.getBytes)
      byteArrayToHexString(sha.digest()).toUpperCase
    }
  }

  private def byteArrayToHexString(bytes: scala.Array[Byte]): String = {
    bytes.map(byteToHexString).reduce(_ + _)
  }

  private def byteToHexString(byte: Byte): String = {
    val res = if (byte < 0) byte + 256
    else byte
    hexDigits(res / 16) + hexDigits(res % 16)
  }

  override def interpret(s: String, interpreterContext: InterpreterContext): InterpreterResult = {
    val loginUser = interpreterContext.getAuthenticationInfo.getUser
    if (loginUser.endsWith("@creditease.cn")) {
      val password = encryptSHA(loginUser.stripSuffix("@creditease.cn"))
      val moonboxUser = baseProps.getProperty("user").split("@").head + "@" + loginUser.stripSuffix("@creditease.cn")
      baseProps.setProperty("user", moonboxUser)
      log.info(s"replace login user $loginUser to $moonboxUser")
      baseProps.setProperty("password", password)
    }
    var interpreterResult: InterpreterResult = new InterpreterResult(InterpreterResult.Code.SUCCESS)
    var statement: Statement = null
    var resultSet: ResultSet = null
    val paragraphId = interpreterContext.getParagraphId
    try {
      val url = baseProps.getProperty(URL_KEY)
      log.info("Getting connection ...")
      val connection: Connection = {
        Option(idToConnection.get(paragraphId)) match {
          case Some(conn) =>
            if (!conn.isClosed) {
              conn
            } else {
              interpreterResult.add(InterpreterResult.Type.TEXT, s"WARNING: Connection timeout, using a new connection.$NEWLINE")
              DriverManager.getConnection(url, baseProps)
            }
          case None => DriverManager.getConnection(url, baseProps)
        }
      }
      if (connection == null) {
        interpreterResult = new InterpreterResult(InterpreterResult.Code.ERROR, "Getting connection error")
        throw new SQLException("Getting connection error")
      }
      idToConnection.put(paragraphId, connection)
      log.info("Creating statement ...")
      statement = connection.createStatement()
      if (statement == null) {
        interpreterResult = new InterpreterResult(InterpreterResult.Code.ERROR, "Creating statement error")
        throw new SQLException("Creating statement error")
      }
      idToStatement.put(paragraphId, statement)
      try {
        statement.setQueryTimeout(getQueryTimeout())
        statement.setMaxRows(getMaxResultLine())
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
          idToStatement.remove(paragraphId)
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
      baseProps.setProperty(key.stripPrefix(DEFAULT_PREFIX + "."), this.property.getProperty(key))
    }
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
    if (max != null) {
      max.toInt
    } else {
      DEFAULT_MAX_RESULT_LINE
    }
  }

  def getMaxConcurrency(): Int = {
    val max = baseProps.getProperty(MAX_CONCURRENCY_KEY)
    if (max != null) {
      max.toInt
    } else {
      DEFAULT_MAX_CONCURRENCY
    }
  }

  private def replaceReservedChars(str: String): String = {
    if (str != null)
      str.replace(TAB, WHITESPACE).replace(NEWLINE, WHITESPACE)
    else
      ""
  }
}
