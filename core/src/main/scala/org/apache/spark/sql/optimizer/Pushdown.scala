package org.apache.spark.sql.optimizer

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.expressions.{AttributeReference, EqualTo}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.datasys.{DataSystemFactory, SparkDataSystem}
import org.apache.spark.sql.execution.datasources.LogicalRelation
import scala.collection.mutable

case class Pushdown(sparkSession: SparkSession) extends Rule[LogicalPlan]{
	override def apply(plan: LogicalPlan): LogicalPlan = {
		val graph = buildDataSystemTree(plan)
		val points = findReplacePoint(graph)
		replacePushdownSubtree(graph, points)
	}

	private def findReplacePoint(graph: MbTreeNode): mutable.HashSet[MbTreeNode] = {
		val parents = new mutable.HashSet[MbTreeNode]()
		val visited = new mutable.HashSet[MbTreeNode]()
		val waitingForVisit = new mutable.Stack[MbTreeNode]()
		waitingForVisit.push(graph)
		while (waitingForVisit.nonEmpty) {
			val toVisit = waitingForVisit.pop()
			if (!visited(toVisit)) {
				toVisit.dependencies.foreach {
					case CrossDependency(node) =>
						parents += node
					case SameDependency(node) =>
						waitingForVisit.push(node)
				}
			}
		}
		if (parents.isEmpty && !graph.dataSystem.isInstanceOf[SparkDataSystem]) {
			parents.add(graph)
		}
		parents.filter(n => n.plan.find(l => n.dataSystem.isGoodAt(l.getClass)).isDefined)
	}

	private def replacePushdownSubtree(graph: MbTreeNode, points: mutable.HashSet[MbTreeNode]): LogicalPlan = {
		graph.plan.transformDown {
			case logicalPlan: LogicalPlan if points.map(_.plan).contains(logicalPlan) =>
				val treeNode = points.find(_.plan.equals(logicalPlan)).get
				treeNode.dataSystem.buildScan(treeNode.plan).logicalPlan
		}
	}

	private def buildDataSystemTree(plan: LogicalPlan): MbTreeNode = {
		val indexes = new mutable.HashSet[String]
		val childrenTreeNode = plan.children.map(buildDataSystemTree)
		plan match {
			case logical@LogicalRelation(relation, output, Some(catalogTable)) =>
				// TODO JdbcRelation index
				MbTreeNode(logical,
					DataSystemFactory.getInstance(catalogTable.storage.properties, sparkSession), Nil)
			case leaf: LeafNode =>
				MbTreeNode(leaf, new SparkDataSystem(sparkSession), Nil)
			case unary: UnaryNode =>
				if (childrenTreeNode.head.dataSystem.isSupport(unary)) {
					MbTreeNode(unary, childrenTreeNode.head.dataSystem, childrenTreeNode.map(SameDependency))
				} else {
					MbTreeNode(unary, new SparkDataSystem(sparkSession), childrenTreeNode.map(CrossDependency))
				}
			case join@Join(left, right, joinType, condition) =>
				val left = childrenTreeNode.head.dataSystem
				val right = childrenTreeNode.tail.head.dataSystem
				if (left.fastEquals(right)) {
					if (left.isSupport(join)) {
						val sqlConf = sparkSession.sessionState.conf
						val leftChildCost = join.left.stats(sqlConf)
						val rightChildCost = join.right.stats(sqlConf)
						if (costUnderExpected(leftChildCost, rightChildCost) ||
							costConsiderIndexUnderExpected(join, leftChildCost, rightChildCost, indexes)) {
							MbTreeNode(join, left, childrenTreeNode.map(SameDependency))
						} else {
							MbTreeNode(join, new SparkDataSystem(sparkSession), childrenTreeNode.map(SameDependency))
						}
					} else {
						MbTreeNode(join, new SparkDataSystem(sparkSession), childrenTreeNode.map(SameDependency))
					}
				} else {
					val leftDependency = if (left.isInstanceOf[SparkDataSystem]) SameDependency else CrossDependency
					val rightDependency = if (right.isInstanceOf[SparkDataSystem]) SameDependency else CrossDependency
					MbTreeNode(
						join,
						new SparkDataSystem(sparkSession),
						childrenTreeNode.zip(Seq(leftDependency, rightDependency)).map {case (n, f) => f(n)})
				}
			case binary: BinaryNode =>
				val left = childrenTreeNode.head.dataSystem
				val right = childrenTreeNode.tail.head.dataSystem
				if (left.fastEquals(right)) {
					if (left.isSupport(binary)) {
						MbTreeNode(binary, left, childrenTreeNode.map(SameDependency))
					} else {
						MbTreeNode(binary, new SparkDataSystem(sparkSession), childrenTreeNode.map(CrossDependency))
					}
				} else {
					val leftDependency = if (left.isInstanceOf[SparkDataSystem]) SameDependency else CrossDependency
					val rightDependency = if (right.isInstanceOf[SparkDataSystem]) SameDependency else CrossDependency
					MbTreeNode(
						binary,
						new SparkDataSystem(sparkSession),
						childrenTreeNode.zip(Seq(leftDependency, rightDependency)).map {case (n, f) => f(n)}
					)
				}
			case union@Union(children) =>
				if (dataSystemEquals(childrenTreeNode) && childrenTreeNode.head.dataSystem.isSupport(union)) {
					MbTreeNode(union, childrenTreeNode.head.dataSystem, childrenTreeNode.map(SameDependency))
				} else {
					MbTreeNode(union, new SparkDataSystem(sparkSession), childrenTreeNode.map { node =>
						if (node.dataSystem.isInstanceOf[SparkDataSystem])
							SameDependency(node)
						else CrossDependency(node)
					})
				}
			case _ =>
				MbTreeNode(plan, new SparkDataSystem(sparkSession), childrenTreeNode.map(CrossDependency))
		}
	}

	private def costConsiderIndexUnderExpected(join: Join, left: Statistics, right: Statistics, indexes: mutable.HashSet[String]): Boolean = {
		val cost: Option[BigInt] = join.condition.flatMap {
			case EqualTo(l: AttributeReference, r: AttributeReference) if indexes.contains(l.name) && indexes.contains(r.name) =>
				for (outer <- left.rowCount; inner <- right.rowCount) yield {
					if (inner < outer) inner
					else outer
				}
			case EqualTo(l: AttributeReference, r: AttributeReference) if indexes.contains(l.name) =>
				right.rowCount
			case EqualTo(l: AttributeReference, r: AttributeReference) if indexes.contains(r.name) =>
				left.rowCount
			case EqualTo(l: AttributeReference, r: AttributeReference) =>
				for (outer <- left.rowCount; inner <- right.rowCount) yield outer * inner
			case _ => Option(BigInt(Long.MaxValue))
		}
		costJudge(cost)
	}

	private def costUnderExpected(left: Statistics, right: Statistics): Boolean = {
		val cost: Option[BigInt] = for (outer <- left.rowCount; inner <- right.rowCount) yield outer * inner
		costJudge(cost)
	}

	private def costJudge(cost: Option[BigInt]): Boolean = {
		cost.getOrElse(BigInt(Long.MaxValue)) <= BigInt(1E10.toLong)
	}

	private def dataSystemEquals(children: Seq[MbTreeNode]): Boolean = {
		children.tail.forall { node =>
			node.dataSystem.fastEquals(children.head.dataSystem)
		}
	}
}
