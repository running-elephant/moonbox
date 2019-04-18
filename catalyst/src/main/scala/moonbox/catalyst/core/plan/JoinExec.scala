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

package moonbox.catalyst.core.plan

import org.apache.spark.sql.catalyst.expressions.{Attribute, Expression}
import org.apache.spark.sql.catalyst.plans.{ExistenceJoin, LeftExistence, _}

abstract case class JoinExec(leftKeys: Seq[Expression],
							 rightKeys: Seq[Expression],
							 joinType: JoinType,
							 condition: Option[Expression],
							 left: CatalystPlan,
							 right: CatalystPlan) extends BinaryExecNode {
	override def output: Seq[Attribute] = {
		joinType match {
			case _: InnerLike =>
				left.output ++ right.output
			case LeftOuter =>
				left.output ++ right.output.map(_.withNullability(true))
			case RightOuter =>
				left.output.map(_.withNullability(true)) ++ right.output
			case FullOuter =>
				(left.output ++ right.output).map(_.withNullability(true))
			case j: ExistenceJoin =>
				left.output :+ j.exists
			case LeftExistence(_) =>
				left.output
			case x =>
				throw new IllegalArgumentException(
					s"${getClass.getSimpleName} should not take $x as the JoinType")
		}
	}
}
