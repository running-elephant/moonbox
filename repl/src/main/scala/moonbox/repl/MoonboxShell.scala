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

package moonbox.repl

import java.util.Locale

import moonbox.client.exception.BackendException
import moonbox.client.{ClientOptions, MoonboxClient}
import org.jline.reader.impl.LineReaderImpl
import org.jline.reader.impl.completer.StringsCompleter
import org.jline.reader.impl.history.DefaultHistory
import org.jline.reader.{LineReader, LineReaderBuilder, UserInterruptException}
import org.jline.terminal.Terminal.{Signal, SignalHandler}
import org.jline.terminal.{Terminal, TerminalBuilder}

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.AnsiColor
import scala.language.implicitConversions

object MoonboxShell {

	private var timeout: Int = 60 * 60
	// unit: second
	private var islocal: Boolean = _
	private var host: String = sys.env.getOrElse("MOONBOX_MASTER_HOST", "localhost")
	private var port: Int = Utils.getJdbcDefaultPort()
	private var user: String = _
	private var password: String = _
	private var fetchSize: Int = 1000
	private var truncate: Int = 0
	private var maxRowsToShow: Int = 100
	private var extraOptions: String = ""
	private var client: MoonboxClient = _
	private var clientInited: Boolean = _

	private val DEFAULT_RETRY_TIMES: Int = 3
	private val DELIMITER: String = ";"
	private val PARAMETER_PREFIX: String = "%SET "
	/* max number of history to maintain */
	private val HISTORY_SIZE: Int = 100
	private val historyMQLs: mutable.Queue[String] = new mutable.Queue[String]()
	private val lineHistory: mutable.Queue[String] = new mutable.Queue[String]()
	private val mainThread = Thread.currentThread()
	private val handler = new SignalHandler() {
		//create Ctrl+C handler, NOTE: sun misc handler dose not work
		override def handle(signal: Signal): Unit = {
			if (signal.equals(Signal.INT)) {
				new Thread() {
					override def run() = {
						mainThread.interrupt()
					}
				}.start()
			}
		}
	}

	private val terminal: Terminal = TerminalBuilder.builder.signalHandler(handler).build()
	private val autoCompleter = new StringsCompleter(MQLs.MQL.map(_.toLowerCase()): _*)
	private val lineReader: LineReader = LineReaderBuilder.builder().terminal(terminal).completer(autoCompleter).parser(null).build()
	//  System.setProperty("log4j.configuration", "") //close log4j print in doReadLine

	def main(args: Array[String]) {
		try {
			parse(args.toList)
			initClient()
			if (clientInited) {
				welcome()
				doReadLine()
			}
		} catch {
			case e: Exception => Console.err.println(e.getMessage)
		} finally {
			System.exit(-1)
		}
	}

	private def prompter: String = s"$user> "

	private def welcome(): Unit = {
		import scala.util.Properties.{versionString, javaVmName, javaVersion}
		val welcomeMsg1 =
			"""Welcome to
      _  _                 _        __  *
     / \/ | ___  ___  _ _ | |_  ___ \ \*
    /  _  |/ _ \/ _ \| \ || _ \/ _ \/ *\
   /__/ \_|\___/\___/|_|_||___/\___/_/\_\    version %s""".format(client.version)

		val welcomeMsg2 = """Using Scala %s (%s, Java %s)""".format(versionString, javaVmName, javaVersion)
		val welcomeMsg3 = "\nType 'commands' | 'cmds' to show commands in common use.\nType 'help' for more MQLs.\n"
		print(AnsiColor.GREEN)
		println(welcomeMsg1)
		println()
		println(welcomeMsg2)
		print(AnsiColor.RESET)
		println(welcomeMsg3)
	}

