/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

package org.apache.spark.sql.datasys

import moonbox.core.datasys._
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

class SparkDataSystem extends DataSystem(Map()) with Pushdownable with DataSystemRegister {

	override def isSupportAll: Boolean = true

	override def fastEquals(other: DataSystem): Boolean = {
		other match {
			case spark:SparkDataSystem => true
			case _ => false
		}
	}

	override val supportedOperators: Seq[Class[_]] = Seq()
	override val supportedUDF: Seq[String] = Seq()
	override val supportedExpressions: Seq[Class[_]] = Seq()
	override val beGoodAtOperators: Seq[Class[_]] = Seq()
	override val supportedJoinTypes: Seq[JoinType] = Seq()

	override def buildScan(plan: LogicalPlan, sparkSession: SparkSession): DataFrame = {
		Dataset.ofRows(sparkSession, plan)
	}

	override def tableNames(): Seq[String] = { throw new UnsupportedOperationException("unsupport method tableNames") }

	override def tableProperties(tableName: String): Map[String, String] = { throw new UnsupportedOperationException("unsupport method tableOption") }

	override def tableName(): String = {  throw new UnsupportedOperationException("unsupport method tableName") }

	override def buildQuery(plan: LogicalPlan): DataTable = {
		throw new UnsupportedOperationException(s"unsupport call method buildQuery in spark datasystem")
	}

	override def shortName(): String = "spark"

	override def dataSource(): String = ""
}
