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

package moonbox.catalyst.adapter.util

import java.sql.{Date, Timestamp}

import moonbox.catalyst.jdbc.JdbcRow
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.{Add, Alias, And, Attribute, AttributeReference, BinaryArithmetic, CaseWhenCodegen, Cast, Divide, EqualTo, Expression, GetArrayStructFields, GetStructField, GreaterThan, GreaterThanOrEqual, In, IsNotNull, IsNull, LessThan, LessThanOrEqual, Literal, Multiply, Or, Round, Substring, Subtract}
import org.apache.spark.sql.catalyst.util.DateTimeUtils
import org.apache.spark.sql.types._
import org.apache.spark.unsafe.types.UTF8String

import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex


case class FieldName(name: String, isLiteral: Boolean, inScript: Boolean = false)

object SparkUtil {
	/*
	inScript: if string should in script, use doc['value'] else use value
	 */
	def parseLeafExpression(e: Expression, inScript: Boolean = false): FieldName = {
		e match {
			case a: AttributeReference =>
				//val qualifierPrefix = a.qualifier.map(_ + ".").getOrElse("")
				if (inScript) {
					FieldName(s"doc['${a.name}'].value", false, false)
				}
				else {
					FieldName(s"${a.name}", false, false)
				}

			case literal@Literal(v, t) =>
				FieldName(literalToSQL(v, t), true, false)

			case Alias(exp, alias) =>
				val value = parseLeafExpression(exp, inScript)
				FieldName(s"""${value.name}""", false, false)

			case g@GetStructField(child, ordinal, _) => //select user.name from nest_table
				val childSchema = child.dataType.asInstanceOf[StructType]
				val fieldName = childSchema(ordinal).name
				val fieldType = childSchema(ordinal).dataType
				val childName = child match {
					case a: AttributeReference => a.name
					case e: Expression => e.toString()
				}
				FieldName(s"$childName.${g.name.getOrElse(fieldName)}", false, false)

			case g@GetArrayStructFields(child, field, _, _, _) =>
				val parentName = parseLeafExpression(child)
				FieldName(s"""${parentName.name}.${field.name}""", false, false)

			case s@Substring(str, pos, len) =>
				val field = parseLeafExpression(str, true).name //should in doc script
			val start = parseLeafExpression(pos, inScript).name
				val length = parseLeafExpression(len, inScript).name
				FieldName(s"""${field}.substring($start,$length)""", false, true)

			case Round(child, scale) =>
				def pow(m: Int, n: Int): Int = {
					var result: Int = 1
					for (i <- 0 until n) {
						result = result * m
					}
					result
				}
				val exponent = parseLeafExpression(scale).name
				val number = pow(10, java.lang.Integer.valueOf(exponent).intValue())
				FieldName(s"""Math.round(${parseLeafExpression(child, true).name}) * $number / $number.0""", false, true)

			case Cast(child, dataType, _) =>
				val param = parseLeafExpression(child, inScript)
				FieldName(s"""${scriptTypeConvert(dataType, param.name, inScript)}""", param.isLiteral, param.inScript) // may in or not in script

			case c@CaseWhenCodegen(branches, elseValue) =>
				val ifPart = s"""if(${parseLeafExpression(branches(0)._1, true).name})""" //should in doc script
			val returnPart = s"{return ${parseLeafExpression(branches(0)._2, true).name};}" //should in doc script
			val elsePart = if (elseValue.isDefined) {
					s"""else {return ${parseLeafExpression(elseValue.get, true).name};}""" //should in doc script
				} else ""
				FieldName(s"""$ifPart $returnPart $elsePart""", false, true)

			case b: BinaryArithmetic =>
				def mkBinaryString(lchild: Expression, rchild: Expression, op: String): String = {
					val left = parseLeafExpression(lchild, inScript).name
					val right = parseLeafExpression(rchild, inScript).name
					s"""($left $op $right)"""
				}

				val colContext = b match {
					case a@Add(lchild, rchild) => mkBinaryString(lchild, rchild, "+")
					case s@Subtract(lchild, rchild) => mkBinaryString(lchild, rchild, "-")
					case m@Multiply(lchild, rchild) => mkBinaryString(lchild, rchild, "*")
					case d@Divide(lchild, rchild) => mkBinaryString(lchild, rchild, "/")
				}
				FieldName(s"""$colContext""", false, true)

			case GreaterThan(left: Expression, right: Expression) =>
				if (left.dataType == StringType) {
					FieldName(s"""${parseLeafExpression(left, inScript).name}.compareTo(${parseLeafExpression(right, inScript).name}) > 0""", false, false)
				} else {
					FieldName(s"""${parseLeafExpression(left, inScript).name} > ${parseLeafExpression(right, inScript).name}""", false, false)
				}
			case EqualTo(left: Expression, right: Expression) =>
				if (left.dataType == StringType) {
					FieldName(s"""${parseLeafExpression(left, inScript).name}.compareTo(${parseLeafExpression(right, inScript).name}) == 0""", false, false)
				} else {
					FieldName(s"""${parseLeafExpression(left, inScript).name} == ${parseLeafExpression(right, inScript).name}""", false, false)
				}
			case GreaterThanOrEqual(left: Expression, right: Expression) =>
				if (left.dataType == StringType) {
					FieldName(s"""${parseLeafExpression(left, inScript).name}.compareTo(${parseLeafExpression(right, inScript).name}) >= 0""", false, false)
				} else {
					FieldName(s"""${parseLeafExpression(left, inScript).name} >= ${parseLeafExpression(right, inScript).name}""", false, false)
				}
			case LessThan(left: Expression, right: Expression) =>
				if (left.dataType == StringType) {
					FieldName(s"""${parseLeafExpression(left, inScript).name}.compareTo(${parseLeafExpression(right, inScript).name}) < 0""", false, false)
				} else {
					FieldName(s"""${parseLeafExpression(left, inScript).name} < ${parseLeafExpression(right, inScript).name}""", false, false)
				}
			case LessThanOrEqual(left: Expression, right: Expression) =>
				if (left.dataType == StringType) {
					FieldName(s"""${parseLeafExpression(left, inScript).name}.compareTo(${parseLeafExpression(right, inScript).name}) <= 0""", false, false)
				} else {
					FieldName(s"""${parseLeafExpression(left, inScript).name} <= ${parseLeafExpression(right, inScript).name}""", false, false)
				}
			case IsNotNull(child: Expression) => FieldName(s"""${parseLeafExpression(child, inScript).name} != null""", false, false)
			case And(left, right) => FieldName(s"""(${parseLeafExpression(left, inScript).name}) && (${parseLeafExpression(right, inScript).name})""", false, false)
			case Or(left, right) => FieldName(s"""(${parseLeafExpression(left, inScript).name}) || (${parseLeafExpression(right, inScript).name})""", false, false)
			case In(value: Expression, list: Seq[Expression]) =>
				val name = list.map { expression =>
					if (value.dataType == StringType) {
						s"""${parseLeafExpression(expression, inScript).name}.compareTo(${parseLeafExpression(value, inScript).name}) == 0"""
					} else {
						s"""${parseLeafExpression(expression, inScript).name} == ${parseLeafExpression(value, inScript).name}"""
					}
				}.mkString("||")
				FieldName(name, false, false)
			case _ =>
				throw new Exception("unknown expression in parseLeafExpression")
		}
	}