	private def doReadLine(): Unit = {
		while (true) {
			try {
				val stringBuilder = new StringBuilder
				val sqlList: ArrayBuffer[String] = new ArrayBuffer[String]()
				var line: String = lineReader.readLine(prompter).trim
				var braceCount: Int = 0
				line.length match {
					case len if len > 0 =>
						var endLine = false
						while (!endLine) {
							line.toCharArray.foreach {
								case ';' if braceCount == 0 =>
									if (stringBuilder.nonEmpty) {
										sqlList += stringBuilder.toString()
										stringBuilder.clear()
									}
								case other =>
									stringBuilder.append(other)
									other match {
										case '(' => braceCount += 1
										case ')' => braceCount -= 1
										case _ => /* no-op */
									}
							}
							if (stringBuilder.isEmpty) {
								endLine = true
							} else {
								if (line.length > 0) {
									if (braceCount != 0) {
										stringBuilder.append("\n")
										line = lineReader.readLine(" " * (user.length - 1) + "-> ").replaceAll("\\s+$", "") // trim end
									} else {
										stringBuilder.append(" ")
										line = lineReader.readLine(" " * (user.length - 1) + "-> ").trim // trim start and end
									}
								} else {
									line = lineReader.readLine(" " * (user.length - 1) + "-> ").trim // trim start and end
								}
							}
						}
					case _ => /* no-op */
				}
				val cleanedSqls = sqlList.map(_.trim).filterNot(_ == "")
				if (cleanedSqls.nonEmpty) {
					/* add line reader history */
					enqueueWithLimit(lineHistory, cleanedSqls.mkString("", "; ", ";"))
					setHistory(lineReader.asInstanceOf[LineReaderImpl])
					process(cleanedSqls)
				}
			} catch {
				case _: UserInterruptException => /* no-op */
				case _: InterruptedException => doCancelInteractiveQuery()
				case e: Exception => Console.err.println(s"MQL process error: ${e.getMessage}")
			}
		}
	}

	private def doCancelInteractiveQuery(): Unit = {
		if (client != null && client.isActive) {
			try {
				client.cancelInteractiveQuery()
			} catch {
				case e: Exception =>
			}
			println("Query canceled.")
		}
	}

	private def initClient(): Unit = {
		try {
			checkParameters()
			val clientOptions =
				ClientOptions.builder()
					.options(handleExtraOptions(extraOptions))
					.user(user)
					.password(password)
					.host(host)
					.port(port)
					.isLocal(islocal)
					.fetchSize(fetchSize)
					.timeout(timeout)
					.maxRows(maxRowsToShow)
					.database("default")
					.build()
			client = MoonboxClient.builder(clientOptions).build()
			clientInited = true
		} catch {
			case e: Exception =>
				if (client != null) {
					client.close()
				}
				throw e
		}
	}

	private def handleExtraOptions(ops: String): Map[String, String] = {
		ops match {
			case "" => Map.empty
			case s =>
				s.split("&").map { kv =>
					val pair = kv.split("=")
					pair.length match {
						case 2 => pair(0).trim -> pair(1).trim
						case _ => throw new Exception("Invalid options.")
					}
				}.toMap
		}
	}

	private def closeCurrentClient(): Unit = {
		try {
			if (client != null) {
				client.close()
			}
		} finally {
			clientInited = false
		}
	}

	private def reConnect(): Unit = {
		try {
			closeCurrentClient()
			initClient()
		} catch {
			case e: Exception => Console.err.println(e.getMessage)
		}
		if (clientInited) {
			doReadLine()
		}
	}

	private def shutdown(): Unit = {
		try {
			closeCurrentClient()
			Console.out.println("Bye!")
			System.exit(0)
		} catch {
			case e: Exception =>
				Console.err.println(e.getMessage)
				System.exit(-1)
		}
	}

