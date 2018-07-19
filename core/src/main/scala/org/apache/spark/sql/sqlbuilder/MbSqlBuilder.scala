package org.apache.spark.sql.sqlbuilder

import java.util.concurrent.atomic.AtomicLong

import moonbox.common.MbLogging
import org.apache.spark.sql.catalyst.expressions.aggregate.{AggregateExpression, AggregateFunction}
import org.apache.spark.sql.catalyst.expressions.{Alias, AttributeReference, BinaryOperator, CaseWhenCodegen, Cast, CheckOverflow, Coalesce, Contains, EndsWith, EqualTo, Exists, ExprId, Expression, GetArrayStructFields, GetStructField, If, In, InSet, IsNotNull, IsNull, Like, ListQuery, Literal, MakeDecimal, NamedExpression, Not, ScalarSubquery, SortOrder, StartsWith, StringPredicate, SubqueryExpression, UnscaledValue}
import org.apache.spark.sql.catalyst.optimizer.{CollapseProject, CombineUnions}
import org.apache.spark.sql.catalyst.plans.logical.{GlobalLimit, Intersect, LocalLimit, Union, _}
import org.apache.spark.sql.catalyst.rules.{Rule, RuleExecutor}
import org.apache.spark.sql.catalyst.util.DateTimeUtils
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.execution.datasources.mbjdbc.MbJDBCRelation
import org.apache.spark.sql.types.{TimestampType, _}
import org.apache.spark.unsafe.types.UTF8String

import scala.collection.mutable
import scala.util.control.NonFatal

class MbSqlBuilder(plan: LogicalPlan, dialect: MbDialect) extends MbLogging {
	require(plan.resolved, "LogicalPlan must be resolved.")

	private val nextSubqueryId = new AtomicLong(0)
	private def newSubqueryName(): String = s"gen_subquery_${nextSubqueryId.getAndIncrement()}"
	var finalLogicalPlan: LogicalPlan = finalPlan(plan)

	def toSQL: String = {
		try {
			//println(finalPlan.toString())
			logicalPlanToSQL(finalLogicalPlan)
		} catch { case NonFatal(e) =>
			throw e
		}
	}

	def canonicalize(plan: LogicalPlan): LogicalPlan = Canonicalizer.execute(plan)

	def finalPlan(plan: LogicalPlan): LogicalPlan = {
		val realOutputNames: Seq[String] = plan.output.map(_.name)
		val canonicalizedPlan = canonicalize(plan)
		val canonicalizedToReal = canonicalizedPlan.output.zip(realOutputNames)
		val needRename = canonicalizedToReal.filter {
			case (attr, name) => attr.name != name
		}.toMap
		if (needRename.isEmpty) canonicalizedPlan
		else {
			val afterRenamed = canonicalizedToReal.map {
				case (attr, name) if needRename.contains(attr) =>
					Alias(attr.withQualifier(None), name)()
				case (attr, name) =>
					attr
			}
			Project(afterRenamed, SubqueryAlias(newSubqueryName(), canonicalizedPlan))
		}
	}

