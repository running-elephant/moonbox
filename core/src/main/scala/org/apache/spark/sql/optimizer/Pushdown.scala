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

package org.apache.spark.sql.optimizer

import moonbox.core.datasys.{DataSystem, Pushdownable}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.expressions.{AttributeReference, EqualTo, ExprId}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.datasys.SparkDataSystem
import org.apache.spark.sql.execution.datasources.LogicalRelation

import scala.collection.mutable

object Pushdown {
	def apply(sparkSession: SparkSession): Pushdown = new Pushdown(sparkSession)
}

class Pushdown(sparkSession: SparkSession) extends Rule[LogicalPlan] {
	override def apply(plan: LogicalPlan): LogicalPlan = {
		val graph = buildDataSystemTree(plan)
		val (whole, partial) = findReplacePoint(graph)
		if (whole.isDefined) {
			wholePushdown(whole.get)
		} else {
			replacePushdownSubtree(graph, partial)
		}
	}

	private def findReplacePoint(graph: MbTreeNode): (Option[MbTreeNode], mutable.HashSet[MbTreeNode]) = {
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

		if (parents.isEmpty && !graph.dataSystem.isInstanceOf[SparkDataSystem] && graph.dataSystem.isInstanceOf[Pushdownable]
			&& (graph.plan.find(l => graph.dataSystem.asInstanceOf[Pushdownable].isGoodAt(l.getClass)).isDefined ||
			graph.dataSystem.asInstanceOf[Pushdownable].isSupportAll)
			&& !graph.dataSystem.isInstanceOf[SparkDataSystem]
		) {
			(Some(graph), mutable.HashSet[MbTreeNode]())
		} else {
			val partial = parents.filter(n => n.dataSystem.isInstanceOf[Pushdownable] && (
				n.dataSystem.asInstanceOf[Pushdownable].isSupportAll ||
					n.plan.find(l => n.dataSystem.asInstanceOf[Pushdownable].isGoodAt(l.getClass)).isDefined)
			)
			(None, partial)
		}
	}

	private def wholePushdown(graph: MbTreeNode): LogicalPlan = {
		WholePushdown(graph.plan, graph.dataSystem.asInstanceOf[Pushdownable])
	}

	private def replacePushdownSubtree(graph: MbTreeNode, points: mutable.HashSet[MbTreeNode]): LogicalPlan = {
		logInfo(s"\n===== Old logical plan before partial push down =====\n${graph.plan.toString()}")
		val newPlan = graph.plan.transformDown {
			case logicalPlan: LogicalPlan if points.map(_.plan).contains(logicalPlan) =>
				val treeNode = points.find(_.plan.equals(logicalPlan)).get
				val oldAttrs = logicalPlan.output
				val plan2 = treeNode.dataSystem.asInstanceOf[Pushdownable].buildScan(treeNode.plan, sparkSession).logicalPlan
				val newAttrs = plan2.output
				val newIdToOldId = mutable.Map.empty[ExprId, ExprId]
				newAttrs.zip(oldAttrs).foreach(elem => newIdToOldId += (elem._1.exprId -> elem._2.exprId))
				plan2.transformExpressions {
					case a: AttributeReference =>
						if (newIdToOldId.contains(a.exprId)) {
							a.copy()(exprId = newIdToOldId(a.exprId), a.qualifier, a.isGenerated)
						} else a
				}
		}
		logInfo(s"\n===== New logical plan with partial push down =====\n${newPlan.toString()}")
		newPlan
	}

	private def buildDataSystemTree(plan: LogicalPlan): MbTreeNode = {
		val indexes = new mutable.HashSet[String]
		val childrenTreeNode = plan.children.map(buildDataSystemTree)
		plan match {
			case logical@LogicalRelation(relation, output, Some(catalogTable)) =>
				MbTreeNode(logical,
					DataSystem.lookupDataSystem(catalogTable.storage.properties), Nil)
			case leaf: LeafNode =>
				MbTreeNode(leaf, new SparkDataSystem(), Nil)
			case unary: UnaryNode =>
				childrenTreeNode.head.dataSystem match {
					case pushdown: Pushdownable if pushdown.isSupport(unary) =>
						MbTreeNode(unary, childrenTreeNode.head.dataSystem, childrenTreeNode.map(SameDependency))
					case _ =>
						MbTreeNode(unary, new SparkDataSystem(), childrenTreeNode.map(CrossDependency))
				}
			case join@Join(left, right, joinType, condition) =>
				val left = childrenTreeNode.head.dataSystem
				val right = childrenTreeNode.tail.head.dataSystem
				(left, right) match {
					case (l: Pushdownable, r: Pushdownable) if l.fastEquals(r) =>
						if (l.isSupport(join)) {
							val sqlConf = sparkSession.sessionState.conf
							val leftChildCost = join.left.stats(sqlConf)
							val rightChildCost = join.right.stats(sqlConf)
							if (costUnderExpected(leftChildCost, rightChildCost) ||
								costConsiderIndexUnderExpected(join, leftChildCost, rightChildCost, indexes)) {
								MbTreeNode(join, left, childrenTreeNode.map(SameDependency))
							} else {
								MbTreeNode(join, new SparkDataSystem(), childrenTreeNode.map(SameDependency))
							}
						} else {
							MbTreeNode(join, new SparkDataSystem(), childrenTreeNode.map(SameDependency))
						}
					case _ =>
						val leftDependency = if (left.isInstanceOf[SparkDataSystem]) SameDependency else CrossDependency
						val rightDependency = if (right.isInstanceOf[SparkDataSystem]) SameDependency else CrossDependency
						MbTreeNode(
							join,
							new SparkDataSystem(),
							childrenTreeNode.zip(Seq(leftDependency, rightDependency)).map { case (n, f) => f(n) })
				}
			case binary: BinaryNode =>
				val left = childrenTreeNode.head.dataSystem
				val right = childrenTreeNode.tail.head.dataSystem
				(left, right) match {
					case (l: Pushdownable, r: Pushdownable) if l.fastEquals(r) =>
						if (l.isSupport(binary)) {
							MbTreeNode(binary, left, childrenTreeNode.map(SameDependency))
						} else {
							MbTreeNode(binary, new SparkDataSystem(), childrenTreeNode.map(CrossDependency))
						}
					case _ =>
						val leftDependency = if (left.isInstanceOf[SparkDataSystem]) SameDependency else CrossDependency
						val rightDependency = if (right.isInstanceOf[SparkDataSystem]) SameDependency else CrossDependency
						MbTreeNode(
							binary,
							new SparkDataSystem(),
							childrenTreeNode.zip(Seq(leftDependency, rightDependency)).map { case (n, f) => f(n) }
						)
				}
			case union@Union(children) =>
				if (dataSystemEquals(childrenTreeNode) && childrenTreeNode.head.dataSystem.asInstanceOf[Pushdownable].isSupport(union)) {
					MbTreeNode(union, childrenTreeNode.head.dataSystem, childrenTreeNode.map(SameDependency))
				} else {
					MbTreeNode(union, new SparkDataSystem(), childrenTreeNode.map { node =>
						if (node.dataSystem.isInstanceOf[SparkDataSystem])
							SameDependency(node)
						else CrossDependency(node)
					})
				}
			case _ =>
				MbTreeNode(plan, new SparkDataSystem(), childrenTreeNode.map(CrossDependency))
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
			(node.dataSystem, children.head.dataSystem) match {
				case (l: Pushdownable, r: Pushdownable) =>
					l.fastEquals(r)
				case _ => false
			}
		}
	}

}