	private def showConnectionState(): Unit = {
		val rowsToAdd = Seq(
			"established" :: clientInited :: Nil,
			"max_rows_show" :: maxRowsToShow :: Nil,
			"truncate_length" :: truncate :: Nil,
			"fetch_size" :: fetchSize :: Nil,
			"history_size" :: HISTORY_SIZE :: Nil,
			"connection_retry_times" :: DEFAULT_RETRY_TIMES :: Nil,
			"delimiter" :: DELIMITER :: Nil
		)
		val rowsToShow = client.getConnectionState.toSeq ++ rowsToAdd
		val schema = "state_name" :: "value" :: Nil
		print(Utils.stringToShow(rowsToShow, schema, maxRowsToShow, truncate))
	}

	private def setHistory(lineReader: LineReaderImpl) = {
		lineReader.getHistory.purge()
		val h = new DefaultHistory()
		lineHistory.foreach(h.add)
		lineReader.setHistory(h)
	}

	private def handleSqlQuery(sqlList: Seq[String]): Unit = {
		try {
			val result = client.interactiveQuery(sqlList, fetchSize, maxRowsToShow, timeout * 1000)
			/* show result */
			val dataToShow = result.toSeq.map(_.toSeq)
			val schema = result.parsedSchema.map(_._1)
			print(Utils.stringToShow(dataToShow, schema, maxRowsToShow))
		} catch {
			case e: BackendException =>
				Console.err.println(s"SQL ERROR: ${e.message}")
		}
	}

	private def printSetHelp(): Unit = {
		val message =
			"""
			  |Use: %SET TRUNCATE=[Int]  /*Set the column length to truncate, 0 denotes unabridged*/
			  |     %SET MAX_ROWS=[Int]  /*Set max rows to show in console*/
			  |     %SET TIMEOUT=[Int]  /*Set connection timeout(second) to moonbox server*/
			  |     %SET FETCH_SIZE=[Long]  /*Set size for per data fetch*/
			""".stripMargin
		Console.err.println(message)
	}

	private def processSetSqls(setStatement: String, sqlList: Seq[String]): Unit = {
		val kv = setStatement.toUpperCase(Locale.ROOT).stripPrefix(PARAMETER_PREFIX).trim
		val idx = kv.indexOf("=")
		if (idx != -1) {
			try {
				val k = kv.substring(0, idx).trim
				val v = kv.substring(idx + 1).trim
				k match {
					case "MAX_ROWS" => maxRowsToShow = v.toInt
					case "TRUNCATE" => truncate = v.toInt
					case "TIMEOUT" => timeout = v.toInt
					case "FETCH_SIZE" => fetchSize = v.toInt
					case _ =>
						Console.err.println(s"""Unknown parameter key in set statement "$setStatement".""")
						printSetHelp()
				}
			} catch {
				case _: NumberFormatException =>
					Console.err.println("Value type invalid.")
					printSetHelp()
				case _: IndexOutOfBoundsException =>
					Console.err.println("Invalid parameter set statement.")
					printSetHelp()
				case other: Exception => throw other
			}
			val s = sqlList.tail
			if (s.nonEmpty) {
				handleSqlQuery(s)
			}
		} else {
			handleSqlQuery(sqlList)
		}
	}

	private def enqueueWithLimit(queue: mutable.Queue[String], sqls: String*): Unit = {
		queue.enqueue(sqls: _*)
		/* The size of history SQLs limited */
		while (queue.length > HISTORY_SIZE) {
			queue.dequeue()
		}
	}

	private def showCommands(): Unit = {
		val schema = Seq("command", "description")
		val data = Seq(
			"history | H" :: "show history MQLs" :: Nil,
			"reconnect | R" :: "reconnect to moonbox server" :: Nil,
			"exit | quit | Q" :: "repl shutdown" :: Nil,
			"state | status" :: "show connection status" :: Nil,
			"commands | cmds | cmd" :: "show commands in common use" :: Nil,
			"help" :: "show all MQLs" :: Nil,
			"%SET TRUNCATE=[Int]" :: "Set the column length to truncate, 0 denotes unabridged" :: Nil,
			"%SET MAX_ROWS=[Int]" :: "Set max rows to show in console" :: Nil,
			"%SET TIMEOUT=[Int]" :: "Set connection timeout(second) to moonbox server" :: Nil,
			"%SET FETCH_SIZE=[Long]" :: "Set size for per data fetch" :: Nil
		)
		print(Utils.stringToShow(data, schema))
	}

