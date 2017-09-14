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

package edp.moonbox.grid.master

import edp.moonbox.common.{EdpLogging, IntParam, Util}
import edp.moonbox.core.MbConf

import scala.annotation.tailrec
import scala.collection.JavaConverters._

class MasterParam(args: Array[String], val conf: MbConf) extends EdpLogging {
	var masters = Seq("grid://localhost:2551")
	var host = "localhost"
	var port = 2551
	var restPort = 9099

	conf.set("akka.cluster.roles.0", MbMaster.ROLE)
	parse(args.toList)
	logInfo(s"master listen on $host:$port")

	def akka = conf.akka

	def cacheHostPort: (String, Int) = {
		val hostPort = conf.cacheServers.split(",").map(_.split(":")).head
		hostPort(0) -> hostPort(1).toInt
	}

	private def parse(arg: String): Seq[String] = {
		arg.stripPrefix("grid://").split(",").map(master =>
			s"akka.tcp://${MbMaster.CLUSTER_NAME}@$master")
	}

	@tailrec
	private def parse(args: List[String]): Unit = args match {
		case ("--masters" | "-m") :: value :: tail =>
			masters = parse(value)
			masters.zipWithIndex.foreach {
				case (master, index) => conf.set(s"akka.cluster.seed-nodes.$index", master)
			}
			parse(tail)
		case ("--host" | "-h") :: value :: tail =>
			host = value
			conf.set("akka.remote.netty.tcp.hostname", value)
			parse(tail)
		case ("--port" | "-p") :: IntParam(value) :: tail =>
			port = value
			conf.set("akka.remote.netty.tcp.port", value.toString)
			parse(tail)
		case "--restPort" :: IntParam(value) :: tail =>
			restPort = value
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
				"  -h host, --host host   hostname worker connect to\n" +
				"  -p port, --port port   port to listen on (default: 2551)\n" +
				"  --restPort port      port for rest request (default: 9099)\n")
		// scalastyle:on println
		System.exit(exitCode)
	}


}