	def parsePredictExpression(e: Expression): String = {
		e match {
			case EqualTo(attribute: Attribute, _@Literal(v, t)) => s"""doc['${attribute.name}'].value == ${SparkUtil.literalToSQL(v, t)}"""
			case EqualTo(_@Literal(v, t), attribute: Attribute) => s"""doc['${attribute.name}'].value == ${SparkUtil.literalToSQL(v, t)}"""
			case GreaterThan(attribute: Attribute, _@Literal(v, t)) => s"""doc['${attribute.name}'].value > ${SparkUtil.literalToSQL(v, t)}"""
			case GreaterThan(_@Literal(v, t), attribute: Attribute) => s"""doc['${attribute.name}'].value < ${SparkUtil.literalToSQL(v, t)}"""
			case GreaterThanOrEqual(attribute: Attribute, _@Literal(v, t)) => s"""doc['${attribute.name}'].value >= ${SparkUtil.literalToSQL(v, t)}"""
			case GreaterThanOrEqual(_@Literal(v, t), attribute: Attribute) => s"""doc['${attribute.name}'].value >= ${SparkUtil.literalToSQL(v, t)}"""
			case LessThan(attribute: Attribute, _@Literal(v, t)) => s"""doc['${attribute.name}'].value < ${SparkUtil.literalToSQL(v, t)}"""
			case LessThan(_@Literal(v, t), attribute: Attribute) => s"""doc['${attribute.name}'].value > ${SparkUtil.literalToSQL(v, t)}"""
			case LessThanOrEqual(attribute: Attribute, _@Literal(v, t)) => s"""doc['${attribute.name}'].value <= ${SparkUtil.literalToSQL(v, t)}"""
			case LessThanOrEqual(_@Literal(v, t), attribute: Attribute) => s"""doc['${attribute.name}'].value >= ${SparkUtil.literalToSQL(v, t)}"""
			case IsNull(attribute: Attribute) => s"""doc['${attribute.name}'].value == null"""
			case IsNotNull(attribute: Attribute) => s"""doc['${attribute.name}'].value != null"""
			case And(left, right) => s"""(${parsePredictExpression(left)}) && (${parsePredictExpression(right)})"""
			case Or(left, right) => s"""(${parsePredictExpression(left)}) || (${parsePredictExpression(right)})"""
			case In(attribute: Attribute, list) =>
				list.map { case _@Literal(v, t) => s"(doc['${attribute.name}'].value == ${SparkUtil.literalToSQL(v, t)})" }.mkString("||")
		}
	}

