package org.apache.spark.sql.optimizer

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical.{Filter, LocalRelation, LogicalPlan}
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.types._

import scala.util.control.Breaks._


/**
  * Removes filters that can be evaluated trivially after Spark PruneFilters and other Optimizers
  * by substituting a dummy empty relation when the filter condition on some attributes
  * will always evaluate to `false`.
  */
case class MbPruneFilters(session: SparkSession) extends Rule[LogicalPlan] with PredicateHelper {

  def apply(plan: LogicalPlan): LogicalPlan =
    try {
      plan transform {
        // 1. Get the conjunctive binary comparison predicates, i.e. (a = 1 and a > 2 and b = 1 and b > 2)
        // 2. Compare same attribute predicates, infer false predicate
        case f@Filter(fc, p: LogicalPlan) =>
          val binaryComparisonPredicates = splitConjunctivePredicates(fc).filter(_.isInstanceOf[BinaryComparison])
          var falseLiteralFlag = false
          breakable {
            for (x <- binaryComparisonPredicates.slice(0, binaryComparisonPredicates.length)) {
              for (y <- binaryComparisonPredicates.slice(1, binaryComparisonPredicates.length + 1)) {
                if (x.children.head.semanticEquals(y.children.head)) {
                  val (l, r) = if (x.prettyName < y.prettyName) (x, y) else (y, x)
                  (l, r) match {
                    case (_ EqualTo l1, _ EqualTo l2) =>
                      if (!l1.semanticEquals(l2)) {
                        falseLiteralFlag = true
                        break
                      }
                    case (_ EqualTo l1, _ EqualNullSafe l2) =>
                      if (l1.semanticEquals(l2)) {
                        falseLiteralFlag = true
                        break
                      }
                    case (_ EqualTo Literal(l1, d1), _ GreaterThan Literal(l2, d2)) =>
                      if (d1.equals(d2) && l1 != null && l2 != null) {
                        if (compareToLiteral(Literal(l1, d1), Literal(l2, d2)) <= 0) {
                          falseLiteralFlag = true
                          break
                        }
                      }
                    case (_ EqualTo Literal(l1, d1), _ GreaterThanOrEqual Literal(l2, d2)) =>
                      if (d1.equals(d2) && l1 != null && l2 != null) {
                        if (compareToLiteral(Literal(l1, d1), Literal(l2, d2)) < 0) {
                          falseLiteralFlag = true
                        }
                      }
                    case (_ EqualTo Literal(l1, d1), _ LessThan Literal(l2, d2)) =>
                      if (d1.equals(d2) && l1 != null && l2 != null) {
                        if (compareToLiteral(Literal(l1, d1), Literal(l2, d2)) >= 0) {
                          falseLiteralFlag = true
                          break
                        }
                      }
                    case (_ EqualTo Literal(l1, d1), _ LessThanOrEqual Literal(l2, d2)) =>
                      if (d1.equals(d2) && l1 != null && l2 != null) {
                        if (compareToLiteral(Literal(l1, d1), Literal(l2, d2)) > 0) {
                          falseLiteralFlag = true
                          break
                        }
                      }
                    case (_ GreaterThan Literal(l1, d1), _ LessThan Literal(l2, d2)) =>
                      if (d1.equals(d2) && l1 != null && l2 != null) {
                        if (compareToLiteral(Literal(l1, d1), Literal(l2, d2)) >= 0) {
                          falseLiteralFlag = true
                          break
                        }
                      }
                    case (_ GreaterThan Literal(l1, d1), _ LessThanOrEqual Literal(l2, d2)) =>
                      if (d1.equals(d2) && l1 != null && l2 != null) {
                        if (compareToLiteral(Literal(l1, d1), Literal(l2, d2)) >= 0) {
                          falseLiteralFlag = true
                          break
                        }
                      }
                    case (_ GreaterThanOrEqual Literal(l1, d1), _ LessThan Literal(l2, d2)) =>
                      if (d1.equals(d2) && l1 != null && l2 != null) {
                        if (compareToLiteral(Literal(l1, d1), Literal(l2, d2)) >= 0) {
                          falseLiteralFlag = true
                          break
                        }
                      }
                    case (_ GreaterThanOrEqual Literal(l1, d1), _ LessThanOrEqual Literal(l2, d2)) =>
                      if (d1.equals(d2) && l1 != null && l2 != null) {
                        if (compareToLiteral(Literal(l1, d1), Literal(l2, d2)) > 0) {
                          falseLiteralFlag = true
                          break
                        }
                      }
                    case _ => //nothing
                  }
                }
              }
            }
          }
          if (falseLiteralFlag) LocalRelation(p.output, data = Seq.empty)
          else f
      }
    } catch {
      case ex: Throwable =>
        log.error(s"MbPruneFilters Optimizer Rule transform on LogicalPlan ${plan.toString()} failed", ex)
        plan
    }

  private def compareToLiteral(left: Literal, right: Literal): Int = {
    if (left == Literal(null, NullType) && right == Literal(null, NullType)) return 0
    if (left == Literal(null, NullType)) return -1
    if (right == Literal(null, NullType)) return 1
    if (!left.dataType.equals(right.dataType))
      throw new IllegalArgumentException(s"Different Type ${left.dataType.simpleString}, ${right.dataType.simpleString} does not support ordered operations")
    left.dataType match {
      case dt: AtomicType =>
        dt.ordering.asInstanceOf[Ordering[Any]].compare(left.value, right.value)
      case a: ArrayType =>
        a.interpretedOrdering.asInstanceOf[Ordering[Any]].compare(left.value, right.value)
      case s: StructType =>
        s.interpretedOrdering.asInstanceOf[Ordering[Any]].compare(left.value, right.value)
      case other =>
        throw new IllegalArgumentException(s"Type $other does not support ordered operations")
    }
  }
}