	def logicalPlanToSQL(logicalPlan: LogicalPlan): String = logicalPlan match {
		case Distinct(p: Project) =>
			projectToSQL(p, isDistinct = true)
		case p: Project =>
			projectToSQL(p, isDistinct = false)
		case SubqueryAlias(alias, child) =>
			build(s"(${logicalPlanToSQL(child)}) $alias")
		case a: Aggregate =>
			aggregateToSQL(a)
		case w: Window =>
			windowToSQL(w)
		case u: Union =>
			val childrenSQL = u.children.filter {
				case l: LocalRelation if l.data.isEmpty => false
				case _ => true
			}.map(logicalPlanToSQL)
			if (childrenSQL.length > 1) childrenSQL.mkString(" UNION ALL ")
			else childrenSQL.head
		case r: LogicalRelation =>
			dialect.relation(r)
		case OneRowRelation => ""
		case Filter(condition, child) =>
			val whereOrHaving = child match {
				case _: Aggregate => "HAVING"
				case _ => "WHERE"
			}
			build(logicalPlanToSQL(child), whereOrHaving, expressionToSQL(condition))
		case Limit(limitExpr, child) =>
			dialect.limitSQL(logicalPlanToSQL(child), expressionToSQL(limitExpr))
			//s"${logicalPlanToSQL(child)} LIMIT ${expressionToSQL(limitExpr)}"
		case GlobalLimit(limitExpr, child) =>
			dialect.limitSQL(logicalPlanToSQL(child), expressionToSQL(limitExpr))
			//s"${logicalPlanToSQL(child)} LIMIT ${expressionToSQL(limitExpr)}"
		case LocalLimit(limitExpr, child) =>
			dialect.limitSQL(logicalPlanToSQL(child), expressionToSQL(limitExpr))
			//s"${logicalPlanToSQL(child)} LIMIT ${expressionToSQL(limitExpr)}"
		case s: Sort =>
			build(
				logicalPlanToSQL(s.child),
				if (s.global) "ORDER BY" else "SORT BY",
				s.order.map(expressionToSQL).mkString(", ")
			)
		case p: Join =>
			build(
				logicalPlanToSQL(p.left),
				p.joinType.sql,
				"JOIN",
				logicalPlanToSQL(p.right),
				p.condition.map(condition => " ON " + expressionToSQL(condition)).getOrElse(""))
	}

	def expressionToSQL(expression: Expression): String = expression match {
		/*case a@Alias(array@GetArrayStructFields(child, field, _, _, _), name) =>
			val colName = expressionToSQL(array)
			s"$colName AS ${dialect.quote(colName)}"*/
		case a@Alias(child, name) =>
			val qualifierPrefix = a.qualifier.map(_ + ".").getOrElse("")
			s"${expressionToSQL(child)} AS $qualifierPrefix${dialect.quote(name)}"
		case GetStructField(a:AttributeReference, _, Some(name)) =>
			dialect.quote(s"${expressionToSQL(a)}.$name")
		case GetArrayStructFields(child, field, _, _, _) =>
			dialect.quote(s"${expressionToSQL(child)}.${field.name}")
		case a: AttributeReference =>
			val qualifierPrefix = a.qualifier.map(_ + ".").getOrElse("")
			s"$qualifierPrefix${dialect.maybeQuote(a.name)}"
		case c@Cast(child, dataType, _) => dataType match {
			case _: ArrayType | _: MapType | _: StructType => expressionToSQL(child)
			case _: DecimalType => s"CAST(${expressionToSQL(child)} AS ${dataTypeToSQL(dataType)})"
			case _ => expressionToSQL(child)
		}
		case If(predicate, trueValue, falseValue) =>
			// calcite
			s"CASE WHEN ${expressionToSQL(predicate)} THEN ${expressionToSQL(trueValue)} ELSE ${expressionToSQL(falseValue)} END"
		// mysql
		/*
		* s"if(${expressionToSQL(predicate)}, ${expressionToSQL(trueValue)}, ${expressionToSQL(falseValue)})"
		* */
		case IsNull(child) =>
			s"${expressionToSQL(child)} IS NULL"
		case IsNotNull(child) =>
			s"${expressionToSQL(child)} IS NOT NULL"
		case Coalesce(children) =>
			//calcite
			s"coalesce(${children.map(expressionToSQL).mkString(",")})"
		// mysql
		/*children.init.foldRight(expressionToSQL(children.last)){
			case (child, sql) => s"IFNULL(${expressionToSQL(child)}, $sql)"
		}*/
		case CaseWhenCodegen(branches, elseValue)=>
			val cases = branches.map { case (c, v) => s" WHEN ${expressionToSQL(c)} THEN ${expressionToSQL(v)}"}.mkString
			val elseCase = elseValue.map(" ELSE " + expressionToSQL(_)).getOrElse("")
			"CASE" + cases + elseCase + " END"
		case UnscaledValue(child) =>
			expressionToSQL(child)
		case AggregateExpression(aggFunc, _, isDistinct, _) =>
			val distinct = if (isDistinct) "DISTINCT " else ""
			s"${aggFunc.prettyName}($distinct${aggFunc.children.map(expressionToSQL).mkString(", ")})"
		case a: AggregateFunction =>
			s"${a.prettyName}(${a.children.map(expressionToSQL).mkString(", ")})"
		case literal@Literal(v, t) =>
			literalToSQL(v, t)
		case MakeDecimal(child, precision, scala) =>
			s"CAST(${expressionToSQL(child)} AS DECIMAL($precision, $scala))"
		case Not(EqualTo(left, right)) =>
			s"${expressionToSQL(left)} <> ${expressionToSQL(right)}"
		case Not(Like(left, right)) =>
			s"${expressionToSQL(left)} NOT LIKE ${expressionToSQL(right)}"
		case Not(child) =>
			s"(NOT ${expressionToSQL(child)})"
		case In(value, list) =>
			val childrenSQL = (value +: list).map(expressionToSQL)
			val valueSQL = childrenSQL.head
			val listSQL = childrenSQL.tail.mkString(", ")
			s"($valueSQL IN ($listSQL))"
		case InSet(child, hset) =>
			val valueSQL = expressionToSQL(child)
			val listSQL = hset.toSeq.map(s => {
				val literal = s match {
					case v: UTF8String => Literal(v, StringType)
					case v => Literal(v)
				}
				expressionToSQL(Literal(literal))
			}).mkString(", ")
			s"($valueSQL IN ($listSQL))"
		case b: BinaryOperator =>
			s"${expressionToSQL(b.left)} ${b.sqlOperator} ${expressionToSQL(b.right)}"
		case s: StringPredicate =>
			stringPredicate(s)
		case c@ CheckOverflow(child, _) =>
			expressionToSQL(child)
		case s@SortOrder(child, direction, nullOrdering, _) =>
			s"${expressionToSQL(child)} ${direction.sql}"
		case subquery: SubqueryExpression =>
			subqueryExpressionToSQL(subquery)
		case e: Expression =>
			e.sql
	}

