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

package moonbox.application.batch.hive

import java.io.PrintStream
import java.util

import moonbox.common.{MbConf, MbLogging}
import org.apache.hadoop.hive.common.io.CachingPrintStream
import org.apache.hadoop.hive.conf.HiveConf
import org.apache.hadoop.hive.ql.Driver
import org.apache.hadoop.hive.ql.processors.CommandProcessorResponse
import org.apache.hadoop.hive.ql.session.SessionState

object Main extends MbLogging {
	def main(args: Array[String]) {
		val conf = new MbConf()
		val keyValues = for (i <- 0 until(args.length, 2)) yield (args(i), args(i+1))
		var driverId: String = null
		var org: String = null
		var username: String = null
		var sqls: Seq[String] = null
		keyValues.foreach {
			case (k ,v) if k.equals("org") =>
				org = v
			case (k ,v) if k.equals("username") =>
				username = v
			case (k, v) if k.equals("sqls") =>
				sqls = v.split(";")
			case (k, v) if k.equals("driverId") =>
				driverId = v
			case (k, v) =>
				conf.set(k, v)
		}
		new Main(conf, driverId, org, username, sqls).runMain()
	}
}

class Main(conf: MbConf, driverId: String, org: String, username: String, sqls: Seq[String]) {
	import scala.collection.JavaConversions._
	def runMain(): Unit = {

		val hiveConf = new HiveConf(classOf[SessionState])

		hiveConf.set("fs.defaultFS", "hdfs://master:8020")
		hiveConf.set("dfs.nameservices", "moonbox")
		hiveConf.set("dfs.ha.namenodes.moonbox", "nn1,nn2")
		hiveConf.set("dfs.namenode.rpc-address.moonbox.nn1", "master:8020")
		hiveConf.set("dfs.namenode.rpc-address.moonbox.nn2", "slave1:8020")
		hiveConf.set("dfs.permissions.enabled", "false")

		hiveConf.set("dfs.client.failover.proxy.provider.moonbox", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider")

		hiveConf.set("javax.jdo.option.ConnectionURL", "jdbc:mysql://10.143.131.38:3306/hive_test3")
		hiveConf.set("javax.jdo.option.ConnectionDriverName", "com.mysql.jdbc.Driver")
		hiveConf.set("javax.jdo.option.ConnectionUserName", "root")
		hiveConf.set("javax.jdo.option.ConnectionPassword", "123456")
		hiveConf.set("mapred.job.name", driverId)
		hiveConf.set("hive.stats.collect.tablekeys", "true")
		hiveConf.set("hive.stats.collect.scancols", "true")
		// hiveConf.set("hive.exec.mode.local.auto", "false")
		hiveConf.set("hive.exec.submit.local.task.via.child", "false")

		hiveConf.set(HiveConf.ConfVars.PREEXECHOOKS.varname, "moonbox.application.hivenative.ExecutionHook")

		val ss: SessionState = new SessionState(hiveConf, s"$org@$username")
		ss.in = System.in

		val out = new MbOutputStream()

		ss.out = new PrintStream(out, true, "UTF-8")
		ss.info = new PrintStream(out, true, "UTF-8")
		ss.err = new CachingPrintStream(System.err, true, "UTF-8")
		ss.initTxnMgr(hiveConf)
		SessionState.start(ss)
		SessionState.setCurrentSessionState(ss)

		val driver: Driver = new Driver()

		for (sql <- sqls) {
			val run: CommandProcessorResponse = driver.run(sql)
			if (run.getResponseCode == 0) {
				val schema = driver.getSchema
				if (schema.getFieldSchemas != null) {
					println(schema.getFieldSchemas.map(fs => fs.getName + "  " + fs.getType).mkString("\n"))
				}
				val result = new util.ArrayList[Object]()
				driver.getResults(result)
				println(result.mkString("\n"))
			}
		}


		/*val ss: CliSessionState = new CliSessionState(hiveConf)
		ss.in = System.in

		val out = new MbOutputStream()

		ss.out = new PrintStream(out, true, "UTF-8");
		ss.info = new PrintStream(out, true, "UTF-8");
		ss.err = new CachingPrintStream(System.err, true, "UTF-8")

		SessionState.start(ss)

		/*val oproc: OptionsProcessor = new OptionsProcessor()

		val config = oproc.getHiveVariables
		config.put("hive.query.name", driverId)*/

		val cli: CliDriver = new CliDriver
		//cli.setHiveVariables(config)
		// use the specified database if specified
		cli.processSelectDatabase(ss)
		// Execute -i init files (always in silent mode)
		cli.processInitFiles(ss)
		cli.processLine(sqls.mkString(";"))*/
	}
}
