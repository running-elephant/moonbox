package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.adapter.util.SparkUtil._
import moonbox.catalyst.adapter.util.{FieldName, SparkUtil}
import moonbox.catalyst.core.plan.{AggregateExec, CatalystPlan}
import moonbox.catalyst.core.CatalystContext
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.expressions.{Alias, AttributeReference, Expression, NamedExpression}
import org.apache.spark.sql.types.{DataType, StringType}


class EsAggregateExec(groupingExpressions: Seq[Expression],
                      aggregateExpressions: Seq[NamedExpression],
                      child: CatalystPlan) extends AggregateExec(groupingExpressions, aggregateExpressions, child) {
    override def translate(context: CatalystContext): Seq[String] = {
        val seq: Seq[String] = child.translate(context)

        groupingExpressions.foreach{e =>
            val field: FieldName = SparkUtil.parseLeafExpression(e)
            groupBySeq = groupBySeq :+ s""""${field.name}"-"terms":{"field": "${field.name}", "size": @limit}"""
        }

        aggregateExpressions.zipWithIndex.foreach{ case (e, idx)=>
            parseAggExpression(e, "", idx)
        }
        //context.aggElementMap = aggFieldMap.toMap
        context.hasAgg = true
        seq ++ Seq(toJson)
    }

    var aggFieldSeq: Seq[(String, String)] = Seq.empty[(String, String)]
    var aggFunSeq: Seq[String] = Seq.empty[String]
    var groupBySeq: Seq[String] = Seq.empty[String]

    def toJson(): String = {
        aggregateFormat(groupBySeq, aggFunSeq, 0)
    }

    def aggregateFormat(groupSeq: Seq[String], aggSeq: Seq[String], i: Int): String = {
        if (i < groupSeq.size) {
            val seq = groupSeq(i).split("-") // split for getting group by name
            s""" "aggregations": {${seq(0)}: {${seq(1)} , ${aggregateFormat(groupSeq, aggSeq, i + 1)} } }"""
        }
        else {
            s""" "aggregations": {${aggSeq.map(e => s""" $e """).mkString(",")}}"""
        }
    }

    def parseFunToJson(func: AggregateFunction, alias: String="", isDistinct:Boolean=false): String = {
        func match {
            case f: Average =>
                val param = func.children.map(parseLeafExpression(_)).head
                if(param.inScript) {
                    s""""$alias":{"avg": {"script": {"inline": "${param.name}"}}}"""
                }else{
                    s""""$alias":{"avg": {"field": "${param.name}"}}"""
                }
            case f: Sum =>
                val param = func.children.map(parseLeafExpression(_)).head
                if(param.inScript) {
                    s""""$alias":{"sum": {"script": {"inline": "${param.name}"}}}"""
                }else {
                    s""""$alias":{"sum": {"field": "${param.name}"}}"""
                }
            case f: Max =>
                val param = func.children.map(parseLeafExpression(_)).head
                if(param.inScript) {
                    s""""$alias":{"max": {"script": {"inline": "${param.name}"}}}"""
                }else {
                    s""""$alias":{"max": {"field": "${param.name}"}}"""
                }
            case f: Min =>
                val param = func.children.map(parseLeafExpression(_)).head
                if(param.inScript) {
                    s""""$alias":{"min": {"script": {"inline": "${param.name}"}}}"""
                }else{
                    s""""$alias":{"min": {"field": "${param.name}"}}"""
                }
            case f: Count =>
                val param = func.children.map(parseLeafExpression(_)).head
                if(param.inScript) {
                    if (isDistinct) { //distinct
                        s""""$alias":{"value_count": {"script": {"inline": "${param.name}"}}}"""
                    }
                    else { //count
                        if (param.name == "*" || param.isLiteral) { //spark parse * to 1, select count(*) from tbl => select count(1) from tbl
                            s""""$alias":{"value_count": {"field": "_index"}}"""
                        } else {
                            s""""$alias":{"value_count": {"script": {"inline": "${param.name}"}}}"""
                        }
                    }
                }else{
                    if (isDistinct) { //distinct
                        s""""$alias":{"value_count": {"field": "${param.name}"}}"""
                    }
                    else { //count
                        if (param.name == "*" || param.isLiteral) { //spark parse * to 1, select count(*) from tbl => select count(1) from tbl
                            s""""$alias":{"value_count": {"field": "_index"}}"""
                        } else {
                            s""""$alias":{"value_count": {"field": "${param.name}"}}"""
                        }
                    }
                }
        }
    }


    def parseAggExpression(e: Expression, alias: String="", idx: Int): Unit = {
        e match {
            case a@Alias(child, name) =>
                parseAggExpression(child, name, idx)
            case a: AttributeReference =>
            case AggregateExpression(aggFunc, _, isDistinct, _) =>
                val funcJson = parseFunToJson(aggFunc, alias, isDistinct)
                aggFunSeq = aggFunSeq :+ funcJson
                aggFieldSeq = aggFieldSeq :+ (alias, alias)
            case _ => println("ERROR")
        }
    }


}

object EsAggregateExec {
    def apply(groupingExpressions: Seq[Expression],
              aggregateExpressions: Seq[NamedExpression],
              child: CatalystPlan): EsAggregateExec = {
        new EsAggregateExec(groupingExpressions, aggregateExpressions, child)
    }
}