	def scriptTypeConvert(d: DataType, str: String, inScript: Boolean = true): String = {
		if (!inScript) {
			//not in script, do not add anything
			str
		} else {
			val newType = d match {
				case IntegerType => s"(int)"
				case DoubleType => s"(double)"
				case LongType => s"(long)"
				case FloatType => s"(float)"
				case ShortType => s"(short)"
				case StringType => s"String.valueOf" //TODO: how string cast here
			}
			if (str.indexOf("return") != -1) {
				str.replace("return", newType)
			} else {
				s"""$newType($str)"""
			}
		}
	}

	//--------------

	def colId2aliasMap(mapping: Seq[(String, String)]): Map[Int, String] = {
		mapping.zipWithIndex.map { case (data, index) => (index, data._1) }.toMap //alias name, send name
	}

	def colId2colNameMap(mapping: Seq[(String, String)]): Map[Int, String] = {
		//aggElementMap.map { elem => (elem._1, elem._2.colName) }
		mapping.zipWithIndex.map { case (data, index) => (index, data._2) }.toMap
	}

	def colName2colIdMap(mapping: Seq[(String, String)]): Map[String, Int] = {
		colId2colNameMap(mapping).map(elem => (elem._2, elem._1))
	}


	def literalToSQL(value: Any, dataType: DataType): String = (value, dataType) match {
		case (_, NullType | _: ArrayType | _: MapType | _: StructType) if value == null => "NULL"
		case (v: UTF8String, StringType) => "'" + v.toString.replace("\\", "\\\\").replace("'", "\\'") + "'"
		case (v: Byte, ByteType) => v + ""
		case (v: Boolean, BooleanType) => s"'$v'"
		case (v: Short, ShortType) => v + ""
		case (v: Long, LongType) => v + ""
		case (v: Float, FloatType) => v + ""
		case (v: Double, DoubleType) => v + ""
		case (v: Decimal, t: DecimalType) => v + ""
		case (v: Int, DateType) => s"'${DateTimeUtils.toJavaDate(v)}'"
		case (v: Long, TimestampType) => s"'${DateTimeUtils.toJavaTimestamp(v)}'"
		case _ => if (value == null) "NULL" else value.toString
	}

	//convert response json message to object array: ES use it
	def resultListToObjectArray(schema: StructType, colId2ColName: Map[Int, String], rowMap: Map[String, Any], isagg: Boolean): Seq[Any] = {
		schema.zipWithIndex.map { case (field, index) =>
			val fieldName = field.name.stripSuffix("`").stripPrefix("`")
			val colName = if (!isagg) {
				if (colId2ColName.contains(index)) {
					//if select has alias, use alias; or use colName
					colId2ColName(index)
				} else {
					fieldName
				}
			} else {
				fieldName
			}
			val value = rowMap.getOrElse(s"`$colName`", findValue(colName, rowMap))
			if (value != None) {
				dataTypeConvert(value, field.dataType)
			} else {
				null
			}
		}
	}

