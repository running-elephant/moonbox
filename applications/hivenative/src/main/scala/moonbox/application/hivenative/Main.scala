package moonbox.application.hivenative

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
		var username: String = null
		var sqls: Seq[String] = null
		keyValues.foreach {
			case (k ,v) if k.equals("username") =>
				username = v
			case (k, v) if k.equals("sqls") =>
				sqls = v.split(";")
			case (k, v) if k.equals("driverId") =>
				driverId = v
			case (k, v) =>
				conf.set(k, v)
		}
		new Main(conf, driverId, username, sqls).runMain()
	}
}

class Main(conf: MbConf, driverId: String, username: String, sqls: Seq[String]) {
	import scala.collection.JavaConversions._
	def runMain(): Unit = {

		val hiveConf = new HiveConf(classOf[SessionState])

		hiveConf.set("mapred.job.name", driverId)
		hiveConf.set("hive.stats.collect.tablekeys", "true")
		hiveConf.set("hive.stats.collect.scancols", "true")
		// hiveConf.set("hive.exec.mode.local.auto", "false")
		hiveConf.set("hive.exec.submit.local.task.via.child", "false")

		val ss: SessionState = new SessionState(hiveConf, username)
		ss.in = System.in

		val out = new MbOutputStream()

		ss.out = new PrintStream(out, true, "UTF-8");
		ss.info = new PrintStream(out, true, "UTF-8");
		ss.err = new CachingPrintStream(System.err, true, "UTF-8")

		SessionState.start(ss)

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
