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

package moonbox.core.datasys.kudu

import java.math.BigDecimal
import java.sql.Timestamp

import org.apache.kudu.client.KuduPredicate
import org.apache.kudu.client.KuduPredicate.ComparisonOp
import org.apache.kudu.{ColumnSchema, Schema, Type}
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical.{Aggregate, Filter, LogicalPlan, Project}
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources
import org.apache.spark.sql.types.StringType
import org.apache.spark.unsafe.types.UTF8String

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

object ExpressionUtils {

  def findPredicates(plan: LogicalPlan, schema: Schema): (Array[String], Array[KuduPredicate]) = {
    val filters: ArrayBuffer[KuduPredicate] = new ArrayBuffer()
    var cols: Array[String] = Array.empty
    plan.children.foreach { child =>
      val (c, fs) = findPredicates(child, schema)
      cols = c
      filters ++= fs
    }
    plan match {
      case Filter(condition, child) =>
        filters ++= ExpressionUtils.toKuduPredicate(condition, schema)
      case LogicalRelation(_, output, _) => cols = output.map(_.name).toArray
      case Project(projectList, _) =>
        if (projectList.nonEmpty) {
          cols = projectList.map(_.name).toArray
        } else {
          cols = cols.take(1)
        }
      case Aggregate(groupingExpressions, aggregateExpressions, child) => /* no-op, TODO: support aggregate push down */
      case _ => /* no-op */
    }
    (cols, filters.toArray)
  }