	private def findValue(name: String, rowMap: Any): Any = {
		import scala.collection.JavaConversions._
		def find(name: String, data: Any): Any = {
			data match {
				case array: java.util.List[Any@unchecked] =>
					val list = new java.util.ArrayList[Any]()
					for (elem <- array) {
						list.add(find(name, elem))
					}
					list
				case map: java.util.Map[String@unchecked, AnyRef@unchecked] =>
					map.getOrDefault(name, map.get(s"`$name`"))
				case map: Map[String@unchecked, Any@unchecked] =>
					map.getOrElse(name, map.get(s"`$name`"))
				case any => any
			}
		}
		val qualifiedName: Array[String] = name.split("\\.")
		qualifiedName.foldLeft(rowMap) { case (v, n) => find(n, v) }
	}

	def dataTypeConvert(value: Any, dataType: DataType): Any = {
		import scala.collection.JavaConversions._
		//println(s"dataTypeConvert  ${value}, ${dataType}")
		(value, dataType) match {
			case (a: java.util.ArrayList[_], array: ArrayType) =>
				a.map(dataTypeConvert(_, array.elementType)).toArray
			case (a: Any, array: ArrayType) =>
				val arraybuffer = new ArrayBuffer[Any]()
				arraybuffer.add(dataTypeConvert(a, array.elementType))
				arraybuffer.toArray
			//case (s: String, array: ArrayType) => Array(s)
			case (a: java.util.ArrayList[_], schema: StructType) => //TODO: config in param
				a.map { x => dataTypeConvert(x, schema) }.toArray
			case (data: String, schema: StructType) => data //TODO:
			case (a: java.util.ArrayList[_], schema: IntegerType) => //select a+1 from tbl
				if (a.nonEmpty) {
					a(0) match {
						case d: Double => d.toInt
						case i: Integer => i.intValue()
					}
				} else {
					java.lang.Integer.valueOf("0")
				}
			case (a: java.util.ArrayList[_], schema: DoubleType) => //TODO: config in param
				if (a.nonEmpty) {
					a(0) match {
						case d: Double => d.toDouble
						case i: Integer => i.doubleValue()
					}
				} else {
					java.lang.Double.valueOf("0.0")
				}
			case (a: java.util.ArrayList[_], schema: ShortType) => //TODO: config in param
				if (a.nonEmpty) {
					a(0) match {
						case d: Double => d.toShort
						case i: Integer => i.shortValue()
					}
				} else {
					java.lang.Short.valueOf("0")
				}
			case (a: java.util.ArrayList[_], schema: LongType) =>
				if (a.nonEmpty) {
					a(0) match {
						case d: Double => d.toLong
						case i: Integer => i.longValue()
					}
				} else {
					java.lang.Short.valueOf("0")
				}
			case (a: java.util.ArrayList[_], schema: FloatType) =>
				if (a.nonEmpty) {
					a(0) match {
						case d: Double => d.toFloat
						case i: Integer => i.floatValue()
					}
				} else {
					java.lang.Short.valueOf("0")
				}
			case (s: String, BooleanType) => s.toBoolean
			case (s: String, StringType) => s

			case (s: String, TimestampType) =>
				//2015-01-01 , 2042/03/20, 2015-01-01T12:10:30Z, 2015/01/01 12:10:30
				val onlyDateRegexa: Regex = "^(\\d+)-(\\d+)-(\\d+)$".r
				val onlyDateRegexb: Regex = "^(\\d+)/(\\d+)/(\\d+)$".r
				val timestampReg: Regex = "^\\d+$".r
				val ns = if (onlyDateRegexa.pattern.matcher(s).matches()) {
					s + " 00:00:00" //onlyDateRegex.pattern.matcher(s).matches()
				} else if (onlyDateRegexb.pattern.matcher(s).matches()) {
					s.replace("/", "-") + " 00:00:00"
				}
				else s
				//NOTICE: spark need the raw type(timestamp), not convert it to  => LONG
				if (timestampReg.pattern.matcher(s).matches()) {
					new Timestamp(ns.toLong).getTime
				} else {
					java.sql.Timestamp.valueOf(ns.replace("T", " ").replace("Z", " ")) //.getTime
				}
			case (s: String, DateType) =>
				//NOTICE: spark need the raw type(date), not convert it to  => LONG
				java.sql.Date.valueOf(s.replace("T", " ").replace("Z", " ")) //.getTime
			case (f: Float, DoubleType) => f.toDouble
			case (f: Float, FloatType) => f
			case (f: Float, LongType) => f.toLong
			case (f: Float, ShortType) => f.toShort
			case (f: Float, IntegerType) => f.toInt
			case (d: Double, DoubleType) => d
			case (d: Double, FloatType) => d.toFloat
			case (d: Double, LongType) => d.toLong
			case (d: Double, ShortType) => d.toShort
			case (d: Double, IntegerType) => d.toInt
			case (d1: Decimal, d2: DecimalType) => d1
			case (d1: Decimal, TimestampType) => //NOTICE: timestamp => LONG  (1420070400001)
				new java.sql.Timestamp(d1.toLong).getTime
			case (i: Int, IntegerType) => i
			case (i: Int, LongType) => i.toLong
			case (i: Int, DoubleType) => i.toDouble
			case (i: Int, ShortType) => i.toShort
			case (i: Int, FloatType) => i.toFloat
			case (i: Short, IntegerType) => i.toShort
			case (i: Short, LongType) => i.toLong
			case (i: Short, DoubleType) => i.toDouble
			case (i: Short, ShortType) => i.toShort
			case (i: Short, FloatType) => i.toFloat
			case (l: Long, IntegerType) => l.toInt
			case (l: Long, LongType) => l
			case (l: Long, DoubleType) => l.toDouble
			case (l: Long, ShortType) => l.toShort
			case (l: Long, FloatType) => l.toFloat
			case (l: Long, TimestampType) => new Timestamp(l).getTime
			case (l: Long, DateType) => new Date(l).getTime
			case (b: Boolean, BooleanType) => b
			case (i: Int, BooleanType) => if (i == 1) true else false
			case (l: Long, BooleanType) => if (l == 1L) true else false
			case (data: java.util.HashMap[String@unchecked, Any@unchecked], schema: StructType) =>
				val complex = schema.fields.map { field =>
					dataTypeConvert(data.get(field.name), field.dataType)
				} //TODO:
				complex
			case (data: scala.collection.mutable.Map[String@unchecked, Any@unchecked], schema: StructType) =>
				val complex = schema.fields.map { field =>
					dataTypeConvert(data.get(field.name), field.dataType)
				}
				complex

			case (null, _) => null
			case (a, StringType) => a.toString
			case _ =>
				throw new Exception(s"Cannot cast $value to a $dataType")
		}
	}