	private def windowToSQL(w: Window): String = {
		build(
			"SELECT",
			(w.child.output ++ w.windowOutputSet).map(expressionToSQL).mkString(", "),
			if (w.child == OneRowRelation) "" else "FROM",
			logicalPlanToSQL(w.child)
		)
	}
	private def aggregateToSQL(a: Aggregate): String = {
		val groupingSQL = a.groupingExpressions.map(expressionToSQL).mkString(",")
		val aggregateSQL = if (a.aggregateExpressions.nonEmpty) a.aggregateExpressions.map(expressionToSQL).mkString(", ")
		else if (a.groupingExpressions.nonEmpty) groupingSQL
		else throw new Exception("both aggregateExpression and groupingExpression in Aggregate are empty.")
		//
		build(
			"SELECT",
			aggregateSQL,
			if (a.child == OneRowRelation) "" else "FROM",
			logicalPlanToSQL(a.child),
			if (groupingSQL.isEmpty) "" else "GROUP BY",
			groupingSQL
		)

	}

	private def projectToSQL(p: Project, isDistinct: Boolean): String = {
		// TODO sql
		build(
			"SELECT",
			if (isDistinct) "DISTINCT" else "",
			p.projectList.map(expressionToSQL).mkString(", "),
			if (p.child == OneRowRelation) "" else "FROM",
			logicalPlanToSQL(p.child)
		)
	}

	def subqueryExpressionToSQL(subquery: Expression): String = subquery match {
		case Exists(plan, children, _) =>
			s"EXISTS (${logicalPlanToSQL(finalPlan(plan))})"
		case ScalarSubquery(plan, children, _) =>
			s"(${logicalPlanToSQL(finalPlan(plan))})"
		case ListQuery(plan, children, _) =>
			s"IN (${logicalPlanToSQL(finalPlan(plan))})"
	}

	def dataTypeToSQL(dataType: DataType): String = {
		dataType.sql
	}

	def stringPredicate(s: StringPredicate): String = s match {
		case StartsWith(left, right) =>
			s"${expressionToSQL(left)} LIKE '${expressionToSQL(right).stripPrefix("'").stripSuffix("'")}%'"
		case EndsWith(left, right) =>
			s"${expressionToSQL(left)} LIKE '%${expressionToSQL(right).stripPrefix("'").stripSuffix("'")}'"
		case Contains(left, right) =>
			s"${expressionToSQL(left)} LIKE '%${expressionToSQL(right).stripPrefix("'").stripSuffix("'")}%'"
	}

