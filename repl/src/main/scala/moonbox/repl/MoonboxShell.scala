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

import java.sql.{Connection, DriverManager, ResultSet, ResultSetMetaData}
import java.util.Properties

import moonbox.jdbc.MbDriver
import org.jline.reader.impl.completer.StringsCompleter
import org.jline.reader.{LineReader, LineReaderBuilder, UserInterruptException}
import org.jline.terminal.Terminal.{Signal, SignalHandler}
import org.jline.terminal.TerminalBuilder

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.AnsiColor
import scala.language.implicitConversions


object MoonboxShell {

	private val DELIMITER: Char = ';'
	private val LEFT_BRACE: Char = '('
	private val RIGHT_BRACE: Char = ')'

	private var timeout: Int = 60 * 60 // unit: second
	private var host: String = sys.env.getOrElse("MOONBOX_MASTER_HOST", "localhost")
	private var port: Int = Utils.getJdbcDefaultPort()

	private var user: String = _
	private var password: String = _
	private var appType: String = "sparkcluster"
	private var appName: Option[String] = None
	private var database: String = "default"
	private var fetchSize: Int = 1000
	private var truncate: Int = 0
	private var maxRows: Int = 100
	private var extraOptions: String = ""
	private val mainThread = Thread.currentThread()

	private val lineReader = createLineReader()

	private var connection: Connection = _

	private def prompter: String = s"$user> "

	private def printWelcome(): Unit = {
		import scala.util.Properties.{javaVersion, javaVmName, versionString}
		val welcomeMsg1 =
			"""Welcome to
      _  _                 _        __  *
     / \/ | ___  ___  _ _ | |_  ___ \ \*
    /  _  |/ _ \/ _ \| \ || _ \/ _ \/ *\
   /__/ \_|\___/\___/|_|_||___/\___/_/\_\    version %s""".format(MbDriver.VERSION)

		val welcomeMsg2 = """Using Scala %s (%s, Java %s)""".format(versionString, javaVmName, javaVersion)
		val welcomeMsg3 = "\nType 'commands' | 'cmds' to show commands in common use.\nType 'help' for more MQLs.\n"
		print(AnsiColor.GREEN)
		println(welcomeMsg1)
		println()
		println(welcomeMsg2)
		print(AnsiColor.RESET)
		println(welcomeMsg3)
	}

