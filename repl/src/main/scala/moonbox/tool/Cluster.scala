package moonbox.tool

import moonbox.protocol.client.{ShowNodesInfoInbound, ShowNodesInfoOutbound}
import moonbox.repl.connector.rest.HttpClient
import org.json4s.jackson.Serialization.read
import org.json4s.DefaultFormats
import moonbox.repl.Utils._

object Cluster {
    val currentUser: String = System.getProperty("user.name")

    val (httpHost, httpPort) = getHttpHostAndPort
    var timeout: Int = 10 // unit: second

    var client: HttpClient = _
    val username = System.getProperty("user.name", "root")
    implicit val formats = DefaultFormats

    def showNodesInfo(): ShowNodesInfoOutbound = {
        timeout = 60
        client = new HttpClient(httpHost, httpPort, timeout * 1000)
        val _login = ShowNodesInfoInbound(username)
        val res = client.post(_login, "/showNodesInfo")
        read[ShowNodesInfoOutbound](res)
    }

    //TODO: other rest api

    def main(args: Array[String]) {
        doCommand(args.toList)
        System.exit(0)
    }

    private def doCommand(args: List[String]): Unit = args match {
        case ("-s" | "--shownodesinfo") :: tail =>
            val rsp =showNodesInfo()
            showDataResult(rsp.schema, rsp.data, rsp.error)
        case _ =>
            printUsageAndExit(1)
    }

    private def printUsageAndExit(exitCode: Int): Unit = {
        // scalastyle: off println
        System.err.println(
            "Usage: moonbox [options]\n" +
                    "options:\n" +
                    "   -s, --shownodesinfo          Show all nodes info \n" +
                    "   --help"
        )
        System.exit(exitCode)
    }
}
