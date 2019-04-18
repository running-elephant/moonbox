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

package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.adapter.util.SparkUtil
import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.parser.udf.ArrayExists
import moonbox.catalyst.core.plan.{CatalystPlan, FilterExec}
import org.apache.spark.sql.catalyst.CatalystTypeConverters
import org.apache.spark.sql.catalyst.CatalystTypeConverters.convertToScala
import org.apache.spark.sql.catalyst.expressions.{And, Attribute, AttributeReference, Contains, EmptyRow, EndsWith, EqualNullSafe, EqualTo, Expression, GetStructField, GreaterThan, GreaterThanOrEqual, If, In, IsNotNull, IsNull, LessThan, LessThanOrEqual, Literal, Not, Or, ScalaUDF, StartsWith}
import org.apache.spark.sql.catalyst.parser.CatalystSqlParser
import org.apache.spark.sql.types.{StringType, StructType}
import org.apache.spark.unsafe.types.UTF8String


class EsFilterExec(condition: Expression, child: CatalystPlan) extends FilterExec(condition, child) {

    val strictPushDown: Boolean = false
    //val isES50: Boolean = true
    //var filterFunctionSeq: scala.collection.mutable.Seq[Expression] = scala.collection.mutable.Seq.empty[Expression]

    override def translate(context: CatalystContext): Seq[String] = {
        val seq: Seq[String] = child.translate(context)

        val filterStr = translateFilter(condition, context)
        //context.filterFunctionSeq ++= filterFunctionSeq   //TODO: no filter function, maybe have in future
        seq ++ Seq(toJson(filterStr))
    }

    //------body---------
    def toJson(s: String): String = {
        if(!s.isEmpty) {
            s""" "query": $s """
        }else{
            s
        }
    }

    import moonbox.catalyst.adapter.elasticsearch5.util.EsUtil._
    import moonbox.catalyst.adapter.util.SparkUtil._


    def handleEqualTo(e: Expression, l: Literal, isES50: Boolean) = {
        val value = convertToScala(l.value, l.dataType)
        val attribute = e match {
            case a: Attribute =>
                a.name
            case g@GetStructField(child, ordinal, name) =>
                val childSchema = child.dataType.asInstanceOf[StructType]
                val fieldName =  childSchema(ordinal).name
                val childName = child match {
                    case a: AttributeReference => a.name
                    case e: Expression => e.toString()
                }
                s"${childName}.${g.name.getOrElse(fieldName)}"
        }

        if (value == null || value == None || value == Unit) {
            if (isES50) {
                s"""{"bool":{"must_not":{"exists":{"field":"$attribute"}}}}"""
            }
            else {
                s"""{"missing":{"field":"$attribute"}}"""
            }
        }else {
            if (strictPushDown) {
                s"""{"term":{"$attribute":${extract(value)}}}"""
            }
            else {
                if (isES50) {
                    s"""{"match":{"$attribute":${extract(value)}}}"""
                }
                else {
                    s"""{"query":{"match":{"$attribute":${extract(value)}}}}"""
                }
            }
        }

    }

    def handleGreaterThan(a: Attribute, l: Literal) = {
        val value = convertToScala(l.value, l.dataType)
        val attribute = a.name
        s"""{"range":{"$attribute":{"gt" :${extract(value)}}}}"""
    }

    def handleGreaterThanOrEqual(a: Attribute, l: Literal) = {
        val value = convertToScala(l.value, l.dataType)
        val attribute = a.name
        s"""{"range":{"$attribute":{"gte":${extract(value)}}}}"""
    }
    def handleLessThan(a: Attribute, l: Literal) = {
        val value = convertToScala(l.value, l.dataType)
        val attribute = a.name
        s"""{"range":{"$attribute":{"lt" :${extract(value)}}}}"""
    }
    def handleLessThanOrEqual(a: Attribute, l: Literal) = {
        val value = convertToScala(l.value, l.dataType)
        val attribute = a.name
        s"""{"range":{"$attribute":{"lte":${extract(value)}}}}"""
    }

    def handleEqualNullSafe(a: Attribute, l: Literal, isES50: Boolean) = {
        val value = convertToScala(l.value, l.dataType)
        val attribute = a.name
        val arg = extract(value)
        if (strictPushDown) s"""{"term":{"$attribute":$arg}}"""
        else {
            if (isES50) {
                s"""{"match":{"$attribute":$arg}}"""
            }
            else {
                s"""{"query":{"match":{"$attribute":$arg}}}"""
            }
        }
    }

