package org.apache.spark.sql.sqlbuilder

import java.sql.Connection

import org.apache.spark.sql.catalyst.expressions.aggregate.Last
import org.apache.spark.sql.catalyst.expressions.{And, AttributeReference, DayOfMonth, EqualTo, Expression, Hour, Like, Literal, Md5, Minute, Month, Not, ParseToTimestamp, RLike, RegExpExtract, RegExpReplace, Second, StringLocate, ToDate, UnixTimestamp, Year}
import org.apache.spark.sql.catalyst.plans.logical.{Join, Project, SubqueryAlias}
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.execution.datasources.mbjdbc.MbJDBCRelation
import org.apache.spark.sql.types.IntegerType

import scala.collection.mutable


class MbClickHouseDialect extends MbDialect {

  import MbSqlBuilder._

  override def relation(relation: LogicalRelation): String = {
    relation.relation.asInstanceOf[MbJDBCRelation].jdbcOptions.table
  }

  override def canHandle(url: String): Boolean = {
    url.toLowerCase().startsWith("jdbc:clickhouse")
  }

  override def explainSQL(sql: String): String = {
    throw new Exception("Explain SQL for ClickHouse not supported now.")
  }

  override def quote(name: String): String = {
    "`" + name.replace("`", "``") + "`"
  }

  override def maybeQuote(name: String): String = {
    if (name.contains("#") || name.contains("(")) quote(name)
    else name
  }

  override def projectToSQL(p: Project, isDistinct: Boolean, child: String, expression: String): String = {
    val columns = expression.split(",")
    if (!existDuplicateColumn(columns))
      super.projectToSQL(p, isDistinct, child, expression)
    else
      throw new Exception("join tables have duplicate column name, not supported now")
  }

  private def existDuplicateColumn(columns: Array[String]): Boolean = {
    var flag = false
    val set = mutable.HashSet.empty[String]
    for (i <- columns.indices if !flag) {
      if (set.contains(columns(i))) flag = true
      else set.add(columns(i))
    }
    flag
  }

  override def subqueryAliasToSQL(alias: String, child: String): String = {
    build(s"($child)")
  }

  override def joinSQL(p: Join, left: String, right: String, condition: String): String = {
    p.joinType.sql match {
      case "LEFT SEMI" | "LEFT ANTI" =>
        build(left, s"WHERE (${joinConditionToSQL(p.condition.get, false)}) IN", right)
      case _ =>
        build(
          left,
          s"ALL ${p.joinType.sql}",
          "JOIN",
          right,
          s"USING ${joinConditionToSQL(p.condition.get)}")
    }
  }

  override def getAttributeName(e: AttributeReference): String = {
    maybeQuote(e.name)
  }

  private def joinConditionToSQL(p: Expression, joinable: Boolean = true): String = {
    p match {
      case a: And =>
        build(s"${joinConditionToSQL(a.left, joinable)}, ${joinConditionToSQL(a.right, joinable)}")
      case e: EqualTo =>
        val leftColumn = joinConditionToSQL(e.left, joinable)
        val rightColumn = joinConditionToSQL(e.right, joinable)
        if (leftColumn == rightColumn) leftColumn
        else {
          joinable match {
            case false => leftColumn
            case true => throw new Exception("join column should be same")
          }
        }
      case attr: AttributeReference =>
        attr.name
    }
  }

  override def expressionToSQL(e: Expression): String = {
    e match {
      case _: ToDate => "toDate"
      case _: Year => "toYear"
      case _: Month => "toMonth"
      case _: DayOfMonth => "toDayOfMonth"
      case _: Hour => "toHour"
      case _: Minute => "toMinute"
      case _: Second => "toSecond"
      case _: StringLocate => "positionCaseInsensitive"
      case _: RLike => "match"
      case _: RegExpExtract => "extract"
      case _: RegExpReplace => "replaceRegexpAll"
      case _: Md5 => "MD5"
      case _: Last => "anyLast"
    }
  }

  // show create table get primary key
  override def getIndexes(conn: Connection, url: String, tableName: String): Set[String] = {
    //throw new Exception("Get ClickHouse table index not supported now.")
    Set.empty[String]
  }


  override def getTableStat(conn: Connection, url: String, tableName: String): (Option[BigInt], Option[Long]) = {
    //throw new Exception("Get ClickHouse table stat not supported now.")
    (Some(BigInt(0L)), Some(0L))
  }


}
