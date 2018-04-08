package moonbox.catalyst.adapter.util

import java.sql.{Date, Timestamp}

import moonbox.catalyst.adapter.elasticsearch5.rdd.EsRow
import moonbox.catalyst.adapter.jdbc.JdbcRow
import moonbox.common.MbLogging
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.{Alias, AttributeReference, Cast, Expression, GetArrayStructFields, GetStructField, Literal}
import org.apache.spark.sql.catalyst.util.DateTimeUtils
import org.apache.spark.sql.types._
import org.apache.spark.unsafe.types.UTF8String

import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex


case class FieldName(name: String, alias: String, dtype: DataType, isLiteral: Boolean = false)

object SparkUtil extends MbLogging{

    def parseLeafExpression(e: Expression): FieldName = {
        e match {
            case a: AttributeReference =>
                //val qualifierPrefix = a.qualifier.map(_ + ".").getOrElse("")
                FieldName(s"${a.name}", "", a.dataType, false)
            case literal@Literal(v, t) =>
                FieldName(literalToSQL(v, t), "", literal.dataType, true)
            case Alias(exp, alias) =>
                val value = parseLeafExpression(exp)
                FieldName(s"""${value.name}""", alias, value.dtype, false)
            case c@Cast(child, _, _) =>
                parseLeafExpression(child)
            case g@GetStructField(child, ordinal, _) =>  //select user.name from nest_table
                val childSchema = child.dataType.asInstanceOf[StructType]
                val fieldName =  childSchema(ordinal).name
                val fieldType =  childSchema(ordinal).dataType
                val childName = child match {
                    case a: AttributeReference => a.name
                    case e: Expression => e.toString()
                }
                FieldName(s"$childName.${g.name.getOrElse(fieldName)}", "",  fieldType, false)
            case g@GetArrayStructFields(child, field, _, _, _) =>
                val parentName = parseLeafExpression(child)
                FieldName(s"""${parentName.name}.${field.name}""", "", field.dataType, false)
            case _ =>
                throw new Exception("unknown expression in parseLeafExpression")
        }
    }

    def literalToSQL(value: Any, dataType: DataType): String = (value, dataType) match {
        case (_, NullType | _: ArrayType | _: MapType | _: StructType) if value == null => "NULL"
        case (v: UTF8String, StringType) =>  "'" + v.toString.replace("\\", "\\\\").replace("'", "\\'") + "'"
        case (v: Byte, ByteType) => v + ""
        case (v: Boolean, BooleanType) => s"'$v'"
        case (v: Short, ShortType) => v + ""
        case (v: Long, LongType) => v + ""
        case (v: Float, FloatType) => v + ""
        case (v: Double, DoubleType) => v + ""
        case (v: Decimal, t: DecimalType) => v + ""
        case (v: Int, DateType) =>s"'${DateTimeUtils.toJavaDate(v)}'"
        case (v: Long, TimestampType) => s"'${DateTimeUtils.toJavaTimestamp(v)}'"
        case _ => if (value == null) "NULL" else value.toString
    }