    def translateSparkFunction(expression: Expression, context: CatalystContext): String = {
        import org.apache.spark.sql.catalyst.expressions.IsNull
        expression match {
            case If(predicate, trueValue, falseValue) =>
                if (predicate.isInstanceOf[IsNull]) {
                    s"""{"query": { "bool": { "filter":  ${translateSparkFunction(falseValue, context)} }}}"""
                } else {
                    s"""{"query": { "bool": { "filter":  ${translateSparkFunction(trueValue, context)} }}}"""
                }
            case a@ArrayExists(left, right) =>  //TODO: nest to json
                //filterFunctionSeq = filterFunctionSeq :+ a
                val objFullName = SparkUtil.parseLeafExpression(left).name
                val array: Array[String] = objFullName.split('.')
                val str = right.toString().replace("x", objFullName)
                val exp = CatalystSqlParser.parseExpression(str)
                if(context.nestFields.contains(array(0))) {
                    //it is nest
                    s"""{"nested": {"path": "${array(0)}", "query":${translateFilter(exp, context)}}} """
                }
                else {
                    context.filterFunctionSeq :+= a //TODO: ES filter in array is not accurate, so we also filter by ourselves
                    s"${translateFilter(exp, context)}"
                }

        }
    }

    def translateESFunction(name: String, expression: Seq[Expression]): String = {
        if(name.toLowerCase == "geo_distance") { //Geo Distance Query, geo_distance only support one filter exp
            val param: Seq[String] = expression.map {
                parseLeafExpression(_).name.replaceAll("'", "").replaceAll("\"", "")
            }
            if (param.length != 3) {
                throw new Exception("geo_distance should have 3 parameters")
            }
            s"""{"bool":{"filter": {"geo_distance":{"distance": "${param(0)}", "location": { "lat":  ${param(1)}, "lon": ${param(2)} } } } } }"""
        }
        else if(name.toLowerCase == "geo_shape") {   //GeoShape Query
            val param: Seq[String] = expression.map {
                parseLeafExpression(_).name.replaceAll("'", "").replaceAll("\"", "")
            }
            if (param.length != 6) {
                throw new Exception("geo_distance should have 6 parameters")
            }
            s"""{"bool":{"filter": {"geo_shape":{"location": { "shape": {"type": "${param(0)}", "coordinates": [[${param(1)}, ${param(2)}], [${param(3)}, ${param(4)}]]}, "relation": "${param(5)}" } } } } }"""
        }
        else if(name.toLowerCase == "geo_bounding_box") {   //GeoShape Query
            val param: Seq[String] = expression.map {
                parseLeafExpression(_).name.replaceAll("'", "").replaceAll("\"", "")
            }
            if (param.length != 5) {
                throw new Exception("geo_distance should have 5 parameters")
            }
            s"""{"bool":{"filter": {"geo_bounding_box":{"${param(0)}": { "top_left" : {"lat": ${param(1)},"lon": ${param(2)}},"bottom_right": {"lat": ${param(3)},"lon": ${param(4)} }} } } } }"""
        }
        else if(name.toLowerCase == "geo_polygon") {
            val param: Seq[String] = expression.map {
                parseLeafExpression(_).name.replaceAll("'", "").replaceAll("\"", "")
            }
            if (param.length != 3) {
                throw new Exception("geo_distance should have 3 parameters")
            }
            val lat = param(1).stripPrefix("[").stripSuffix("]").split(",")
            val lon = param(2).stripPrefix("[").stripSuffix("]").split(",")
            val point = (0 until lat.length).map{idx => s"""{"lat":${lat(idx)}, "lon":${lon(idx)} }"""}.mkString(",")
            s"""{"bool":{"filter": {"geo_polygon":{"${param(0)}": { "points" : [ $point ] } } } } } """

        }
        else { //NOTE: NEST QUERY do in ARRAY_EXIST FUNCTION
            ""
        }
    }