	private def createSignalHandler(): SignalHandler = {
		new SignalHandler() {
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
	}

	private def createLineReader(): LineReader = {
		val terminal = TerminalBuilder.builder.signalHandler(createSignalHandler()).build()
		val autoCompleter = new StringsCompleter(MQLs.MQL.map(_.toLowerCase()): _*)
		LineReaderBuilder.builder()
				.terminal(terminal)
				.completer(autoCompleter)
				.parser(null)
				.build()
	}

	def main(args: Array[String]) {
		try {
			parseArgs(args.toList)
			checkArgs()
			createConnection()
			printWelcome()
			loop()
		} catch {
			case e: Exception =>
				Console.err.println(e.getMessage)
		} finally {
			System.exit(-1)
		}
	}

	private def loop(): Unit = {
		while (true) {
			try {
				val sqlString = readLine().mkString(DELIMITER.toString)
				if (sqlString.trim.nonEmpty) {
					process(sqlString)
				}
			} catch {
				case e: NullPointerException =>
				case e: InterruptedException =>
				case e: UserInterruptException =>
			}
		}
	}

	private def process(sql: String): Unit = {
		if (ClearScreen.accept(sql)) {
			clearScreen()
		} else if (Exit.accept(sql)) {
			shutdown()
		} else if (Reconnect.accept(sql)) {
			reConnect()
		} else if (ShowHistory.accept(sql)) {
			showHistory()
		} else if (ShowHelp.accept(sql)) {
			showHelp()
		} else if (ShowState.accept(sql)) {
			showConnectionState()
		} else if (ShowCommands.accept(sql)) {
			showCommands()
		} else if (SetFetchSize.accept(sql)) {
			setFetchSize(sql)
		} else if (SetMaxRows.accept(sql)) {
			setMaxRows(sql)
		} else if (SetTimeout.accept(sql)) {
			setTimeout(sql)
		} else if (SetTruncate.accept(sql)) {
			setTruncate(sql)
		} else {
			remoteExecute(sql)
		}
	}

	private def setFetchSize(sql: String): Unit = {
		setParameter(sql, SetFetchSize) {
			fetchSize: Int => {
				this.fetchSize = fetchSize
			}
		}
	}

	private def setMaxRows(sql: String): Unit = {
		setParameter(sql, SetMaxRows) {
			maxRows: Int => {
				this.maxRows = maxRows
			}
		}
	}

	private def setTimeout(sql: String): Unit = {
		setParameter(sql, SetTimeout) {
			timeout: Int => {
				this.timeout = timeout
			}
		}
	}

	private def setTruncate(sql: String): Unit = {
		setParameter(sql, SetTruncate) {
			truncate: Int => {
				this.truncate = truncate
			}
		}
	}

	private def setParameter(sql: String, set: FrontCommand)(f: Int => Unit): Unit = {
		val index = sql.indexOf('=')
		if (index != -1) {
			try {
				val param = sql.substring(index + 1).trim.toInt
				f(param)
			} catch {
				case e: NumberFormatException =>
					val helpMsg = "Set error:" + set.command.mkString(" | ")
					Console.err.println(helpMsg)
			}
		}
	}

	private def showHistory(): Unit = {
		lineReader.getHistory.iterator()
	}

	private def remoteExecute(sql: String): Unit = {
		val statement = connection.createStatement()
		statement.setFetchSize(fetchSize)
		statement.setQueryTimeout(timeout)
		try {
			val rs = statement.executeQuery(sql)
			val rsMeta = rs.getMetaData
			val columnNums = rsMeta.getColumnCount
			val schema = new Array[String](columnNums)
			val data = new ArrayBuffer[Seq[Any]]()
			for (i <- 0 until columnNums) {
				schema(i) = rsMeta.getColumnName(i + 1)
			}
			while (rs.next()) {
				data.append((1 to columnNums).map(i => rs.getObject(i)))
			}
			print(Utils.stringToShow(data, schema, maxRows))
		}	catch {
			case u: UserInterruptException =>
			case i: InterruptedException =>
			case e: Exception =>
				if (e.getMessage != null && e.getMessage.contains("java.lang.InterruptedException")) {
					try {
						statement.cancel()
					} catch {
						case e: Exception =>
					}
					println("Query canceled.")
				} else {
					Console.err.println(s"Query error: ${e.getMessage}")
				}
		}
	}

	private def clearScreen(): Unit = {
		System.out.print("\033[H\033[2J")
		System.out.flush()
	}

	private def readLine(): Seq[String] = {
		val sqls = new mutable.ArrayBuffer[String]()
		val sqlBuilder = new mutable.StringBuilder()
		var braceCount = 0
		var endLine = false
		var line = lineReader.readLine(prompter)
		while (!endLine) {
			line.trim.toCharArray.foreach { char =>
				if (char == DELIMITER && braceCount == 0) {
					sqls.append(sqlBuilder.toString())
					sqlBuilder.clear()
				} else {
					sqlBuilder.append(char)
					if (char == LEFT_BRACE) {
						braceCount += 1
					}
					if (char == RIGHT_BRACE) {
						braceCount -= 1
					}
				}
			}
			if (sqlBuilder.nonEmpty) {
				sqlBuilder.append(' ')
				line = lineReader.readLine(" " * (prompter.length - 2) + "| ").trim
			} else {
				endLine = true
			}
		}
		sqls
	}

	private def createConnection(): Unit = {
		try {
			val info = new Properties()
			info.put("user", user)
			info.put("password", password)
			info.put("appType", appType)
			info.put("fetchSize", fetchSize.toString)
			info.put("queryTimeout", timeout.toString)
			info.put("maxRows", maxRows.toString)
			appName.foreach(name => info.put("appName", name))
			info.putAll(handleExtraOptions(extraOptions).asJava)
			connection = DriverManager.getConnection(s"jdbc:moonbox://$host:$port/$database", info)
		} catch {
			case e: Exception =>
				if (connection != null && !connection.isClosed) {
					connection.close()
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

	private def closeConnection(): Unit = {
		try {
			if (connection != null) {
				connection.close()
			}
		} catch {
			case e: Exception =>
		}
	}

	private def reConnect(): Unit = {
		try {
			closeConnection()
			createConnection()
		} catch {
			case e: Exception => Console.err.println(e.getMessage)
		}
	}

	private def shutdown(): Unit = {
		try {
			closeConnection()
			Console.out.println("Bye!")
			System.exit(0)
		} catch {
			case e: Exception =>
				Console.err.println(e.getMessage)
				System.exit(-1)
		}
	}

	private def showConnectionState(): Unit = {
		val state = Seq(
			"established" :: !connection.isClosed :: Nil,
			"maxRows" :: maxRows :: Nil,
			"truncate" :: truncate :: Nil,
			"fetchSize" :: fetchSize :: Nil,
			"delimiter" :: DELIMITER :: Nil,
			"timeout" :: timeout :: Nil,
			"appType" :: appType :: Nil
		)
		val schema = "state_name" :: "value" :: Nil
		print(Utils.stringToShow(state, schema, maxRows, 0))
	}

	private def showCommands(): Unit = {
		val schema = Seq("command", "description")
		val data = Seq(ShowHistory, Reconnect, Exit, ShowState, ShowCommands,
			ShowHelp, SetTruncate, SetMaxRows, SetTimeout, SetFetchSize)
		    .map(c => Seq(c.command.mkString(" | "), c.description))
		print(Utils.stringToShow(data, schema))
	}

	private def showHelp(): Unit = {
		val schema = Seq("MQLs")
		val data = MQLs.MQL.map(_ :: Nil)
		print(Utils.stringToShow(data, schema))
	}

	private def checkArgs(): Unit = {
		if (user == null) {
			user = lineReader.readLine("username:")
		}
		if (password == null) {
			password = lineReader.readLine("Enter password:", new Character(0))
		}
	}

	@tailrec
	private def parseArgs(args: List[String]): Unit = args match {
		case ("-u" | "--user") :: value :: tail =>
			user = value
			parseArgs(tail)
		case ("-h" | "--host") :: value :: tail =>
			host = value
			parseArgs(tail)
		case ("-P" | "--port") :: IntParam(value) :: tail =>
			port = value
			parseArgs(tail)
		case ("-p" | "--password") :: value :: tail =>
			password = value
			parseArgs(tail)
		case ("-d" | "--database") :: value :: tail =>
			database = value
			parseArgs(tail)
		case ("-t" | "--timeout") :: IntParam(value) :: tail =>
			timeout = value
			parseArgs(tail)
		case ("-a" | "--apptype") :: value :: tail =>
			appType = value
			parseArgs(tail)
		case ("-n" | "--appname") :: value :: tail =>
			appName = Some(value)
			parseArgs(tail)
		case ("-f" | "--fetchsize") :: IntParam(value) :: tail =>
			fetchSize = value
			parseArgs(tail)
		case ("-e" | "--extraoptions") :: value :: tail =>
			extraOptions = value
			parseArgs(tail)
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
					"		-d, --database				Database to connect to.\n" +
					"		-a, --apptype  				Type of app to use for computing.\n" +
					"		-n, --appname					Name of app to use for computing. optional.\n" +
					"   -u, --user            User for login, the format is org@user.\n" +
					"   -p, --password        Password to use when connecting to server.\n" +
					"   -t, --timeout         The query timeout: seconds.\n" +
					"   -f, --fetchsize       The fetch size.\n" +
					"   -e, --extraoptions    The extra options: like \"k1=v1&k2=v2...\"\n" +
					"   --help"
		)
		System.exit(exitCode)
	}
}
