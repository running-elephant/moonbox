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

package moonbox.catalyst.adapter.mongo

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{AggregateExec, CatalystPlan}
import org.apache.spark.sql.catalyst.expressions.aggregate.AggregateExpression
import org.apache.spark.sql.catalyst.expressions.{Alias, AttributeReference, Expression, NamedExpression}

class MongoAggregateExec(groupingExpressions: Seq[Expression],
                         aggregateExpressions: Seq[NamedExpression],
                         child: CatalystPlan) extends AggregateExec(groupingExpressions: Seq[Expression],
  aggregateExpressions: Seq[NamedExpression],
  child: CatalystPlan) with MongoTranslateSupport {
  override def translate(context: CatalystContext) = {

    var fieldsInId = Seq[String]()
    var fieldsInGroup = Seq[String]()
    var additionalProjectFields = Seq[(String, String, Boolean)]() // _1: alias name, _2: field name, _3: [true notes that the field is in _id, and vise versa]

    if (groupingExpressions.nonEmpty) {
      fieldsInId = groupingExpressions.map {
        case a: AttributeReference =>
          a.name + """: "$""" + a.name + """""""
      }
    }

    if (aggregateExpressions.nonEmpty) {
      for (aggregateExpression <- aggregateExpressions) {
        aggregateExpression match {
          case a: AttributeReference =>
            //            groupingFieldNames :+= a.name
            additionalProjectFields :+= (a.name, a.name, true)
          //            fieldsInId :+= (a.name + """: "$""" + a.name + """"""")
          case a@Alias(aChild, name) =>
            aChild match {
              case e: AttributeReference =>
                //                groupingFieldNames :+= name
                additionalProjectFields :+= (name, e.name, true)
              //                fieldsInId :+= (name + """: "$""" + e.name + """"""")
              case _: AggregateExpression =>
                additionalProjectFields :+= (name, name, false)
                fieldsInGroup :+= s"${withQuotes(name)}: ${expressionToBson(aChild)}"
            }
          case _ =>
            throw new Exception("AggregateExpression match exception")
        }
      }
    }

    val aggregateBson = fieldsInId.mkString("{$group: {_id: {", ", ", "}") + ", " + fieldsInGroup.mkString(", ") + "}}"

    val res: Seq[String] = child.translate(context) :+ aggregateBson :+ additionalProjectBson(additionalProjectFields)

    res
  }

  def additionalProjectBson(tuples: Seq[(String, String, Boolean)]): String = {
    var containsId: Boolean = false
    val fields = tuples.map { tuple =>
      if (tuple._1 == "_id")
        containsId = true
      if (tuple._3) {
        s"${tuple._1}: " + withQuotes("""$_id.""" + tuple._2)
      } else {
        withQuotes(tuple._1) + """: 1"""
      }
    }
    if (containsId)
      """{$project: {""" + fields.mkString(", ") + """}}"""
    else
      """{$project: {_id: 0, """ + fields.mkString(", ") + """}}"""
  }

}
