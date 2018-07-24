/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

import moonbox.catalyst.adapter.util.SparkUtil._
import moonbox.catalyst.core.parser.udf.{ArrayExists, ArrayFilter, ArrayMap}
import moonbox.catalyst.core.plan.{CatalystPlan, ProjectExec}
import moonbox.catalyst.core.CatalystContext
import org.apache.spark.sql.catalyst.expressions.{Add, Alias, And, Attribute, AttributeReference, BinaryArithmetic, CaseWhenCodegen, Cast, Divide, EqualTo, Expression, GetArrayStructFields, GetStructField, GreaterThan, GreaterThanOrEqual, If, In, IsNotNull, IsNull, LessThan, LessThanOrEqual, Literal, Multiply, NamedExpression, Or, Round, Substring, Subtract}
import org.apache.spark.sql.types._


class EsProjectExec(projectList: Seq[NamedExpression], child: CatalystPlan) extends ProjectExec(projectList, child) {
    var catalystContext: CatalystContext = _
    override def translate(context: CatalystContext): Seq[String] = {
        catalystContext = context
        val seq: Seq[String] = child.translate(context)

        var aliasColumnSeq = Seq.empty[String]
        var sourceColumnSeq = Seq.empty[String]
        if(catalystContext.projectElementSeq.nonEmpty) {
            seq
        }else{
            catalystContext.projectElementSeq = projectList.zipWithIndex.map {
                case (_@Alias(children, alias), index) =>
                    children match {
                        case _: BinaryArithmetic | _ : Literal | _ : Substring | _: Round | _: Cast | _: CaseWhenCodegen  =>
                            aliasColumnSeq = aliasColumnSeq :+ s"""|"$alias": {
                                                                   |    "script" : {
                                                                   |        "inline" : "${translateProject(children, true, index)}"
                                                                   |    }
                                                                   |}""".stripMargin
                            (alias, alias)
                        case _ =>
                            val column = s"""${translateProject(children, false, index)}"""
                            sourceColumnSeq = sourceColumnSeq :+ s""""$column""""
                            (alias, column)
                    }
                case (e: Expression, index) =>
                    val column = s"""${translateProject(e, false, index)}"""
                    sourceColumnSeq = sourceColumnSeq :+ s""""$column""""
                    (column, column)
            }
            val aliasColumn = aliasColumnSeq.mkString(",")
            val sourceColumn = sourceColumnSeq.mkString(",")

            val scriptField: String = s"""|"script_fields": {
                                          |$aliasColumn
                                          |} """.stripMargin
            val sourceField: String =  s""" |"_source":{
                                          |"includes": [$sourceColumn], "excludes": []
                                          |} """.stripMargin
            seq :+sourceField :+ scriptField
        }

    }

    def mkColumnInSource(col: String): String = { s"""|$col""".stripMargin }
    def mkColumnInScript(col: String): String = { s"""|doc['$col'].value""".stripMargin }


    def translateProject(e: Expression, script: Boolean = false, index: Int): String = {
        e match {
            case a@ArrayFilter(left, right) =>
                catalystContext.projectFunctionSeq :+= (a, index)
                translateProject(left, script, index)

            case a@ArrayMap(left, right) =>
                catalystContext.projectFunctionSeq :+= (a, index)
                translateProject(left, script, index)

            case s@Substring(str, pos, len) =>
                val field = translateProject(str, script, index)
                val start = translateProject(pos, script, index)
                val length = translateProject(len, script, index)
                s"""$field.substring($start,$length)"""

            case Round(child, scale) =>
                def pow(m: Int, n: Int): Int =  {
                    var result: Int = 1
                    for(i <- 0 until n){result = result * m}
                    result
                }
                val exponent = translateProject(scale, script, index)
                val number = pow(10, java.lang.Integer.valueOf(exponent).intValue() )
                s"""Math.round(${translateProject(child, script, index)}) * $number / $number.0"""

            case Cast(child, dataType, _) =>
                val param = translateProject(child, script, index)
                s"""${scriptTypeConvert(dataType, param)}"""

            case c@CaseWhenCodegen(branches, elseValue) =>
                val ifPart = s"""if(${parsePredictExpression(branches(0)._1)})"""
                val returnPart = s"{return ${translateProject(branches(0)._2, script, index)};}"
                val elsePart = if(elseValue.isDefined){
                    s"""else {return ${translateProject(elseValue.get, script, index)};}"""
                } else ""
                s"""$ifPart $returnPart $elsePart"""

            case a: AttributeReference =>
                if(script) { mkColumnInScript(a.name) }
                else { mkColumnInSource(a.name) }

            case b: BinaryArithmetic =>
                def mkBinaryString(lchild: Expression,rchild: Expression, op: String): String = {
                    val left = translateProject(lchild, script, index)
                    val right = translateProject(rchild, script, index)
                    s"""($left $op $right)"""
                }

                val colContext = b match {
                    case a@Add(lchild, rchild) => mkBinaryString(lchild, rchild, "+")
                    case s@Subtract(lchild, rchild) => mkBinaryString(lchild, rchild, "-")
                    case m@Multiply(lchild, rchild) => mkBinaryString(lchild, rchild, "*")
                    case d@Divide(lchild, rchild) => mkBinaryString(lchild, rchild, "/")
                }
                s"""$colContext"""

            case _@Literal(v, t) =>
                val literal: String = literalToSQL(v, t)
                literal

            case g@GetStructField(child, ordinal, _) =>  //select user.name from nest_table
                val childSchema = child.dataType.asInstanceOf[StructType]
                val fieldName =  childSchema(ordinal).name
                val childName = child match {
                    case a: AttributeReference => a.name
                    case e: Expression => e.toString()
                }
                s"$childName.${g.name.getOrElse(fieldName)}"

            case g@GetArrayStructFields(child, field, _, _, _) =>
                val parentName = translateProject(child, false, index)
                val colName = s"$parentName.${field.name}"

                if(script) { mkColumnInScript(colName) }
                else { mkColumnInSource(colName) }

            case _ => println("ERROR translateProject")
                ""
        }
    }

    def getScriptField(expression: Expression): String = {
        expression match {
            case a@AttributeReference(name, _, _, _) =>
                s"doc['$name']"
            case _ =>
                s"$expression"
        }
    }

}


object EsProjectExec{
    def apply(projectList: Seq[NamedExpression], child: CatalystPlan): EsProjectExec = {
        new EsProjectExec(projectList, child)
    }

}
