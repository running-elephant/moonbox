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

package moonbox.repl

import java.util.Locale

import moonbox.repl.connector.Connector
import moonbox.repl.connector.jdbc.JdbcConnector
import moonbox.repl.connector.rest.HttpConnector
import org.jline.reader.impl.LineReaderImpl
import org.jline.reader.impl.completer.StringsCompleter
import org.jline.reader.impl.history.DefaultHistory
import org.jline.reader.{LineReader, LineReaderBuilder, UserInterruptException}
import org.jline.terminal.Terminal.{Signal, SignalHandler}
import org.jline.terminal.{Terminal, TerminalBuilder}

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.language.implicitConversions

object Main {

  var method: String = "rest"
  var retryTimes: Int = 3
  var timeout: Int = 300 // unit: second
  var islocal: Boolean = true
  var user: String = _
  var host: String = "localhost"
  var port: Int = 9099
  var password: String = _
  val DELIMITER: String = ";"
  val PARAMETER_PREFIX: String = "%SET "
  val historyMqls: mutable.Queue[String] = new mutable.Queue[String]()
  val lineHistory: mutable.Queue[String] = new mutable.Queue[String]()
  val HISTORY_SIZE: Int = 100
  /* max number of history to maintain */
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
  System.setProperty("log4j.configuration", "") //close log4j print in repl

  def main(args: Array[String]) {
    parse(args.toList)
    do {
      checkParameters()
      connector = if (method == "rest" || method == "r") {
        //new RestConnector(timeout)
        new HttpConnector(timeout, islocal)
      } else {
        new JdbcConnector(timeout, islocal)
      }
      repl()
    } while (retryTimes > 0)

    System.exit(-1)
  }

  private def setHistory(lineReader: LineReaderImpl) = {
    lineReader.getHistory.purge()
    val h = new DefaultHistory()
    lineHistory.foreach(h.add)
    lineReader.setHistory(h)
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
          case _: UserInterruptException =>
            if (connector != null) {
              connector.cancel()
            }
          case e: Exception =>
            System.err.println(e.getMessage)
        }
      }
    } else {
      retryTimes -= 1
      System.out.println("retry ...")
    }
  }

  private def printSetHelp(): Unit = {
    val message =
      """
        |Use: %SET TRUNCATE=[Int]  /*Set the column length to truncate, 0 denotes unabridged*/
        |     %SET MAX_COUNT=[Int]  /*Set max rows to show in console*/
        |     %SET TIMEOUT=[Int]  /*Set timeout(second) of connection from moonbox server*/
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
          case "MAX_COUNT" => connector.setMaxRowsShow(v.toInt)
          case "TRUNCATE" => connector.setMaxColumnLength(v.toInt)
          case "TIMEOUT" => connector.setTimeout(v.toInt)
          case "FETCH_SIZE" => connector.setFetchSize(v.toInt)
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
        connector.process(s)
      }
    } else {
      connector.process(sqlList)
    }
  }

  private def enqueueWithLimit(queue: mutable.Queue[String], sqls: String*): Unit = {
    queue.enqueue(sqls: _*)
    /* The size of history SQLs limited */
    while (queue.length > HISTORY_SIZE) {
      queue.dequeue()
    }
  }

  private def process(sqlList: Seq[String]): Unit = {
    val compositedSql = sqlList.mkString(";")
    val headSql = sqlList.head
    headSql.toUpperCase(Locale.ROOT) match {
      case "" =>
      case stmt if stmt.startsWith(PARAMETER_PREFIX) =>
        enqueueWithLimit(historyMqls, compositedSql)
        processSetSqls(headSql, sqlList)
      case "HISTORY" | "H" =>
        val data = historyMqls.zipWithIndex.map(u => Seq(historyMqls.length - u._2, u._1 + ";"))
        print(Utils.showString(data, Seq("ID", "HISTORY MQLs"), HISTORY_SIZE))
      case "RECONNECT" | "R" => try connector.close() finally repl()
      case "EXIT" | "QUIT" | "Q" => try connector.shutdown() finally System.exit(0)
      case "STATE" | "STATUS" => connector.connectionState.prettyShow()
      case _ =>
        enqueueWithLimit(historyMqls, compositedSql)
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
    case ("-t" | "--timeout") :: IntParam(value) :: tail =>
      timeout = value
      parse(tail)
    case ("-r" | "--runtime") :: value :: tail =>
      islocal = value.equalsIgnoreCase("local")
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
//        "	  -z, --zookeeper Zookeeper quorum to get active master address.\n" +
        "   -u, --user :    User for login.\n" +
        "   --help"
    )
    System.exit(exitCode)
  }

}
