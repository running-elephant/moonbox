package moonbox.repl

import moonbox.repl.adapter.{Connector, JdbcConnector, Utils}
import moonbox.repl.http.MbHttpConnector
import org.jline.reader.{LineReader, LineReaderBuilder, UserInterruptException}
import org.jline.reader.impl.completer.StringsCompleter
import org.jline.terminal.{Terminal, TerminalBuilder}
import org.jline.terminal.Terminal.Signal
import org.jline.terminal.Terminal.SignalHandler
import scala.annotation.tailrec
import scala.language.implicitConversions

object Main extends JsonSerializer {

  var method: String = "rest"
  var retryTimes: Int = 3
  var timeout: Int = 300 // unit: second
  var user: String = _
  var host: String = "localhost"
  var port: Int = 9099
  var password: String = _
  val delimiter: String = ";"
  var connector: Connector = _
  val handler = new SignalHandler() {  //create Ctrl+C handler, NOTE: sun misc handler dose not work
    override def handle(signal: Signal): Unit = {
      if(signal.equals(Signal.INT)) {
        new Thread() {
          override def run() = {
            if (connector != null) { connector.cancel() }
          }
        }.start()
      }
    }
  }

  val terminal: Terminal = TerminalBuilder.builder.signalHandler(handler).build()
  val autoCompleter = new StringsCompleter(MQLs.MQL.map(_.toLowerCase()): _*)
  val lineReader: LineReader = LineReaderBuilder.builder().terminal(terminal).completer(autoCompleter).parser(null).build()

  def main(args: Array[String]) {
    parse(args.toList)
    do {
      checkParameters()
      connector = if (method == "rest" || method == "r") {
        //new RestConnector(timeout)
        new MbHttpConnector(timeout)
      } else {
        new JdbcConnector(timeout)
      }
      repl()
    } while (retryTimes > 0)

    System.exit(-1)
  }

  def repl(): Unit = {
    if (connector.prepare(host, port, user, password, "default")) {
      while (true) {
        try {
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

          val multiLines = stringBuilder.toString().trim
          if (multiLines.length > 0) {
            lineReader.getHistory.add(multiLines)
          }

          process(multiLines.stripSuffix(delimiter)) //do process

          stringBuilder.clear()
        } catch {
          case _: UserInterruptException =>
            if (connector != null) {
              connector.cancel()
            }
          case e: Exception =>
            System.out.println(e.getMessage)
        }
      }
    } else {
      retryTimes -= 1
      System.out.println("retry ...")
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
        val sqlList = Utils.splitSql(trimmedSql, ';') //trimmedSql.split(";")
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
    case ("-t" | "--timeout") :: IntParam(value) :: tail =>
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
