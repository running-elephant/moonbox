package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.adapter.util.SparkUtil
import moonbox.catalyst.core.parser.udf.{ArrayExists, ArrayFilter, ArrayMap}
import moonbox.catalyst.core.plan.{CatalystPlan, ProjectExec}
import moonbox.catalyst.core.CatalystContext
import org.apache.spark.sql.catalyst.expressions.{Add, Alias, AttributeReference, BinaryArithmetic, Divide, Expression, GetArrayStructFields, GetStructField, If, Literal, Multiply, NamedExpression, Subtract}
import org.apache.spark.sql.types.StructType


class EsProjectExec(projectList: Seq[NamedExpression], child: CatalystPlan) extends ProjectExec(projectList, child) {
    var catalystContext: CatalystContext = _
    override def translate(context: CatalystContext): Seq[String] = {
        catalystContext = context
        val seq: Seq[String] = child.translate(context)

        var aliasColumnSeq = Seq.empty[String]
        var sourceColumnSeq = Seq.empty[String]
        catalystContext.projectElementSeq = projectList.zipWithIndex.map {
            case (_@Alias(children, alias), index) =>
                children match {
                    case _: BinaryArithmetic | _ : Literal =>
                        aliasColumnSeq = aliasColumnSeq :+ s"""|"$alias": {
                            |    "script" : {
                            |        "lang" : "expression",
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

        val a: String = s"""|"script_fields": {
              |$aliasColumn
              |} """.stripMargin
        val b: String =  s""" |"_source":{
               |"includes": [$sourceColumn], "excludes": []
               |} """.stripMargin
        seq :+b :+ a
    }

    def mkColumnInSource(col: String): String = { s"""|$col""".stripMargin }
    def mkColumnInScript(col: String): String = { s"""|doc['$col']""".stripMargin }

    def translateProject(e: Expression, script: Boolean = false, index: Int): String = {
        e match {
            case a@ArrayFilter(left, right) =>
                catalystContext.projectFunctionSeq :+= (a, index)
                translateProject(left, script, index)

            case a@ArrayMap(left, right) =>
                catalystContext.projectFunctionSeq :+= (a, index)
                translateProject(left, script, index)

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
                val literal: String = SparkUtil.literalToSQL(v, t)
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
