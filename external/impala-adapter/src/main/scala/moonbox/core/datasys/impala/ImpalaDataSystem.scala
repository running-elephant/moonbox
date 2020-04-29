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

package moonbox.core.datasys.impala

import java.sql.{Connection, DriverManager}
import java.util.Properties

import moonbox.common.MbLogging
import moonbox.core.datasys.{DataSystem, DataTable, Pushdownable}
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ArrayBuffer

class ImpalaDataSystem(props: Map[String, String])
  extends DataSystem(props) with Pushdownable with MbLogging {

  checkOptions("type", "url")

  override val supportedOperators: Seq[Class[_]] = Seq(classOf[Project],
    classOf[Filter],
    classOf[Aggregate],
    classOf[Sort],
    classOf[Join],
    classOf[GlobalLimit],
    classOf[LocalLimit],
    classOf[Subquery],
    classOf[SubqueryAlias])

  override val supportedJoinTypes: Seq[JoinType] = Seq()
  override val supportedExpressions: Seq[Class[_]] = Seq()
  override val beGoodAtOperators: Seq[Class[_]] = Seq()
  override val supportedUDF: Seq[String] = Seq()

  override def tableNames(): Seq[String] = {
    val tables = new ArrayBuffer[String]()
    val connection = getConnection()
    val resultSet = connection.createStatement().executeQuery("show tables")
    while (resultSet.next()) {
      tables.+=:(resultSet.getString(1))
    }
    connection.close()
    tables
  }

  override def tableName(): String = {
    props("dbtable")
  }

  override def tableProperties(tableName: String): Map[String, String] = {
    props.+("dbtable" -> tableName)
  }

  override def isSupportAll: Boolean = ???

  override def fastEquals(other: DataSystem): Boolean = ???

  override def buildScan(plan: LogicalPlan, sparkSession: SparkSession): DataFrame = ???

  override def buildQuery(plan: LogicalPlan, sparkSession: SparkSession): DataTable = ???

  private def getConnection: () => Connection = {
    val p = new Properties()
    props.foreach { case (k, v) => p.put(k, v) }
    ((url: String, props: Properties) => {
      () => {
        Class.forName("com.cloudera.impala.jdbc41.Driver")
        DriverManager.getConnection(url, props)
      }
    }) (props("url"), p)
  }

  override def test(): Unit = {
    var connection: Connection = null
    try {
      connection = getConnection()
    } catch {
      case e: Exception =>
        logError("impala test failed.", e)
        throw e
    } finally {
      if (connection != null) {
        connection.close()
      }
    }
  }

}