	private def showHelp(): Unit = {
		val schema = Seq("MQLs")
		val data = MQLs.MQL.map(_ :: Nil)
		print(Utils.stringToShow(data, schema))
	}

	private def process(sqlList: Seq[String]): Unit = {
		val compositedSql = sqlList.mkString(";")
		val headSql = sqlList.head
		headSql.toUpperCase(Locale.ROOT) match {
			case "" =>
			case stmt if stmt.startsWith(PARAMETER_PREFIX) =>
				enqueueWithLimit(historyMQLs, compositedSql)
				processSetSqls(headSql, sqlList)
			case "HISTORY" | "H" =>
				val data = historyMQLs.zipWithIndex.map(u => Seq(historyMQLs.length - u._2, u._1 + ";"))
				print(Utils.stringToShow(data, Seq("ID", "HISTORY MQLs"), HISTORY_SIZE))
			case "RECONNECT" | "R" => reConnect()
			case "EXIT" | "QUIT" | "Q" => shutdown()
			case "STATE" | "STATUS" => showConnectionState()
			case "COMMANDS" | "CMDS" => showCommands()
			case "HELP" => showHelp()
			case "CLS" | "CLEAR" =>
				System.out.print("\033[H\033[2J")
				System.out.flush()
			case _ =>
				enqueueWithLimit(historyMQLs, compositedSql)
				handleSqlQuery(sqlList)
		}
	}

	private def checkParameters(): Unit = {
		if (user == null) {
			user = lineReader.readLine("username:")
		}
		if (password == null) {
			password = lineReader.readLine("Enter password:", new Character(0))
		}
	}

	@tailrec
	private def parse(args: List[String]): Unit = args match {
		case ("-u" | "--user") :: value :: tail =>
			user = value
			parse(tail)
		case ("-h" | "--host") :: value :: tail =>
			host = value
			parse(tail)
		case ("-P" | "--port") :: IntParam(value) :: tail =>
			port = value
			parse(tail)
		case ("-p" | "--password") :: value :: tail =>
			password = value
			parse(tail)
		case ("-t" | "--timeout") :: IntParam(value) :: tail =>
			timeout = value
			parse(tail)
		case ("-r" | "--runtime") :: value :: tail =>
			islocal = value.equalsIgnoreCase("local")
			parse(tail)
		case ("-f" | "--fetchsize") :: IntParam(value) :: tail =>
			fetchSize = value
			parse(tail)
		case ("-e" | "--extraoptions") :: value :: tail =>
			extraOptions = value
			parse(tail)
		case ("--help") :: tail =>
			printUsageAndExit(0)
		case Nil =>
		case _ =>
			printUsageAndExit(1)
	}

	private def printUsageAndExit(exitCode: Int): Unit = {
		// scalastyle: off println
		System.err.println(
			"Usage: moonbox-shell [options]\n" +
				"options:\n" +
				"   -h, --host            Connect to host.\n" +
				"   -P, --port            Port num to ues for connecting to server.\n" +
				"   -u, --user            User for login, org@user.\n" +
				"   -p, --password        Password to use when connecting to server.\n" +
				"   -r, --runtime         Run in local or in cluster.\n" +
				"   -t, --timeout         The query timeout: seconds.\n" +
				"   -f, --fetchsize       The fetch size.\n" +
				"   -e, --extraoptions    The extra options: like \"k1=v1&k2=v2...\"\n" +
				"   --help"
		)
		System.exit(exitCode)
	}
}