	def literalToSQL(value: Any, dataType: DataType): String = (value, dataType) match {
		case (_, NullType | _: ArrayType | _: MapType | _: StructType) if value == null => "NULL"
		case (v: UTF8String, StringType) =>  "'" + v.toString.replace("\\", "\\\\").replace("'", "\\'") + "'"
		case (v: Byte, ByteType) => v + ""
		case (v: Boolean, BooleanType) => s"'$v'"
		case (v: Short, ShortType) => v + ""
		case (v: Long, LongType) => v + ""
		case (v: Float, FloatType) => v + ""
		case (v: Double, DoubleType) => v + ""
		case (v: Decimal, t: DecimalType) => v + ""
		case (v: Int, DateType) =>s"'${DateTimeUtils.toJavaDate(v)}'"
		case (v: Long, TimestampType) => s"'${DateTimeUtils.toJavaTimestamp(v)}'"
		case _ => if (value == null) "NULL" else value.toString
	}

	private def build(segments: String*): String =
		segments.map(_.trim).filter(_.nonEmpty).mkString(" ")




	object Canonicalizer extends RuleExecutor[LogicalPlan] {
		override protected def batches: Seq[Batch] = Seq(
			Batch("Prepare", FixedPoint(100),
				CollapseProject,
				CombineUnions,
				EliminateProject,
				EliminateEmptyColumn
			),
			Batch("Recover Scoping Info", Once,
				AddProject,
				AddSubqueryAlias,
				NormalizeAttribute
			)
		)
	}

	object NormalizeAttribute extends Rule[LogicalPlan] {
		override def apply(plan: LogicalPlan): LogicalPlan = {
			plan.transformUp {
				case l@LogicalRelation(jdbc: MbJDBCRelation, output, _) =>
					l.transformExpressions {
						case a: AttributeReference =>
							AttributeReference(
								name = a.name,
								dataType = a.dataType,
								nullable = a.nullable,
								metadata = a.metadata)(
								exprId = a.exprId,
								qualifier = Some(jdbc.jdbcOptions.table))
					}
				case l@LogicalRelation(_, output, _) =>
					l.transformExpressions {
						case a: AttributeReference =>
							AttributeReference(
								name = a.name,
								dataType = a.dataType,
								nullable = a.nullable,
								metadata = a.metadata)(
								exprId = a.exprId,
								qualifier = None)
					}
				case l: LeafNode => l
				case u =>
					val exprIdToQualifier = u.children.flatMap(_.output).map(a => (a.exprId, a.qualifier)).toMap
					u.transformExpressions {
						case a: AttributeReference =>
							AttributeReference(
								name = a.name,
								dataType = a.dataType,
								nullable = a.nullable,
								metadata = a.metadata)(
								exprId = a.exprId,
								qualifier = exprIdToQualifier.getOrElse(a.exprId, None))
					}
			}
		}
	}

	object NormalizedAttribute extends Rule[LogicalPlan] {

		private def findLogicalRelation(plan: LogicalPlan,
			logicalRelations: mutable.ArrayBuffer[LogicalRelation]): Unit = {
			plan.foreach {
				case l: LogicalRelation =>
					logicalRelations.+=(l)
				case Filter(condition, _) =>
					traverseExpression(condition)
				case Project(projectList, _) =>
					projectList.foreach(traverseExpression)
				case Aggregate(groupingExpressions, aggregateExpressions, _) =>
					groupingExpressions.foreach(traverseExpression)
					aggregateExpressions.foreach(traverseExpression)
				case Window(windowExpressions, _, _, _) =>
					windowExpressions.foreach(traverseExpression)
				case _ =>

			}
			def traverseExpression(expr: Expression): Unit = {
				expr.foreach {
					case ScalarSubquery(plan, _, _) => findLogicalRelation(plan, logicalRelations)
					case Exists(plan, _, _) => findLogicalRelation(plan, logicalRelations)
					case ListQuery(plan, _, _) => findLogicalRelation(plan, logicalRelations)
					case _ =>
				}
			}
		}

