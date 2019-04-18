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

package moonbox.application.batch.spark

import moonbox.common.{MbConf, MbLogging}
import moonbox.core._
import moonbox.core.command._
import moonbox.core.datasys.DataSystem
import org.apache.spark.sql.SaveMode


object Main extends MbLogging {

	def main(args: Array[String]) {
		val conf = new MbConf()
		val keyValues = for (i <- 0 until(args.length, 2)) yield (args(i), args(i+1))
		var username: String = null
		var sqls: Seq[String] = null
		keyValues.foreach {
			case (k ,v) if k.equals("username") =>
				username = v
			case (k, v) if k.equals("sqls") =>
				sqls = v.split(";")
			case (k, v) =>
				conf.set(k, v)
		}
		new Main(conf, username, sqls).runMain()
	}
}

class Main(conf: MbConf, username: String, sqls: Seq[String]) {

	private val mbSession: MbSession = new MbSession(conf).bindUser(username, autoLoadDatabases = false)

	def runMain(): Unit = {
		sqls.foreach { sql =>
			mbSession.parsedCommand(sql) match {
				case runnable: MbRunnableCommand =>
					runnable.run(mbSession)(mbSession.userContext)

				case createTempView: CreateTempView =>
					val optimized = mbSession.optimizedPlan(createTempView.query)
					val df = mbSession.toDF(optimized)
					if (createTempView.isCache) {
						df.cache()
					}
					if (createTempView.replaceIfExists) {
						df.createOrReplaceTempView(createTempView.name)
					} else {
						df.createTempView(createTempView.name)
					}

				case insert @ InsertInto(MbTableIdentifier(table, database), query, colNames, overwrite) =>
					val sinkCatalogTable = mbSession.getCatalogTable(table, database)
					val options = sinkCatalogTable.properties
					val format = DataSystem.lookupDataSource(options("type"))
					val saveMode = if (overwrite) SaveMode.Overwrite else SaveMode.Append
					val optimized = mbSession.optimizedPlan(query)
					val dataFrame = mbSession.toDF(optimized)
					val dataFrameWriter = TableInsertPrivilegeChecker
						.intercept(mbSession, sinkCatalogTable, dataFrame)
						.write
						.format(format)
						.options(options)
						.partitionBy(colNames:_*)
						.mode(saveMode)
					if (options.contains("partitionColumnNames")) {
						dataFrameWriter.partitionBy(options("partitionColumnNames").split(","): _*)
					}
					dataFrameWriter.save()

				case _ =>
					throw new Exception("Unsupport command in batch mode")
			}
		}
	}

}
