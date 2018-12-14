package moonbox.grid.deploy.worker

import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.util.IntParam

import scala.annotation.tailrec

class MoonboxWorkerParam(args: Array[String], val conf: MbConf) extends MbLogging {
	var host = Utils.localHostName()
	var port = 0
	var masters: Array[String] = null

	if (System.getenv("MOONBOX_WORKER_PORT") != null) {
		port = System.getenv("MOONBOX_WORKER_PORT").toInt
	}

	parse(args.toList)

	conf.set("moonbox.rpc.akka.remote.netty.tcp.hostname", host)
	conf.set("moonbox.rpc.akka.remote.netty.tcp.port", port.toString)
	@tailrec
	private def parse(args: List[String]): Unit = args match {
		case ("-h" | "--host") :: value :: tail =>
			Utils.checkHost(value)
			host = value
			parse(tail)
		case ("-p" | "--port") :: IntParam(value) :: tail =>
			port = value
			parse(tail)
		case ("--help") :: tail =>
			printUsageAndExit(0)
		case value :: tail =>
			if (masters != null) {
				printUsageAndExit(1)
			}
			masters = value.stripPrefix("moonbox://").split(",").map("moonbox://"  + _)
			parse(tail)
		case Nil =>
			if (masters == null) {
				printUsageAndExit(1)
			}
		case _ =>
			printUsageAndExit(1)
	}

	def akkaConfig: Map[String, String] = {
		for { (key, value) <- AKKA_DEFAULT_CONFIG ++ AKKA_HTTP_DEFAULT_CONFIG ++ conf.getAll
			  if key.startsWith("moonbox.rpc.akka") || key.startsWith("moonbox.rest.akka")
		} yield {
			if (key.startsWith("moonbox.rpc.akka"))
				(key.stripPrefix("moonbox.rpc."), value)
			else
				(key.stripPrefix("moonbox.rest."), value)
		}
	}

	private def printUsageAndExit(exitCode: Int): Unit = {
		// scalastyle: off println
		System.err.println(
			"Usage: MbMaster [options] <master>\n" +
				"options:\n" +
				"   -h, --host : the host using for rpc\n" +
				"   -p, --port : the port using for rpc\n" +
				"   --help"
		)
		System.exit(exitCode)
	}
}
