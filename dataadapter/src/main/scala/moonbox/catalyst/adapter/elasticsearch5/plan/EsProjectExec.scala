package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.adapter.util.SparkUtil
import moonbox.catalyst.core.parser.udf.{ArrayExists, ArrayFilter, ArrayMap}
import moonbox.catalyst.core.plan.{CatalystPlan, ProjectExec}
import moonbox.catalyst.core.{CatalystContext, ProjectElement}
import org.apache.spark.sql.catalyst.expressions.{Add, Alias, AttributeReference, BinaryArithmetic, Divide, Expression, GetArrayStructFields, GetStructField, If, Literal, Multiply, NamedExpression, Subtract}
import org.apache.spark.sql.types.StructType


class EsProjectExec(projectList: Seq[NamedExpression], child: CatalystPlan) extends ProjectExec(projectList, child) {

    override def translate(context: CatalystContext): Seq[String] = {
        val seq: Seq[String] = child.translate(context)
        //to source include list
        projectList.zipWithIndex.map{case (expression, index) => translateProject(expression, context, "", true, index)}
        context.projectElementMap = projectElementMap.toMap

        appendExtraCol2Project(context)  //NOTE: add extra filter col to project

        seq ++ Seq(toJson:_*)
    }
    //-----------body-----------
    val projectElementMap: scala.collection.mutable.Map[Int, ProjectElement] = scala.collection.mutable.Map.empty[Int, ProjectElement]

    def appendExtraCol2Project(context: CatalystContext)={
        //TODO: add filter exist colname if it not in the project list
        val filterSet = context.filterFunctionSeq
                .filter{elem => elem.isInstanceOf[ArrayExists]}
                .map{ case ArrayExists(left, right) => SparkUtil.parseLeafExpression(left).name }.toSet
        val projectSet = context.projectElementMap.filter(!_._2.colName.isEmpty).map{_._2.colName}.toSet
        val extraColNameSet = filterSet -- projectSet

        val sz = projectElementMap.size
        extraColNameSet.zipWithIndex.foreach{ case (elem, index) =>
            projectElementMap.put(sz + index, ProjectElement(elem, "", "", false))  //padding to tail
        }
    }

    def toJson: Seq[String] = {
        var seq: Seq[String] = Seq.empty[String]

        val field = projectElementMap.filter(!_._2.pushDown).map{ elem =>
            if(!elem._2.pushDown) {
                s""" "${elem._2.colName}" """
            }
        }.mkString(",")
        seq = seq :+ s""""_source": {"includes": [$field], "excludes": [] }"""

        val script = projectElementMap.filter(_._2.pushDown).map{elem =>
                s""" "${elem._2.aliasName}": { "script": { "lang": "expression", "inline": "${elem._2.colJson}" } } """
        }.mkString(",")
        seq = seq :+ s""" "script_fields" : { $script } """
        seq
    }

    def translateProject(e: Expression, context: CatalystContext, alias: String="", store: Boolean = true, index: Int): String = {
        e match {
            case a@ArrayFilter(left, right) =>
                context.projectFunctionSeq :+= (a, index)
                val colName = SparkUtil.parseLeafExpression(left).name

                projectElementMap.put(index, ProjectElement(colName, "", alias, false))
                ""
            case a@ArrayMap(left, right) =>
                context.projectFunctionSeq :+= (a, index)
                val colName = SparkUtil.parseLeafExpression(left).name
                projectElementMap.put(index, ProjectElement(colName, "", alias, false))
                ""
            case a@Alias(child, name) =>
                translateProject(child, context, name, true, index)
            case a: AttributeReference =>
                val colName = a.name
                if(store) {
                    projectElementMap.put(index, ProjectElement(colName, "", alias, false))
                    if(alias.isEmpty) {
                        s"$colName"
                    }
                    else {  //TODO
                        s"doc['${colName}']"
                    }
                } else {
                    if(alias.isEmpty) {
                        s"$colName"
                    }
                    else {
                        s"doc['$colName']"  //TODO: select a + 1
                    }
                }
            case b: BinaryArithmetic =>
                def mkBinaryString(lchild: Expression,rchild: Expression, op: String): String = {
                    val left = translateProject(lchild, context, alias, false, index)
                    val right = translateProject(rchild, context, alias, false, index)
                    s"""($left $op $right)"""
                }

                val colContext = b match {
                    case a@Add(lchild, rchild) => mkBinaryString(lchild, rchild, "+")
                    case s@Subtract(lchild, rchild) => mkBinaryString(lchild, rchild, "-")
                    case m@Multiply(lchild, rchild) => mkBinaryString(lchild, rchild, "*")
                    case d@Divide(lchild, rchild) => mkBinaryString(lchild, rchild, "/")
                }
                if(store) {
                    projectElementMap.put(index, ProjectElement(alias, colContext, alias, true))  //TODO: no colName here
                }
                s"""$colContext"""

            case literal@Literal(v, t) =>
                val literal: String = SparkUtil.literalToSQL(v, t)
                if(store) {     //select 1 from table
                    projectElementMap.put(index, ProjectElement(alias, literal, alias, true))
                }
                literal
            case If(predicate, trueValue, falseValue) => ""
            case g@GetStructField(child, ordinal, _) =>  //select user.name from nest_table
                val childSchema = child.dataType.asInstanceOf[StructType]
                val fieldName =  childSchema(ordinal).name
                val childName = child match {
                    case a: AttributeReference => a.name
                    case e: Expression => e.toString()
                }
                if(store) {
                    val nestName = s"$childName.${g.name.getOrElse(fieldName)}"
                    projectElementMap.put(index, ProjectElement(nestName, "", alias, false))
                    nestName
                } else ""
            case g@GetArrayStructFields(child, field, _, _, _) =>
                val parentName = translateProject(child, context, "", false, index)
                val colName = s"$parentName.${field.name}"
                if(store) {
                    //NOTE: es do not support A.B as C in doc value field
                    projectElementMap.put(index, ProjectElement(colName, "", alias, false))
                }
                s"$colName"
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