	/**
	  * if in JDBC, simple convert data, schema is None
	  * if in spark RDD, all array (which is StructType) should be converted to Row, schema is the spark infer schema, Not None
	  **/

	def resultListToRow: (Option[StructType], Seq[Any]) => Row =
		(schema: Option[StructType], seq: Seq[Any]) => {
			if (schema.isDefined) {
				//convert array to ROW for spark
				val row = schema.get.zipWithIndex.map { case (field, index) =>
					val value = seq(index)
					dataTypeConvertRow(value, field.dataType) //TODO:
				}
				Row(row: _*)
			} else {
				//simple convert
				Row(seq: _*)
			}
		}

	def resultListToJdbcRow: (Option[StructType], Seq[Any]) => JdbcRow =
		(schema: Option[StructType], seq: Seq[Any]) => {
			if (schema.isDefined) {
				//convert array to ROW for spark
				val row = schema.get.zipWithIndex.map { case (field, index) =>
					val value = seq(index)
					dataTypeConvertRow(value, field.dataType) //TODO:
				}
				new JdbcRow(row: _*)
			} else {
				//simple convert
				new JdbcRow(seq: _*)
			}
		}

	def dataTypeConvertRow(value: Any, dataType: DataType): Any = {
		(value, dataType) match {
			case (a: Array[Any], array: ArrayType) => //TODO: config in param
				a.map { x => dataTypeConvertRow(x, array.elementType) }
			case (a: Array[Any], struct: StructType) =>
				val complex = struct.zipWithIndex.map { case (field, index) =>
					dataTypeConvertRow(a(index), field.dataType)
				}

				new JdbcRow(complex: _*)
			case (null, _) =>
				null

			case (a: Any, b: Any) => a
			case _ =>
				throw new Exception(s"Cannot cast $value to a $dataType")
		}
	}

}