		override def apply(plan: LogicalPlan): LogicalPlan = {
			val logicalRelations = new mutable.ArrayBuffer[LogicalRelation]()
			findLogicalRelation(plan, logicalRelations)
			val colNames = new mutable.HashSet[String]()
			val conflict = new mutable.HashMap[LogicalRelation, Seq[AttributeReference]]()
			val isGenerated = new mutable.HashSet[LogicalPlan]()
			logicalRelations.foreach { table =>
				val (in, notIn) = table.output.partition(attr => colNames.contains(attr.name))
				if (in.nonEmpty) conflict.put(table, in)
				colNames.++=(notIn.map(_.name))
			}
			val renamedExprId = new mutable.HashSet[ExprId]()

			val plan1 = plan.transformUp {
				case l@LogicalRelation(relation, output, catalogTable) if conflict.contains(l) =>
					val renamedOutput = output.map { attr =>
						if (conflict(l).contains(attr)) {
							renamedExprId.add(attr.exprId)
							Alias(attr, normalizedName(attr))(exprId = attr.exprId, qualifier = None)
						} else AttributeReference(name = attr.name,
							dataType = attr.dataType, nullable = attr.nullable,
							metadata = attr.metadata)(exprId = attr.exprId, qualifier = None)
					}
					val generateProject = Project(renamedOutput, l)
					isGenerated.add(generateProject)
					SubqueryAlias(newSubqueryName(), generateProject)
			}
			plan1.transformUp {
				case l: LogicalRelation =>l
				case p @Project(_, r: LogicalRelation) =>
					if (isGenerated.contains(p)) {
						p
					} else {
						p.transformExpressions {
							case a: AttributeReference =>
								val name = if (renamedExprId.contains(a.exprId)) normalizedName(a) else a.name
								AttributeReference(name, a.dataType)(exprId = a.exprId, qualifier = None)
							case a: Alias =>
								val name = if (renamedExprId.contains(a.exprId)) normalizedName(a) else a.name
								Alias(a.child, name)(exprId = a.exprId, qualifier = None)
						}
					}
				case o => o.transformExpressions {
					case a: AttributeReference =>
						val name = if (renamedExprId.contains(a.exprId)) normalizedName(a) else a.name
						AttributeReference(name, a.dataType)(exprId = a.exprId, qualifier = None)
					case a: Alias =>
						val name = if (renamedExprId.contains(a.exprId)) normalizedName(a) else a.name
						Alias(a.child, name)(exprId = a.exprId, qualifier = None)
				}
			}
		}

		def normalizedName(n: NamedExpression): String = {
			"genattr" + n.exprId.id
		}
	}

	object EliminateProject extends Rule[LogicalPlan] {
		override def apply(plan: LogicalPlan): LogicalPlan = plan transformUp {
			case a@Aggregate(groupingExpressions, aggregateExpressions, p: Project) =>
				a.copy(child = p.child)
			case p1@Project(projectList, s@Sort(_, _, p2: Project)) =>
				Sort(s.order, s.global, Project(p1.projectList, p2.child))
		}
	}

	object EliminateEmptyColumn extends Rule[LogicalPlan] {
		override def apply(plan: LogicalPlan): LogicalPlan = plan transform {
			case a: Aggregate if a.aggregateExpressions.isEmpty =>
				a.child
			case p: Project if p.projectList.isEmpty  =>
				p.child
			case w: Window if w.windowExpressions.isEmpty =>
				w.child
		}
	}

	object AddSubqueryAlias extends Rule[LogicalPlan] {
		override def apply(plan: LogicalPlan): LogicalPlan = {
			val points = new mutable.HashSet[(LogicalPlan, LogicalPlan)]()
			findPoint(plan, points)
			if (points.nonEmpty) {
				plan.transformDown {
					case a =>
						val newChildren = a.children.map(a -> _).map { parentChild =>
							if (points.contains(parentChild)) {
								//points.remove(parentChild)
								SubqueryAlias(newSubqueryName(), parentChild._2)
							} else parentChild._2
						}
						a.withNewChildren(newChildren)
				}
			} else plan
		}