    def translateFilter(filter: Expression, context: CatalystContext):String = {
        // the pushdown can be strict - i.e. use only filters and thus match the value exactly (works with non-analyzed)
        // or non-strict meaning queries will be used instead that is the filters will be analyzed as well
        val isES50 = context.isES50
        filter match {
            //TODO: handle all self function
            case a@ArrayExists(left, right)  => translateSparkFunction(a, context)
            case u@ScalaUDF(_, _, children, _, Some(name)) => translateESFunction(name, children)

            //case EqualTo(attribute: Expression, value: Literal) => handleEqualTo(attribute, value, isES50)
            //case EqualTo(value: Literal, attribute: Expression) => handleEqualTo(attribute, value, isES50)

            case EqualTo(attribute: Attribute, value: Literal) => handleEqualTo(attribute, value, isES50)
            case EqualTo(value: Literal, attribute: Attribute) => handleEqualTo(attribute, value, isES50)

            case GreaterThan(attribute: Attribute, value: Literal)        => handleGreaterThan(attribute, value)
            case GreaterThan(value: Literal, attribute: Attribute)        => handleGreaterThan(attribute, value)

            case GreaterThanOrEqual(attribute: Attribute, value: Literal) => handleGreaterThanOrEqual(attribute, value)
            case GreaterThanOrEqual(value: Literal, attribute: Attribute) => handleGreaterThanOrEqual(attribute, value)

            case LessThan(attribute: Attribute, value: Literal)           => handleLessThan(attribute, value)
            case LessThan(value: Literal, attribute: Attribute)           => handleLessThan(attribute, value)

            case LessThanOrEqual(attribute: Attribute, value: Literal)    => handleLessThanOrEqual(attribute, value)
            case LessThanOrEqual(value: Literal, attribute: Attribute)    => handleLessThanOrEqual(attribute, value)

            case EqualNullSafe(attribute: Attribute, value: Literal) =>  handleEqualNullSafe(attribute, value, isES50)
            case EqualNullSafe(value: Literal, attribute: Attribute) =>  handleEqualNullSafe(attribute, value, isES50)

            case In(attribute: Attribute, list)  if !list.exists(!_.isInstanceOf[Literal])  =>
                val hSet = list.map(e => e.eval(EmptyRow))
                val toScala = CatalystTypeConverters.createToScalaConverter(attribute.dataType)
                val values: Array[Any] = hSet.toArray.map(toScala)
                // when dealing with mixed types (strings and numbers) Spark converts the Strings to null (gets confused by the type field)
                // this leads to incorrect query DSL hence why nulls are filtered
                val filtered = values filter (_ != null)
                if (filtered.isEmpty) {
                    return ""
                }
                //if (!strictPushDown)
                    //println(s"Attribute $attribute type not suitable for match query; using terms (strict) instead")

                if (strictPushDown) s"""{"terms":{"$attribute":${extractAsJsonArray(filtered)}}}"""
                else {
                    if (isES50) {
                        s"""{"bool":{"should":[${extractMatchArray(attribute.name, filtered, isES50)}]}}"""  //www, add param
                    }
                    else {
                        s"""{"or":{"filters":[${extractMatchArray(attribute.name, filtered, isES50)}]}}"""    //www, add param
                    }
                }
            case IsNull(attribute: Attribute)   =>
                if (isES50) {
                    s"""{"bool":{"must_not":{"exists":{"field":"${attribute.name}"}}}}"""
                }
                else {
                    s"""{"missing":{"field":"${attribute.name}"}}"""
                }
            case IsNotNull(attribute: Attribute)    => s"""{"exists":{"field":"${attribute.name}"}}"""
            case And(left, right)   =>
                if (isES50) {
                    s"""{"bool":{"filter":[${translateFilter(left, context)}, ${translateFilter(right, context)}]}}"""
                }
                else {
                    s"""{"and":{"filters":[${translateFilter(left, context)}, ${translateFilter(right, context)}]}}"""
                }
            case Or(left, right)    =>
                if (isES50) {
                    s"""{"bool":{"should":[{"bool":{"filter":${translateFilter(left, context)}}}, {"bool":{"filter":${translateFilter(right, context)}}}]}}"""
                }
                else {
                    s"""{"or":{"filters":[${translateFilter(left, context)}, ${translateFilter(right, context)}]}}"""
                }
            case Not(filterToNeg)   =>
                if (isES50) {
                    s"""{"bool":{"must_not":${translateFilter(filterToNeg, context)}}}"""
                }
                else {
                    s"""{"not":{"filter":${translateFilter(filterToNeg, context)}}}"""
                }
            // the filter below are available only from Spark 1.3.1 (not 1.3.0)
            case StartsWith(attribute: Attribute, Literal(v: UTF8String, StringType)) =>
                val arg = {
                    val x = v.toString
                    //if (!strictPushDown) x.toLowerCase(Locale.ROOT) else x
                    x
                }
                if (isES50) {
                    s"""{"wildcard":{"${attribute.name}":"$arg*"}}"""
                }
                else {
                    s"""{"query":{"wildcard":{"${attribute.name}":"$arg*"}}}"""
                }
            case EndsWith(attribute: Attribute, Literal(v: UTF8String, StringType)) =>
                val arg = {
                    val x = v.toString
                    //if (!strictPushDown) x.toLowerCase(Locale.ROOT) else x
                    x
                }
                if (isES50) {
                    s"""{"wildcard":{"${attribute.name}":"*$arg"}}"""
                }
                else {
                    s"""{"query":{"wildcard":{"${attribute.name}":"*$arg"}}}"""
                }
            case Contains(attribute: Attribute, Literal(v: UTF8String, StringType)) =>
                val arg = {
                    val x =  v.toString
                    //if (!strictPushDown) x.toLowerCase(Locale.ROOT) else x
                    x
                }
                if (isES50) {
                    s"""{"wildcard":{"${attribute.name}":"*$arg*"}}"""
                }
                else {
                    s"""{"query":{"wildcard":{"${attribute.name}":"*$arg*"}}}"""
                }
            case _ => ""
        }
    }


}


object EsFilterExec {
    def apply(condition: Expression, child: CatalystPlan): EsFilterExec = {
        new EsFilterExec(condition, child)
    }

}
