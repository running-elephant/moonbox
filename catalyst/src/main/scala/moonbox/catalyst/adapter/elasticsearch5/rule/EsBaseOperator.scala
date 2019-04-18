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

package moonbox.catalyst.adapter.elasticsearch5.rule

import moonbox.catalyst.adapter.elasticsearch5.plan._
import moonbox.catalyst.core.Strategy
import moonbox.catalyst.core.plan._
import org.apache.spark.sql.catalyst.catalog.CatalogRelation
import org.apache.spark.sql.catalyst.expressions.IntegerLiteral
import org.apache.spark.sql.catalyst.plans._
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.datasources.LogicalRelation

object EsBaseOperator extends Strategy{

    override def apply(p: LogicalPlan): Seq[CatalystPlan] = {
        p match {
            case logical.Sort(sortExprs, global, child) =>
                EsSortExec(sortExprs, global, planLater(child)) :: Nil
            case logical.Project(projectList, child) =>
                EsProjectExec(projectList, planLater(child)) :: Nil
            case logical.Filter(condition, child) =>
                EsFilterExec(condition, planLater(child)) :: Nil
            case logical.Aggregate(groupingExpressions, aggregateExpressions, child) =>
                EsAggregateExec(groupingExpressions, aggregateExpressions, planLater(child)) :: Nil
            case logical.Limit(IntegerLiteral(limit), child) =>
                EsLimitExec(limit, planLater(child)) :: Nil
            case LogicalRelation(relation, output, catalogTable) =>
                EsRowDataSourceScanExec(output, null, relation, null, null, null) :: Nil
            case logical.LocalRelation(output, data) =>
                EsTableScanExec(output, data) :: Nil
            case CatalogRelation(tableMeta, dataCols, partitionCols) =>
                EsTableScanExec(dataCols, null) :: Nil
            case e: Any =>
                throw new Exception(s"unknow type BaseOperator $e")
        }
    }

    def test(p: LogicalPlan): Seq[CatalystPlan] = {
        val a = EsBaseOperator(p)
        a
    }
}
