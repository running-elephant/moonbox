package moonbox.security.strategy

import moonbox.core.CatalogContext
import moonbox.core.catalog.CatalogStrategy
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.expressions.{Alias, AttributeReference, Expression, NamedExpression}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.LogicalRelation

import scala.collection.mutable

object StrategyPlanner {

  def builtinRegister(sparkSession: SparkSession): Unit = {
    sparkSession.udf.register(Mask.name, Mask.function)
    sparkSession.udf.register(Truncate.name, Truncate.function)
    sparkSession.udf.register(Hash.name, Hash.function)
    sparkSession.udf.register(HideString.name, HideString.function)
    sparkSession.udf.register(HideBoolean.name, HideBoolean.function)
    sparkSession.udf.register(HideNumber.name, HideNumber.function)
    sparkSession.udf.register(HideByte.name, HideByte.function)
    sparkSession.udf.register(HideShort.name, HideShort.function)
    sparkSession.udf.register(HideInt.name, HideInt.function)
    sparkSession.udf.register(HideLong.name, HideLong.function)
    sparkSession.udf.register(HideFloat.name, HideFloat.function)
    sparkSession.udf.register(HideDouble.name, HideDouble.function)
    sparkSession.udf.register(Shift.name, Shift.function)
    sparkSession.udf.register(ShiftByte.name, ShiftByte.function)
    sparkSession.udf.register(ShiftShort.name, ShiftShort.function)
    sparkSession.udf.register(ShiftInt.name, ShiftInt.function)
    sparkSession.udf.register(ShiftLong.name, ShiftLong.function)
    sparkSession.udf.register(ShiftFloat.name, ShiftFloat.function)
    sparkSession.udf.register(ShiftDouble.name, ShiftDouble.function)
  }

  def applyStrategyToPlan(sparkSession: SparkSession, catalog: CatalogContext, analyzedPlan: LogicalPlan): LogicalPlan = {
    val oldExpr2New = new mutable.HashMap[String, NamedExpression]()
    val hs = new mutable.HashSet[LogicalPlan]()
    findViewAndTable(analyzedPlan, hs)
    if (hs.isEmpty) {
      analyzedPlan
    } else {
      analyzedPlan.transformUp {
        case view: View =>
          if (hs.contains(view)) {
            val dbName = view.desc.identifier.database.getOrElse("default")
            val viewName = view.desc.identifier.table
            val strategies = catalog.getStrategy(dbName, viewName)
            if (strategies.nonEmpty) {
              val projectList = view.output.map { attr =>
                val s = strategies.filter(attr.name == _.columnName)
                if (s.nonEmpty) {
                  addStrategy(sparkSession, attr, s.last, oldExpr2New)
                } else attr
              }
              Project(projectList, view)
            } else view
          } else view
        case relation: LogicalRelation =>
          if (hs.contains(relation)) {
            val (dbName, tableName) = relation.catalogTable match {
              case Some(table) =>
                (table.identifier.database.getOrElse("default"), table.identifier.table)
              case None => throw new Exception("Relation unCanonicalized.")
            }
            val strategies = catalog.getStrategy(dbName, tableName)
            if (strategies.nonEmpty) {
              val projectList = relation.output.map { attr =>
                val s = strategies.filter(attr.name == _.columnName)
                if (s.nonEmpty) {
                  addStrategy(sparkSession, attr, s.last, oldExpr2New)
                } else attr
              }
              Project(projectList, relation)
            } else relation
          } else relation
        case other => other.mapExpressions(mapExpr(oldExpr2New))
      }
    }
  }

  def findViewAndTable(logicalPlan: LogicalPlan, hashSet: mutable.HashSet[LogicalPlan]): Unit = {
    logicalPlan match {
      case view: View => hashSet.add(view)
      case relation: LogicalRelation => hashSet.add(relation)
//      case union: Union => union.children.foreach(child => findViewAndTable(child, hashSet))
//      case binary: BinaryNode => binary.children.foreach(child => findViewAndTable(child, hashSet))
//      case unary: UnaryNode => findViewAndTable(unary.child, hashSet)
      case other => other.children.foreach(child => findViewAndTable(child, hashSet))
    }
  }

  def mapExpr(oldExpr2New: mutable.HashMap[String, NamedExpression]): Expression => Expression = {
    case a: AttributeReference if oldExpr2New.contains(a.name + a.exprId) => a.copy()(exprId = oldExpr2New(a.name + a.exprId).exprId, a.qualifier, a.isGenerated)
    case other => other.mapChildren(mapExpr(oldExpr2New))
  }

  /**
    *
    * @param sparkSession
    * @param expr
    * @param strategy
    * @param changeRecoder " 'oldExpressionName + oldExpressionId' -> newExpression"
    * @return
    */
  private def addStrategy(sparkSession: SparkSession, expr: NamedExpression, strategy: CatalogStrategy, changeRecoder: mutable.HashMap[String, NamedExpression]): NamedExpression = {
    val colName = expr.name
    val strategyName = strategy.name
    val strategyParam = strategy.param
    val strategyExpression = strategyParam match {
      case Some(param) =>
        sparkSession.sessionState.sqlParser.parseExpression(s"$strategyName($colName, '$param')")
      case None => sparkSession.sessionState.sqlParser.parseExpression(s"$strategyName($colName)")
    }
    val alias = Alias(strategyExpression, colName)()
    changeRecoder += (expr.name + expr.exprId -> alias)
    alias
  }
}