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
package org.apache.spark.sql.jdbc

import java.sql.Types

import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
import org.apache.spark.sql.types._

class ClickHouseDialect extends JdbcDialect {

  println("clickhouse dialect")

  override def canHandle(url: String): Boolean = url.startsWith("jdbc:clickhouse")

  override def quoteIdentifier(colName: String): String = {
    s"`$colName`"
  }

  override def getCatalystType(
                                sqlType: Int, typeName: String, size: Int, md: MetadataBuilder): Option[DataType] = {
    if (sqlType == Types.ARRAY) {
      val scale = md.build.getLong("scale").toInt
      toCatalystType(typeName, size, scale).map(ArrayType(_))
    } else None
  }

  private def toCatalystType(
                              typeName: String,
                              precision: Int,
                              scale: Int): Option[DataType] = typeName match {
    case "Int8" | "UInt8" | "Int16" | "UInt16" | "Int32" | "UInt32" => Some(IntegerType)
    case "Int64" | "UInt64" => Some(LongType)
    case "Float32" => Some(FloatType)
    case "Float64" => Some(DoubleType)
    case "Date" => Some(DateType)
    case "DateTime" => Some(TimestampType)
    case "String" | "FixedString" => Some(StringType)
    case _ => Some(StringType)
  }

  override def getJDBCType(dt: DataType): Option[JdbcType] = dt match {
    case IntegerType => Some(JdbcType("Int32", Types.INTEGER))
    case LongType => Some(JdbcType("Int64", Types.BIGINT))
    case FloatType => Some(JdbcType("Float32", Types.FLOAT))
    case DoubleType => Some(JdbcType("Float64", Types.DOUBLE))
    case DateType => Some(JdbcType("Date", Types.DATE))
    case TimestampType => Some(JdbcType("DateTime", Types.TIMESTAMP))
    case StringType => Some(JdbcType("FixedString", Types.BLOB))
    case ArrayType(et, _) if et.isInstanceOf[AtomicType] =>
      getJDBCType(et).map(_.databaseTypeDefinition)
        .orElse(JdbcUtils.getCommonJDBCType(et).map(_.databaseTypeDefinition))
        .map(_ => JdbcType(s"Array()", java.sql.Types.ARRAY))
    case _ => None
  }
}
