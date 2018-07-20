package moonbox.repl

import java.io.{PrintWriter, StringWriter}
import java.util.Locale

import moonbox.repl.adapter.{Connector, JdbcConnector, Utils}
import moonbox.repl.http.MbHttpConnector
import org.jline.reader.{LineReader, LineReaderBuilder, UserInterruptException}
import org.jline.reader.impl.completer.StringsCompleter
import org.jline.terminal.{Terminal, TerminalBuilder}
import org.jline.terminal.Terminal.Signal
import org.jline.terminal.Terminal.SignalHandler

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.language.implicitConversions

object Main extends JsonSerializer {

  var method: String = "rest"
  var retryTimes: Int = 3
  var timeout: Int = 300 // unit: second
  var user: String = _
  var host: String = "localhost"
  var port: Int = 9099
  var password: String = _
  val DELIMITER: String = ";"
  val PARAMETER_PREFIX: String = "%SET "
  val historySqls: mutable.Queue[String] = new mutable.Queue[String]()
  var connector: Connector = _
  val handler = new SignalHandler() { //create Ctrl+C handler, NOTE: sun misc handler dose not work
    override def handle(signal: Signal): Unit = {
      if (signal.equals(Signal.INT)) {
        new Thread() {
          override def run() = {
            if (connector != null) {
              connector.cancel()
            }
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
          var line: String = lineReader.readLine(prompter).trim
          val sqlList: ArrayBuffer[String] = new ArrayBuffer[String]()
          var braceCount: Int = 0
          line.length match {
            case len if len > 0 =>
              var endLine = false
              while (!endLine) {
                line.toCharArray.foreach {
                  case ';' if braceCount == 0 && stringBuilder.nonEmpty =>
                    sqlList += stringBuilder.toString()
                    stringBuilder.clear()
                  case other =>
                    stringBuilder.append(other)
                    other match {
                      case '(' => braceCount += 1
                      case ')' => braceCount -= 1
                      case _ =>
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
                  }
                }
              }
            case _ => /* no-op */
          }
          val cleanedSqls = sqlList.map(_.trim).filterNot(_ == "")
          if (cleanedSqls.nonEmpty) {
            /* add line reader history */
            lineReader.getHistory.add(cleanedSqls.mkString("; "))
            /* 100 history SQLs limited */
            while (historySqls.length > 100) {
              historySqls.dequeue()
            }
            process(cleanedSqls)
          }
        } catch {
          case _: UserInterruptException =>
            if (connector != null) {
              connector.cancel()
            }
          case e: Exception =>
            val stringWriter = new StringWriter()
            e.printStackTrace(new PrintWriter(stringWriter))
            System.out.println(stringWriter.toString)
        }
      }
    } else {
      retryTimes -= 1
      System.out.println("retry ...")
    }
  }

  private def processParams(kv: String, sqlList: Seq[String]): Unit = {
    val idx = kv.indexOf("=")
    if (idx != -1) {
      try {
        val k = kv.substring(0, idx).trim
        val v = kv.substring(idx + 1).trim
        k match {
          case "MAX_COUNT" => connector.max_count = v.toInt
          case "TRUNCATE" => connector.truncate = v.toInt
          case other => Console.err.println(s"Unknown parameter key: $other")
        }
      } catch {
        case _: NumberFormatException => Console.err.println("Value format invalid: should be Int.")
        case _: IndexOutOfBoundsException => Console.err.println("Invalid parameter set statement.")
        case other: Exception => throw other
      }
    } else {
      connector.process(sqlList)
    }
  }

  private def process(sqlList: Seq[String]): Unit = {
    sqlList.head.toUpperCase(Locale.ROOT) match {
      case "" =>
      case stmt if stmt.startsWith(PARAMETER_PREFIX) =>
        sqlList.foreach { sql =>
          historySqls.enqueue(sql)
        }
        processParams(stmt.stripPrefix(PARAMETER_PREFIX).trim, sqlList)
        val s = sqlList.tail
        if (s.nonEmpty) {
          connector.process(s)
        }
      case "HISTORY" | "H" =>
        val data = historySqls.zipWithIndex.map(u => Seq(historySqls.length - u._2, u._1 + ";"))
        print(Utils.showString(data, Seq("ID", "HISTORY MQLs"), 100))
      case "RECONNECT" | "R" =>
        connector.close()
        repl()
      case "EXIT" | "QUIT" | "Q" =>
        connector.shutdown()
        System.exit(0)
      case _ =>
        /* add moonbox history SQLs */
        sqlList.foreach { sql =>
          historySqls.enqueue(sql)
        }
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
