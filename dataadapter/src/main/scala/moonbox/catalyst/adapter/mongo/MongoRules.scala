package moonbox.catalyst.adapter.mongo

import moonbox.catalyst.adapter.mongo.util.MongoJDBCUtils
import moonbox.catalyst.core.Strategy
import moonbox.catalyst.core.plan.CatalystPlan
import org.apache.spark.sql.catalyst.catalog.CatalogRelation
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.LogicalRelation

object MongoRules {
  val rules: Seq[Strategy] = Seq(Logical2Exec)

  object Logical2Exec extends Strategy {
    override def apply(plan: LogicalPlan): Seq[CatalystPlan] = {
      plan match {
        case p: Project => new MongoProjectExec(p.projectList, planLater(p.child)) :: Nil
        case a: Aggregate => new MongoAggregateExec(a.groupingExpressions, a.aggregateExpressions, planLater(a.child)) :: Nil
        case f: Filter => new MongoFilterExec(f.condition, planLater(f.child)) :: Nil
        case l@Limit(_, _) => new MongoLimitExec(l.limitExpr.eval().asInstanceOf[Int], planLater(l.child)) :: Nil
        case l@GlobalLimit(_, _) => new MongoLimitExec(l.limitExpr.eval().asInstanceOf[Int], planLater(l.child)) :: Nil
        case l@LocalLimit(_, _) => new MongoLimitExec(l.limitExpr.eval().asInstanceOf[Int], planLater(l.child)) :: Nil
        case s: Sort => new MongoSortExec(s.order, s.global, planLater(s.child)) :: Nil
        case p: Join => throw new Exception("Join is temporarily unsupported") // TODO: $lookup ? since version_3.2
        case Distinct(p: Project) => throw new Exception("Distinct is temporarily unsupported")
        case SubqueryAlias(_, _) => throw new Exception("Subquery is temporarily unsupported")
        case LogicalRelation(_, output, _) => new MongoTableScanExec(output, null) :: Nil
        case r: LocalRelation => new MongoTableScanExec(r.output, r.data) :: Nil
        case t: CatalogRelation => new MongoTableScanExec(t.output, null) :: Nil
        case _ => Nil
        //      case OneRowRelation =>
        //      case w: Window =>
        //      case u: Union =>
      }
    }
  }

}
