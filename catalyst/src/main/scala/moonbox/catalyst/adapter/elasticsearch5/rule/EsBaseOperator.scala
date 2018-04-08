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
