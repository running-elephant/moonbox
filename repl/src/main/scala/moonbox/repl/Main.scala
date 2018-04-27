package moonbox.repl

import moonbox.common.MbLogging
import moonbox.repl.adapter.{Connector, JdbcConnector, RestConnector}
import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.completer.StringsCompleter
import org.jline.terminal.TerminalBuilder

import scala.annotation.tailrec
import scala.language.implicitConversions

object Main extends JsonSerializer with MbLogging {

  var method: String = "rest"
  var retryTimes: Int = 3
  var timeout: Int = 300
  var user: String = _
  var host: String = "localhost"
  var port: Int = 9099
  var password: String = _
  val delimiter: String = ";"
  var connector: Connector = _
  val terminal = TerminalBuilder.builder /*.system(true).signalHandler(Terminal.SignalHandler.SIG_IGN)*/ .build()
  val autoCompleter = new StringsCompleter(MQLs.MQL.map(_.toLowerCase()): _*)
  val lineReader = LineReaderBuilder.builder().terminal(terminal).completer(autoCompleter).parser(null).build()

  def main(args: Array[String]) {
	  parse(args.toList)

    do {
      checkParameters()
      connector = if(method == "rest" || method == "r") {
        new RestConnector(timeout)
      }else{
        new JdbcConnector(timeout)
      }
      repl()
    } while (retryTimes > 0)

    System.exit(-1)
  }

	def repl(): Unit = {
		if (connector.prepare(host, port, user, password, "default")) {
			while (true) {
				val stringBuilder = new StringBuilder
				val line: String = lineReader.readLine(prompter, null, null, null).trim
				if (line.length > 0) {
          if (!line.endsWith(delimiter)) {
            stringBuilder.append(line)
            var endLine = false

            while (!endLine) {
              val line: String = lineReader.readLine(" " * (user.length - 1) + "->" + " ").trim
              if (line.length > 0)
                if (!line.endsWith(delimiter)) {
                  stringBuilder.append(" " + line)
                } else {
                  if (line.equals(";"))
                    stringBuilder.append(line)
                  else
                    stringBuilder.append(" " + line)
                  endLine = true
                }
            }
          } else {
            stringBuilder.append(line)
          }
        }

				try {
					val multiLines = stringBuilder.toString().trim
					if (multiLines.length > 0) {
            lineReader.getHistory.add(multiLines)
          }

					process(multiLines.stripSuffix(delimiter))  //do process

					stringBuilder.clear()
				} catch {
					case e: Exception =>
						System.out.println(e.getMessage)
				}
			}
		} else {
			retryTimes -= 1
			logInfo("retry ...")
		}
	}

  private def process(sqls: String): Unit = {
    sqls.trim match {
      case "" =>
      case "reconnect" | "r" =>
        connector.close()
        repl()
      case "exit" | "quit" =>
        connector.shutdown()
        System.exit(0)
      case trimmedSql =>
        val sqlList = trimmedSql.split(";")
        /*val illegal: Boolean = sqlList.map { sql =>
          val validate: Try[Boolean] = Validation.validate(sql)
          if (validate.isFailure) {
            System.out.println(validate.failed.get.getMessage)
            false
          } else {
            true
          }
        }.forall(_ == true)
        if (illegal) {
          connector.process(sqlList)
        }*/
		  connector.process(sqlList)
    }
  }

  private def prompter: String = s"$user> "

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
    case ("-m" | "--method") :: value :: tail =>
      method = value
      parse(tail)
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
    case ("-z" | "--zookeeper") :: value :: tail =>
      password = value
      parse(tail)
    case ("-t" | "--timeout"):: IntParam(value) :: tail =>
        timeout = value
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
      "Usage: moonbox [options]\n" +
        "options:\n" +
        "   -m, --method    JDBC or REST\n" +
        "   -h, --host      Connect to host.\n" +
        "   -P, --port      Port num to ues for connecting to server.\n" +
        "   -p, --password  Password to use when connecting to server.\n" +
		    "	  -z, --zookeeper Zookeeper quorum to get active master address.\n" +
        "   -u, --user :    User for login.\n" +
        "   --help"
    )
    System.exit(exitCode)
  }

}
