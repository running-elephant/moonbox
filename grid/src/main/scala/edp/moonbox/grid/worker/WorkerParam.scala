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

package edp.moonbox.grid.worker

import edp.moonbox.common.{EdpLogging, IntParam}
import edp.moonbox.core.MbConf
import edp.moonbox.grid.master.MbMaster
import java.util.{Map => JMap}
import scala.collection.JavaConverters._

import scala.annotation.tailrec


class WorkerParam(args: Array[String], val conf: MbConf) extends EdpLogging {
	var masters = Seq("grid://localhost:2551")
	var host = "localhost"
	var port = 2551

	conf.set("akka.cluster.roles.0", MbWorker.ROLE)
	parse(args.toList)

	def akka = conf.akka

	private def parse(arg: String): Seq[String] = {
		arg.stripPrefix("grid://").split(",").map(master =>
			s"akka.tcp://${MbMaster.CLUSTER_NAME}@$master")
	}

	@tailrec
	private def parse(args: List[String]): Unit = args match {
		case ("--host" | "-h") :: value :: tail =>
			host = value
			conf.set("akka.remote.netty.tcp.hostname", value)
			parse(tail)
		case ("--port" | "-p") :: IntParam(value) :: tail =>
			port = value
			conf.set("akka.remote.netty.tcp.port", value.toString)
			parse(tail)
		case ("--masters" | "-m") :: value :: tail =>
			masters = parse(value)
			masters.zipWithIndex.foreach {
				case (master, index) => conf.set(s"akka.cluster.seed-nodes.$index", master)
			}
			parse(tail)
		case ("--help") :: tail =>
			printUsageAndExit(0)
		case Nil => // No-op
		case _ =>
			printUsageAndExit(1)
	}

	/**
	  * Print usage and exit JVM with the given exit code.
	  */
	private def printUsageAndExit(exitCode: Int) {
		// scalastyle:off println
		System.err.println(
			"Usage: MbMaster [options]\n" +
				"\n" +
				"Options:\n" +
				"  -h host, --host host   hostname others connect to\n" +
				"  -p port, --port port   port to listen on (default: 2551)\n" +
				"  -m masters, --masters masters master url connected to (example: grid://host:port,host:port)\n")
		// scalastyle:on println
		System.exit(exitCode)
	}


}