    //convert response json message to object array: ES use it
    def resultListToObjectArray(schema: StructType, colId2ColName: Map[Int, String], rowMap: Map[String, Any], isagg: Boolean): Seq[Any] = {
        schema.zipWithIndex.map { case (field, index) =>
            val fieldName = field.name.stripSuffix("`").stripPrefix("`")
            val colName = if(!isagg) {
                if (colId2ColName.contains(index)) { //if select has alias, use alias; or use colName
                    colId2ColName(index)
                } else {
                    fieldName
                }
            }else{
                fieldName
            }
            val value = rowMap.getOrElse(s"`$colName`", findValue(colName, rowMap))
            if(value != None) {
                dataTypeConvert(value, field.dataType)
            }else{
                logInfo(s"!can not find col $colName in resultlist")
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
                    for(elem <- array) {
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
            case (a: java.util.ArrayList[_], schema: StructType) =>   //TODO: config in param
                a.map{ x => dataTypeConvert(x, schema) }.toArray
            case (data: String, schema: StructType) => data //TODO:
            case (a: java.util.ArrayList[_], schema: IntegerType) =>   //select a+1 from tbl
                if(a.nonEmpty) {
                    a(0) match {
                        case d: Double => d.toInt
                        case i: Integer => i.intValue()
                    }
                } else { java.lang.Integer.valueOf("0") }
            case (a: java.util.ArrayList[_], schema: DoubleType) =>   //TODO: config in param
                if(a.nonEmpty) {
                    a(0) match {
                        case d: Double => d.toDouble
                        case i: Integer => i.doubleValue()
                    }
                } else { java.lang.Double.valueOf("0.0") }
            case (a: java.util.ArrayList[_], schema: ShortType) =>   //TODO: config in param
                if(a.nonEmpty) {
                    a(0) match {
                        case d: Double => d.toShort
                        case i: Integer => i.shortValue()
                    }
                } else { java.lang.Short.valueOf("0") }
            case (a: java.util.ArrayList[_], schema: LongType) =>
                if(a.nonEmpty) {
                    a(0) match {
                        case d: Double => d.toLong
                        case i: Integer => i.longValue()
                    }
                } else { java.lang.Short.valueOf("0") }
            case (s: String, BooleanType) => s.toBoolean
            case (s: String, StringType) => s

            case (s: String, TimestampType) =>
                //2015-01-01 , 2042/03/20, 2015-01-01T12:10:30Z, 2015/01/01 12:10:30
                val onlyDateRegexa :Regex = "^(\\d+)-(\\d+)-(\\d+)$".r
                val onlyDateRegexb: Regex = "^(\\d+)/(\\d+)/(\\d+)$".r
                val ns = if(onlyDateRegexa.pattern.matcher(s).matches()){
                     s + " 00:00:00" //onlyDateRegex.pattern.matcher(s).matches()
                }else if(onlyDateRegexb.pattern.matcher(s).matches()){
                    s.replace("/", "-") + " 00:00:00"
                }
                else s
                //NOTICE: timestamp => LONG
                java.sql.Timestamp.valueOf(ns.replace("T", " ").replace("Z", " ")).getTime
            case (s: String, DateType) =>
                //NOTICE: timestamp => LONG
                java.sql.Date.valueOf(s.replace("T", " ").replace("Z", " ")).getTime
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
            case (i: Short, IntegerType) => i.toShort
            case (i: Short, LongType) => i.toShort
            case (i: Short, DoubleType) => i.toShort
            case (i: Short, ShortType) => i.toShort
            case (l: Long, IntegerType) => l.toInt
            case (l: Long, LongType) => l
            case (l: Long, DoubleType) => l.toDouble
            case (l: Long, ShortType) => l.toShort
            case (l: Long, TimestampType) => new Timestamp(l).getTime
            case (l: Long, DateType) => new Date(l).getTime
            case (b: Boolean, BooleanType) => b
            case (data: java.util.HashMap[String@unchecked, Any@unchecked], schema: StructType) =>
                val complex = schema.fields.map { field =>
                    dataTypeConvert(data.get(field.name), field.dataType)
                }  //TODO:
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

    def resultListToJdbcRow: (Option[StructType], Seq[Any]) => JdbcRow =
        (schema: Option[StructType],  seq: Seq[Any]) => {
            if(schema.isDefined) { //convert array to ROW for spark
                val row = schema.get.zipWithIndex.map { case (field, index) =>
                    val value = seq(index)
                    dataTypeConvertRow(value, field.dataType) //TODO:
                }
                new JdbcRow(row :_*)
            }else{  //simple convert
                new JdbcRow(seq:_*)
            }
        }


    def dataTypeConvertRow(value: Any, dataType: DataType): Any = {
        (value, dataType) match {
            case (a: Array[Any], array: ArrayType) =>   //TODO: config in param
                a.map{ x => dataTypeConvertRow(x, array.elementType) }
            case (a: Array[Any], struct: StructType) =>
                    val complex = struct.zipWithIndex.map { case (field, index) =>
                        dataTypeConvertRow(a(index), field.dataType)
                    }

                new JdbcRow(complex:_*)
            case (null, _) => null
            case (a: Any, b: Any) =>  a
            case _ =>
                throw new Exception(s"Cannot cast $value to a $dataType")
        }
    }

}