  /*def predicate2Filter(predicate: Expression): sources.Filter = {
    predicate match {
      case And(left, right) =>
        sources.And(predicate2Filter(left), predicate2Filter(right))
      case b: BinaryComparison => binaryComparison2Filter(b)
      case In(value, list) =>
        value match {
          case a: AttributeReference =>
            val values = list.map {
              case Literal(v, _) => v
              case other => throw new Exception(s"Invalid value[$other] in list.")
            }
            sources.In(a.name, values.toArray)
          case _ => throw new Exception(s"Invalid Attribute expression: $value")
        }
      case IsNotNull(child) =>
        child match {
          case a: AttributeReference => sources.IsNotNull(a.name)
          case _ => throw new Exception(s"Invalid Attribute expression: $child")
        }
      case IsNull(child) =>
        child match {
          case a: AttributeReference => sources.IsNull(a.name)
          case _ => throw new Exception(s"Invalid Attribute expression: $child")
        }
      /* unsupported temporarily */
      case Or(left, right) => throw new Exception(s"$predicate is temporarily unsupported.")
      case Exists(_, _, _) => throw new Exception(s"$predicate is temporarily unsupported.")
      case InSet(child, hset) => throw new Exception(s"$predicate is temporarily unsupported.")
      case IsNaN(child) => throw new Exception(s"$predicate is temporarily unsupported.")
      case Not(child) => throw new Exception(s"$predicate is temporarily unsupported.")
      case StartsWith(left, right) =>
        (left, right) match {
          case (a: AttributeReference, Literal(v, StringType)) => sources.StringStartsWith(a.name, v.toString)
          case _ => throw new Exception(s"Invalid expression: StartsWith($left, $right)")
        }
      case Contains(left, right) => throw new Exception(s"$predicate is temporarily unsupported.")
      case EndsWith(left, right) => throw new Exception(s"$predicate is temporarily unsupported.")
    }
  }*/

  def toKuduPredicate(condition: Expression, schema: Schema): Seq[KuduPredicate] = {
    if (!condition.isInstanceOf[Predicate]) throw new Exception(s"Invalid predicate expression: $condition")
    condition match {
      case And(left, right) =>
        toKuduPredicate(left, schema) ++ toKuduPredicate(right, schema)
      case b: BinaryComparison => Seq(binaryComparison2KuduPredicate(b, schema))
      case In(value, list) =>
        value match {
          case a: AttributeReference =>
            val columnSchema = schema.getColumn(a.name)
            Seq(buildInListPredicate(columnSchema, list))
          case _ => throw new Exception(s"Invalid Attribute expression: $value")
        }
      case IsNotNull(child) =>
        child match {
          case a: AttributeReference =>
            val columnSchema = schema.getColumn(a.name)
            Seq(KuduPredicate.newIsNotNullPredicate(columnSchema))
          case _ => throw new Exception(s"Invalid Attribute expression: $child")
        }
      case IsNull(child) =>
        child match {
          case a: AttributeReference =>
            val columnSchema = schema.getColumn(a.name)
            Seq(KuduPredicate.newIsNullPredicate(columnSchema))
          case _ => throw new Exception(s"Invalid Attribute expression: $child")
        }
      /* unsupported temporarily */
      case Or(left, right) => throw new Exception(s"$condition is temporarily unsupported.")
      case Exists(_, _, _) => throw new Exception(s"$condition is temporarily unsupported.")
      case InSet(child, hset) => throw new Exception(s"$condition is temporarily unsupported.")
      case IsNaN(child) => throw new Exception(s"$condition is temporarily unsupported.")
      case Not(child) => throw new Exception(s"$condition is temporarily unsupported.")
      case StartsWith(left, right) =>
        (left, right) match {
          case (a: AttributeReference, Literal(v, StringType)) => startsWith2KuduPredicate(a, v.toString, schema)
          //          case (Literal(v, StringType), a: AttributeReference) => startsWith2KuduPredicate(a, v, schema)
          case _ => throw new Exception(s"Invalid expression: StartsWith($left, $right)")
        }
      case Contains(left, right) => throw new Exception(s"$condition is temporarily unsupported.")
      case EndsWith(left, right) => throw new Exception(s"$condition is temporarily unsupported.")
    }
  }

  /**
    * Returns the smallest string s such that, if p is a prefix of t,
    * then t < s, if one exists.
    *
    * @param p the prefix
    * @return Some(the prefix infimum), or None if none exists.
    */
  private def prefixInfimum(p: String): Option[String] = {
    p.reverse.dropWhile(_ == Char.MaxValue).reverse match {
      case "" => None
      case q => Some(q.slice(0, q.length - 1) + (q(q.length - 1) + 1).toChar)
    }
  }

  def startsWith2KuduPredicate(column: Expression, prefix: String, schema: Schema): Seq[KuduPredicate] = {
    prefixInfimum(prefix) match {
      case None => Seq(binaryComparison2KuduPredicate(GreaterThanOrEqual(column, Literal(prefix)), schema))
      case Some(inf) =>
        Seq(binaryComparison2KuduPredicate(GreaterThanOrEqual(column, Literal(prefix)), schema),
          binaryComparison2KuduPredicate(LessThan(column, Literal(inf)), schema))
    }
  }

  def binaryComparison2KuduPredicate(expression: BinaryComparison, schema: Schema): KuduPredicate = {
    val (columnName, value, needReverse) = getColumnNameAndValue(expression.left, expression.right)
    val columnSchema = schema.getColumn(columnName)
    val op: ComparisonOp = expression match {
      case EqualNullSafe(left, right) => throw new Exception(s"$expression is temporarily unsupported.")
      case EqualTo(left, right) => ComparisonOp.EQUAL
      case GreaterThan(left, right) => if (needReverse) ComparisonOp.LESS else ComparisonOp.GREATER
      case GreaterThanOrEqual(left, right) => if (needReverse) ComparisonOp.LESS_EQUAL else ComparisonOp.GREATER_EQUAL
      case LessThan(left, right) => if (needReverse) ComparisonOp.GREATER else ComparisonOp.LESS
      case LessThanOrEqual(left, right) => if (needReverse) ComparisonOp.GREATER_EQUAL else ComparisonOp.LESS_EQUAL
    }
    genComparisonPredicate(columnSchema, op, value)
  }

  /*def binaryComparison2Filter(expression: BinaryComparison): sources.Filter = {
    val (columnName, value, needReverse) = getColumnNameAndValue(expression.left, expression.right)
    expression match {
      case EqualNullSafe(left, right) => throw new Exception(s"$expression is temporarily unsupported.")
      case EqualTo(left, right) => sources.EqualTo(columnName, value)
      case GreaterThan(left, right) => if (needReverse) sources.LessThan(columnName, value) else sources.GreaterThan(columnName, value)
      case GreaterThanOrEqual(left, right) => if (needReverse) sources.LessThanOrEqual(columnName, value) else sources.GreaterThanOrEqual(columnName, value)
      case LessThan(left, right) => if (needReverse) sources.GreaterThan(columnName, value) else sources.LessThan(columnName, value)
      case LessThanOrEqual(left, right) => if (needReverse) sources.GreaterThanOrEqual(columnName, value) else sources.LessThanOrEqual(columnName, value)
    }
  }*/

  def buildInListPredicate(colSchema: ColumnSchema, list: Seq[Expression]): KuduPredicate = {
    val values = list.map {
		case Literal(value, StringType) if value.isInstanceOf[UTF8String] => value.toString
      case Literal(value, _) => value
      case other => throw new Exception(s"Invalid value[$other] in list.")
    }
    KuduPredicate.newInListPredicate(colSchema, values.asJava)
  }

  def genComparisonPredicate(columnSchema: ColumnSchema, operator: ComparisonOp, value: Any): KuduPredicate = {
	  value match {
		  case value: Boolean => KuduPredicate.newComparisonPredicate(columnSchema, operator, value)
		  case value: Byte => KuduPredicate.newComparisonPredicate(columnSchema, operator, value)
		  case value: Short => KuduPredicate.newComparisonPredicate(columnSchema, operator, value)
		  case value: Int => KuduPredicate.newComparisonPredicate(columnSchema, operator, value)
		  case value: Long => KuduPredicate.newComparisonPredicate(columnSchema, operator, value)
		  case value: Timestamp => KuduPredicate.newComparisonPredicate(columnSchema, operator, timestampToMicros(value))
		  case value: Float => KuduPredicate.newComparisonPredicate(columnSchema, operator, value)
		  case value: Double => KuduPredicate.newComparisonPredicate(columnSchema, operator, value)
		  case value: String => KuduPredicate.newComparisonPredicate(columnSchema, operator, value)
		  case value: Array[Byte] => KuduPredicate.newComparisonPredicate(columnSchema, operator, value)
		  case value: BigDecimal => KuduPredicate.newComparisonPredicate(columnSchema, operator, value)
		  case value: UTF8String => KuduPredicate.newComparisonPredicate(columnSchema, operator, value.toString)
	  }
  }

  /**
    *
    * @param left
    * @param right
    * @return (column_name, column_value, if_need_reverse)
    */
  def getColumnNameAndValue(left: Expression, right: Expression): (String, Any, Boolean) = {
    left match {
      case a: AttributeReference =>
        val r = right.asInstanceOf[Literal]
        (a.name, r.value, false)
      case Literal(value, dataType) =>
        val l = left.asInstanceOf[AttributeReference]
        (l.name, value, true)
      case other => throw new Exception(s"$other is neither an attribute reference nor a literal")
    }
  }

  /**
    * Converts a [[Timestamp]] to microseconds since the Unix epoch (1970-01-01T00:00:00Z).
    *
    * @param timestamp the timestamp to convert to microseconds
    * @return the microseconds since the Unix epoch
    */
  def timestampToMicros(timestamp: Timestamp): Long = {
    // Number of whole milliseconds since the Unix epoch, in microseconds.
    val millis = timestamp.getTime * 1000
    // Sub millisecond time since the Unix epoch, in microseconds.
    val micros = (timestamp.getNanos % 1000000) / 1000
    if (micros >= 0) {
      millis + micros
    } else {
      millis + 1000000 + micros
    }
  }

  def microsToTimestamp(micros: Long): Timestamp = {
    var millis = micros / 1000
    var nanos = (micros % 1000000) * 1000
    if (nanos < 0) {
      millis -= 1
      nanos += 1000000000
    }
    val timestamp = new Timestamp(millis)
    timestamp.setNanos(nanos.asInstanceOf[Int])
    timestamp
  }

  /*private def leafExpressionExtract(expr: Expression): Any ={
    expr match {
      case a: AttributeReference => a.name
      case Literal(value, dataType) => ()
    }
  }*/

}