		def findPoint(node: LogicalPlan, points: mutable.HashSet[(LogicalPlan, LogicalPlan)]): Boolean = {
			val hasSelect: Seq[Boolean] = node.children.map(findPoint(_, points))
			node match {
				case l: LeafNode => false
				case p: Project =>
					if (hasSelect.head) {
						points.add(p -> p.child)
						true
					} else true
				case p: Aggregate =>
					if (hasSelect.head) {
						points.add(p -> p.child)
						true
					} else true
				case p: Window =>
					if (hasSelect.head) {
						points.add(p -> p.child)
						true
					} else true
				case p: Generate =>
					if (hasSelect.head) {
						points.add(p -> p.child)
						true
					} else true
				case j@Join(left, right, _, _) =>
					if (hasSelect.head) {
						points.add(j -> left)
					}
					if (hasSelect.last) {
						points.add(j -> right)
					}
					false
				case j@Intersect(left, right) =>
					if (hasSelect.head) {
						points.add(j -> left)
					}
					if (hasSelect.last) {
						points.add(j -> right)
					}
					false
				case u@Union(children) =>
					hasSelect.zip(children).foreach {case (has, p) if has => {
						points.add(u -> p)
					}}
					false
				case a => hasSelect.head
			}
		}
	}

	object AddProject extends Rule[LogicalPlan] {
		private val orderCode = Map[Class[_], Int](
			classOf[LogicalRelation] -> 1,
			classOf[Filter] -> 2,
			classOf[Project] -> 3,
			classOf[Aggregate] -> 4,
			classOf[Sort] -> 5,
			classOf[LocalLimit] -> 6,
			classOf[GlobalLimit] -> 7
		)
		override def apply(plan: LogicalPlan): LogicalPlan = {
			val points = new mutable.HashSet[LogicalPlan]()
			findPoint(plan, plan, points)
			if (points.nonEmpty) {
				plan.transformDown {
					case a if points.contains(a) => {
						points.remove(a)
						Project(a.output, a)
					}
				}
			} else plan
		}

		/**
		  *
		  * @param node current
		  * @param root root
		  * @return (scope , has select)
		  */
		private def findPoint(node: LogicalPlan, root: LogicalPlan, points: mutable.HashSet[LogicalPlan]): (LogicalPlan, Boolean) = {
			val children = node.children.map(child => findPoint(child, root, points))
			node match {
				// has select in scope
				case p: Project =>
					(children.head._1, true)
				case a: Aggregate =>
					(children.head._1, true)
				case w: Window =>
					(children.head._1, true)
				case g: Generate =>
					(children.head._1, true)
				// scope changed
				case j: Join =>
					j.children.zip(children).foreach {
						case (start, state) =>
							if (!start.isInstanceOf[LeafNode]) {
								find(start, state, points)
							}
					}
					if (j == root) find(j, (j, false), points)
					(j, false)
				case u: Union =>
					u.children.zip(children).foreach {
						case (start, state) => if (!start.isInstanceOf[LeafNode]) {
							find(start, state, points)
						}
					}
					if (u == root) find(u, (u, false), points)
					(u, false)
				case i: Intersect =>
					i.children.zip(children).foreach {
						case (start, state) => if (!start.isInstanceOf[LeafNode]) {
							find(start, state, points)
						}
					}
					if (i == root) find(i, (i, false), points)
					(i, false)
				case s: SubqueryAlias =>
					s.children.zip(children).foreach {
						case (start, state) => find(start, state, points)
					}
					(s, false)
				case g: GlobalLimit =>
					g.children.zip(children).foreach {
						case (start, state) => find(start, state, points)
					}
					(g, false)
				case a => {
					val res = children.headOption
					if (res.isDefined) {
						if (a == root) {
							a.children.zip(children).foreach {
								case (start, state) => find(start, state, points)
							}
						}
						res.get
					}
					else {
						if (a == root) find(a, (a, false), points)
						(a, false)
					}
				}
				//
			}

		}
		private def find(start: LogicalPlan, state: (LogicalPlan, Boolean), points: mutable.HashSet[LogicalPlan]): Unit = {
			val hasSelect = state._2
			if (!hasSelect) {
				var flag = true
				var current = start
				val until = state._1
				while (flag) {
					if (current == until) {
						flag = false
						points.add(current)
					} else {
						if (orderCode(current.getClass) < orderCode(classOf[Project])) {
							points.add(current)
							flag = false
						} else {
							current = current.children.head
						}
					}
				}
			}
		}
	}


}


