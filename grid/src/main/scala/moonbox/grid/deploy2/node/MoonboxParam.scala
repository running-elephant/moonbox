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

package moonbox.grid.deploy2.node

import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.util.IntParam

import scala.annotation.tailrec

class MoonboxParam(args: Array[String], val conf: MbConf) extends MbLogging {
	val clusterName: String = conf.get(CLUSTER_NAME)
	var nodes: Seq[String] = _
	var host: String =_
	var port: Int = _
	var restPort: Int = _
	var tcpPort: Int = _

	parse(args.toList)

	if (nodes == null || nodes.isEmpty) {
		printUsageAndExit(1)
	} else {
		nodes.zipWithIndex.foreach {
			case (node, index) => conf.set(s"moonbox.rpc.akka.cluster.seed-nodes.$index", node)
		}
	}
	if (host == null || host == "") {
		host = "localhost"
	}
	conf.set("moonbox.rpc.akka.remote.netty.tcp.hostname", host)
	if (port == 0) {
		port = 2551
	}
	conf.set("moonbox.rpc.akka.remote.netty.tcp.port", port.toString)

	if (restPort == 0) {
		if (conf.get(REST_SERVER_PORT.key).isDefined) {
			restPort = conf.get(REST_SERVER_PORT.key).get.toInt
		}
	}

	if (tcpPort == 0) {
		if (conf.get(TCP_SERVER_PORT.key).isDefined) {
			tcpPort = conf.get(TCP_SERVER_PORT.key).get.toInt
		}
	}

	conf.set(REST_SERVER_PORT.key, restPort.toString)

	private def parseNodes(arg: String): Seq[String] = {
		arg.split(",").map { address =>
			val nodeWithPort = if (address.indexOf(":") > 0)
				address else s"$address:2551"
			s"akka.tcp://$clusterName@$nodeWithPort"
		}
	}

	@tailrec
	private def parse(args: List[String]): Unit = args match {
		case ("-n" | "--nodes") :: value :: tail =>
			nodes = parseNodes(value)
			parse(tail)
		case ("-h" | "--host") :: value :: tail =>
			host = value
			parse(tail)
		case ("-p" | "--port") :: IntParam(value) :: tail =>
			port = value
			parse(tail)
		case ("-r" | "--restPort") :: IntParam(value) :: tail =>
			restPort = value
			parse(tail)
		case ("-t" | "--tcpPort"):: IntParam(value) :: tail =>
			tcpPort = value
			parse(tail)
		case ("--help") :: tail =>
			printUsageAndExit(0)
		case Nil =>
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
			"Usage: MbMaster [options]\n" +
				"options:\n" +
				"   -n, --nodes : the list of nodes\n" +
				"   -h, --host : the host using for rpc\n" +
				"   -p, --port : the port using for rpc\n" +
				"   -r, --restPort : the port rest server listened on\n" +
				"   --help"
		)
		System.exit(exitCode)
	}
}
