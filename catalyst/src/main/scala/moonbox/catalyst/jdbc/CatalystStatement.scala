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

package moonbox.catalyst.jdbc

import java.sql._
import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.EsCatalystQueryExecutor
import moonbox.catalyst.adapter.elasticsearch5.util.EsUtil
import moonbox.catalyst.adapter.mongo.MongoCatalystQueryExecutor
import moonbox.catalyst.adapter.mongo.util.MongoJDBCUtils
import moonbox.catalyst.core.parser.SqlParser
import moonbox.catalyst.jdbc.JDBCUtils._
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types.StructType

class CatalystStatement(url: String, props: Properties) extends Statement {
  var maxFieldSize: Int = _

  @throws[SQLException]
  override def executeQuery(sql: String): ResultSet = {
    val newProps = JDBCUtils.parseURL(url, props)
    val parser = new SqlParser()
    if (url.startsWith(MONGO_URL_PREFIX)) {
      val executor = new MongoCatalystQueryExecutor(newProps)
      val schema = executor.getTableSchema
      val tableName = Option(newProps.getProperty(MongoJDBCUtils.COLLECTION_KEY)).getOrElse(newProps.getProperty("collection"))
      parser.registerTable(tableName, schema, "mongo")
      //executor.adaptorFunctionRegister(parser.getRegister)
      val plan = parser.parse(sql)
      val (iter, index2SqlType, columnLabel2Index) = executor.execute4Jdbc(plan)
      maxFieldSize = index2SqlType.size
      new CatalystResultSet(iter, index2SqlType, columnLabel2Index)
    } else if (url.startsWith(ES_URL_PREFIX)) {
      val executor = new EsCatalystQueryExecutor(newProps)
      val schema = executor.getTableSchema
      val tableName = newProps.getProperty(EsUtil.DB_NAME)  //database
      parser.registerTable(tableName, schema, "es")
      //executor.adaptorFunctionRegister(parser.getRegister)
      val plan = parser.parse(sql)
      val (iter, index2SqlType, columnLabel2Index) = executor.execute4Jdbc(plan)
      maxFieldSize = index2SqlType.size
      new CatalystResultSet(iter, index2SqlType, columnLabel2Index)
    } else {
      // invoke executor's execute method by reflection
      val executorPath = newProps.getProperty(EXECUTOR_KEY)
      val clazz = Class.forName(executorPath)
      val constructor = clazz.getConstructor(classOf[Properties])
      val obj = constructor.newInstance(newProps)
      val schema = clazz.getDeclaredMethod("getTableSchema").invoke(obj)
      val tableName = newProps.getProperty(JDBCUtils.TABLE_KEY)
      parser.registerTable(tableName, schema.asInstanceOf[StructType], clazz.getSimpleName.stripSuffix("$"))
      //val register = parser.getRegister
      //clazz.getDeclaredMethod("adaptorFunctionRegister", classOf[UDFRegistration]).invoke(obj, register)
      val plan = parser.parse(sql)
      val (iter, index2SqlType, columnLabel2Index) = clazz.getDeclaredMethod("execute4Jdbc", classOf[LogicalPlan]).invoke(obj, plan)
      maxFieldSize = index2SqlType.asInstanceOf[Map[Int, Int]].size
      new CatalystResultSet(iter.asInstanceOf[Iterator[JdbcRow]], index2SqlType.asInstanceOf[Map[Int, Int]], columnLabel2Index.asInstanceOf[Map[String, Int]])
    }
  }

  @throws[SQLException]
  override def executeUpdate(sql: String) = 0

  @throws[SQLException]
  override def close() = {
  }

  @throws[SQLException]
  override def getMaxFieldSize = maxFieldSize

  @throws[SQLException]
  override def setMaxFieldSize(max: Int) = {
    this.maxFieldSize = max
  }

  @throws[SQLException]
  override def getMaxRows = 0

  @throws[SQLException]
  override def setMaxRows(max: Int) = {
  }

  @throws[SQLException]
  override def setEscapeProcessing(enable: Boolean) = {
  }

  @throws[SQLException]
  override def getQueryTimeout = 0

  @throws[SQLException]
  override def setQueryTimeout(seconds: Int) = {
  }

  @throws[SQLException]
  override def cancel() = {
  }

  @throws[SQLException]
  override def getWarnings = null

  @throws[SQLException]
  override def clearWarnings() = {
  }

  @throws[SQLException]
  override def setCursorName(name: String) = {
  }

  @throws[SQLException]
  override def execute(sql: String) = false

  @throws[SQLException]
  override def getResultSet = null

  @throws[SQLException]
  override def getUpdateCount = 0

  @throws[SQLException]
  override def getMoreResults = false

  @throws[SQLException]
  override def setFetchDirection(direction: Int) = {
  }

  @throws[SQLException]
  override def getFetchDirection = 0

  @throws[SQLException]
  override def setFetchSize(rows: Int) = {
  }

  @throws[SQLException]
  override def getFetchSize = 0

  @throws[SQLException]
  override def getResultSetConcurrency = 0

  @throws[SQLException]
  override def getResultSetType = 0

  @throws[SQLException]
  override def addBatch(sql: String) = {
  }

  @throws[SQLException]
  override def clearBatch() = {
  }

  @throws[SQLException]
  override def executeBatch = null

  @throws[SQLException]
  override def getConnection = null

  @throws[SQLException]
  override def getMoreResults(current: Int) = false

  @throws[SQLException]
  override def getGeneratedKeys = null

  @throws[SQLException]
  override def executeUpdate(sql: String, autoGeneratedKeys: Int) = 0

  @throws[SQLException]
  override def executeUpdate(sql: String, columnIndexes: scala.Array[Int]) = 0

  @throws[SQLException]
  override def executeUpdate(sql: String, columnNames: scala.Array[String]) = 0

  @throws[SQLException]
  override def execute(sql: String, autoGeneratedKeys: Int) = false

  @throws[SQLException]
  override def execute(sql: String, columnIndexes: scala.Array[Int]) = false

  @throws[SQLException]
  override def execute(sql: String, columnNames: scala.Array[String]) = false

  @throws[SQLException]
  override def getResultSetHoldability = 0

  @throws[SQLException]
  override def isClosed = false

  @throws[SQLException]
  override def setPoolable(poolable: Boolean) = {
  }

  @throws[SQLException]
  override def isPoolable = false

  @throws[SQLException]
  override def closeOnCompletion() = {
  }

  @throws[SQLException]
  override def isCloseOnCompletion = false

  @throws[SQLException]
  override def unwrap[T](iface: Class[T]) = null.asInstanceOf[T]

  @throws[SQLException]
  override def isWrapperFor(iface: Class[_]) = false
}
