/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.core.split

import edp.moonbox.common.EdpLogging
import org.apache.spark.sql.catalyst.catalog.CatalogRelation
import org.apache.spark.sql.catalyst.expressions.AttributeReference
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.{PushDownTable, ResultTable, SparkSession}

import scala.collection.mutable
import scala.collection.mutable.{HashSet, Stack}


class MbSpliter(sparkSession: SparkSession) extends EdpLogging {
	import sparkSession.sessionState._
	def split(sql: String): (Option[ResultTable], Seq[PushDownTable]) = {
		val graph: Node = buildDependencyGraph(optimizedLogicalPlan(sql))
		val pushDownTables: mutable.HashSet[PushDownTable] = findPushDown(graph)
		val resultTable = findResult(graph, pushDownTables)
		(resultTable, pushDownTables.toSeq)
	}

	def parsedLogicalPlan(sql: String): LogicalPlan = sqlParser.parsePlan(sql)

	def analyzedLogicalPlan(sql: String): LogicalPlan = analyzer.execute(parsedLogicalPlan(sql))

	def optimizedLogicalPlan(sql: String): LogicalPlan = optimizer.execute(analyzedLogicalPlan(sql))

	def buildDependencyGraph(plan: LogicalPlan): Node = { // Expression pushdown ?

		val dependency: Seq[Node] = plan.children.map(buildDependencyGraph)
		plan match {
			// leaf node
			case l@LogicalRelation(relation, output , catalogTable) =>
				Node(l, PlatformFactory.newInstance(catalogTable.get.storage.properties), Nil)
			// TODO
			case l@LocalRelation(_, _) =>
				println()
				Node(l, new SparkPlatform, Nil)
			case c@CatalogRelation(catalog, _, _) =>
				println()
				Node(c, new SparkPlatform, Nil)
			case r: Range =>
				println()
				Node(r, new SparkPlatform, Nil)
			case o : OneRowRelation.type =>
				println()
				Node(o, new SparkPlatform, Nil)
			//unary node
			case u: UnaryNode => if (dependency.head.platform.isSupport(u))
				Node(u, dependency.head.platform, dependency.map(SamePlatformDependency))
			else
				Node(u, new SparkPlatform, dependency.map(CrossPlatformDependency))
			// binary node
			case b: BinaryNode =>
				val left = dependency.head.platform
				val right = dependency.tail.head.platform
				// TODO
				if (left.equals(right)) {
					if (left.isSupport(b)) { // left and right all support
						Node(plan, left, dependency.map(SamePlatformDependency))
					} else {// left and right all unsupport
						Node(plan, new SparkPlatform, dependency.map(CrossPlatformDependency))
					}
				} else {
					val leftDep = if (left.isInstanceOf[SparkPlatform]) SamePlatformDependency else CrossPlatformDependency
					val rightDep = if (left.isInstanceOf[SparkPlatform]) SamePlatformDependency else CrossPlatformDependency
					Node(plan, new SparkPlatform, dependency.zip(Seq(leftDep, rightDep)).map {case (n, f) => f(n)})
				}

			case u: Union =>
				// TODO
				if (platformEquals(dependency.toList) && dependency.head.platform.isSupport(u))
					Node(u, dependency.head.platform, dependency.map(SamePlatformDependency))
				else Node(u, new SparkPlatform, dependency.map( dep => {
					if (dep.platform.isInstanceOf[SparkPlatform]) SamePlatformDependency(dep)
					else CrossPlatformDependency(dep)
				}))
			case a =>
				Node(plan, new SparkPlatform, dependency.map(CrossPlatformDependency))
		}

	}

	def findResult(node: Node, splitPoint: HashSet[PushDownTable]): Option[ResultTable] = {
		if (splitPoint.isEmpty) { // no pushdown
			Some(ResultTable(node.plan, new SparkPlatform))
		} else if (splitPoint.map(_.plan).contains(node.plan)) { // whole pushdown
			assert(splitPoint.size == 1)
			None
		} else {
			val logicalPlan = node.plan.transformDown {
				case p: LogicalPlan if splitPoint.map(_.plan).contains(p) =>
					val table: Option[PushDownTable] = splitPoint.find( t => t.plan.equals(p))
					//UnresolvedRelation(TableIdentifier(table.get.name))
					//UnresolvedRelation(TableIdentifier(table.get.name, Some(sparkSession.catalog.currentDatabase)))
					PushDownReplaced(table.get.name, p.output.map(attr =>AttributeReference(attr.name, attr.dataType)(attr.exprId, attr.qualifier)))
			}
			Some(ResultTable(logicalPlan, node.platform))
		}
	}

	def findPushDown(node: Node): HashSet[PushDownTable] = {
		val parents = new HashSet[PushDownTable]
		val visited = new HashSet[Node]
		val waitingForVisit = new Stack[Node]
		waitingForVisit.push(node)
		while (waitingForVisit.nonEmpty) {
			val toVisit = waitingForVisit.pop()
			if (!visited(toVisit)) {
				visited += toVisit
				toVisit.dep.foreach {
					case hasChanged@CrossPlatformDependency(splitNode) =>
						parents += PushDownTable(splitNode.plan, splitNode.platform, false)
					case notChanged: SamePlatformDependency =>
						waitingForVisit.push(notChanged.node)
				}
			}
		}
		if (parents.isEmpty) {
			parents.add(PushDownTable(node.plan, node.platform, true))
		}
		// TODO optimize
		parents.filter(n => n.plan.find(l => n.platform.beGoodAt.contains(l.getClass)).isDefined)
	}

	private def platformEquals(platforms: List[Node]): Boolean = {
		platforms match {
			case Nil => true
			case head :: Nil => true
			case head :: tail => head.platform.equals(tail.head.platform) && platformEquals(tail.tail)
		}
	}
}
