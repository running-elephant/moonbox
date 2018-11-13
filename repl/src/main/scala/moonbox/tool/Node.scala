package moonbox.tool

import com.typesafe.config.ConfigFactory
import moonbox.protocol.client._
import moonbox.repl.IntParam
import moonbox.repl.connector.rest.HttpClient
import org.apache.hadoop.yarn.api.records.ApplicationId
import org.apache.hadoop.yarn.client.api.YarnClient
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.json4s.jackson.Serialization.read
import moonbox.repl.Utils._
import scala.annotation.tailrec
import scala.collection.JavaConverters._
import org.json4s.DefaultFormats

object Node {

    val currentUser: String = System.getProperty("user.name")

    val (httpHost, httpPort) = getHttpHostAndPort
    var timeout: Int = 10 // unit: second

    var client: HttpClient = _
    val username = System.getProperty("user.name", "root")
    implicit val formats = DefaultFormats

    def doShowDatabases(user: String, password: String): ShowDatabasesOutbound = {
        val token = login(user, password)
        val dbs = showDatabases(token, true)
        logout(token)
        dbs
    }

    def doShowTables(user: String, password: String, database: String): ShowTablesOutbound = {
        val token = login(user, password)
        val tbs = showTables(token, database)
        logout(token)
        tbs
    }

    def doDescTables(user: String, password: String, table: String, database: String): DescribeTablesOutbound = {
        val token = login(user, password)
        val tbs = descTable(token, table, database)
        logout(token)
        tbs
    }

    def showDatabases(token: String, detail: Boolean): ShowDatabasesOutbound = {
        timeout = 60
        client = new HttpClient(httpHost, httpPort, timeout * 1000)
        val _show = ShowDatabasesInbound(token)
        val res = client.post(_show, "/metadata/showDatabases")
        read[ShowDatabasesOutbound](res)
    }

    def showTables(token: String, database: String): ShowTablesOutbound = {
        timeout = 60
        client = new HttpClient(httpHost, httpPort, timeout * 1000)
        val _submit = ShowTablesInbound(token, database)
        val res = client.post(_submit, "/metadata/showTables")
        read[ShowTablesOutbound](res)
    }

    def descTable(token: String, table: String, database: String): DescribeTablesOutbound = {
        timeout = 60
        client = new HttpClient(httpHost, httpPort, timeout * 1000)
        val _submit = DescribeTablesInbound(token, table, database)
        val res = client.post(_submit, "/metadata/describeTable")
        read[DescribeTablesOutbound](res)
    }


    def login(user: String, password: String): String = {
        client = new HttpClient(httpHost, httpPort, timeout * 1000)
        val _login = LoginInbound(user, password)
        val res = client.post(_login, "/login")
        read[LoginOutbound](res).token.get
    }

    def logout(token: String): Option[String] = {
        client = new HttpClient(httpHost, httpPort, timeout * 1000)
        val _logout = LogoutInbound(token)
        val res = client.post(_logout, "/logout")
        read[LogoutOutbound](res).error
    }

    def submit(token: String, sqls: Seq[String], config: String): String = {
        timeout = 60
        client = new HttpClient(httpHost, httpPort, timeout * 1000)
        val _submit = BatchQueryInbound(token,  sqls, config)
        val res = client.post(_submit, "/submit")
        read[BatchQueryOutbound](res).jobId.get
    }

    def addYarn(config: String): AddAppOutbound = { //for adhoc app
        timeout = 60
        client = new HttpClient(httpHost, httpPort, timeout * 1000)
        val _login = AddAppInbound(username, config)
        val res = client.post(_login, "/addYarnApp")
        read[AddAppOutbound](res)
    }

    def removeYarnApp(id: String): RemoveAppOutbound = {
        client = new HttpClient(httpHost, httpPort, timeout * 1000)
        val _login = RemoveAppInbound(username, id)
        val res = client.post(_login, "/removeYarnApp")
        read[RemoveAppOutbound](res)
    }

    def showYarnApp(): ShowAppOutbound = {
        client = new HttpClient(httpHost, httpPort, timeout * 1000)
        val _login = ShowAppInbound(username)
        val res = client.post(_login, "/showYarnApp")
        read[ShowAppOutbound](res)
    }


    def showAppIdResult(appId: Option[String], error: Option[String]): Unit = {
        if(appId.isDefined) {
            println(appId.get)
        }
        if(error.isDefined) {
            println(error.get)
        }
    }


    def getAllAppInfo(map: Map[String, Any]): (Seq[String], Seq[Seq[String]]) = {
        val schema = Seq("appid", "name", "progress", "starttime", "endtime")
        var yarnClient: YarnClient = null
        try {
            val yarnConf = new YarnConfiguration
            map.foreach(elem => yarnConf.set(elem._1, elem._2.toString))

            yarnClient = YarnClient.createYarnClient
            yarnClient.init(yarnConf)
            yarnClient.start()

            val data = yarnClient.getApplications.asScala.map { app => Seq(app.getApplicationId.toString, app.getName, app.getProgress.toString, moonbox.common.util.Utils.formatDate(app.getStartTime), moonbox.common.util.Utils.formatDate(app.getFinishTime)) }

            (schema, data)
        } catch {
            case e: Exception => e.printStackTrace()
                (schema, Seq(Seq("", "", "", "")))
        } finally {
            if ( yarnClient != null ) { yarnClient.close() }
        }
    }

    def killApplication(map: Map[String, Any], appid: String): Boolean = {
        var yarnClient: YarnClient = null
        try {
            if (appid.split('_').length != 3) {
                throw new Exception(s"appid $appid format is not corrent")
            }
            val yarnConf = new YarnConfiguration
            map.foreach(elem => yarnConf.set(elem._1, elem._2.toString))

            yarnClient = YarnClient.createYarnClient
            yarnClient.init(yarnConf)
            yarnClient.start()

            val clusterTimestamp = appid.split('_')(1).toLong
            val id = appid.split('_')(2).toInt
            val applicationId = ApplicationId.newInstance(clusterTimestamp, id)
            yarnClient.killApplication(applicationId)
            true
        } catch {
            case e: Exception => e.printStackTrace()
                false
        } finally {
            if ( yarnClient != null ) { yarnClient.close() }
        }

    }

    def main(args: Array[String]) {
        doCommand(args.toList)
        System.exit(0)
    }

    def parse(value: String): Map[String, Any] = {
        if(value == null) {
            throw new Exception("Value is Null")
        }
        val b = scala.util.parsing.json.JSON.parseFull(value)
        b match {
            case Some(map: Map[String, Any]) => map
            case None => throw new Exception("Parsing failed")
            case other => throw new Exception("Unknown data structure: " + other)
        }
    }
    var config: String = _
    var table: String = _
    var database: String = _
    var user: String = _
    var password: String = _
    var token: String = _

    @tailrec
    private def doCommand(args: List[String]): Unit = args match {
        case ("-t" | "--timeout") :: IntParam(value) :: tail =>
            timeout = value
            doCommand(tail)
        case ("-c" | "--config") :: value :: tail =>
            config = value
            doCommand(tail)
        case ("-a" | "--addyarnapp") :: tail =>
            val rsp = addYarn(config)  //json string
            showAppIdResult(rsp.appId, rsp.error)
        case ("-r" | "--removeyarnapp") :: id :: tail =>
            val rsp = removeYarnApp(id)
            showAppIdResult(rsp.appId, rsp.error)
        case ("-sy" | "--showyarnapp") :: tail =>
            val rsp = showYarnApp()
            showDataResult(rsp.schema, rsp.data, rsp.error)
        case ("-l" | "--listyarnapp" ) :: tail =>
            val b = parse(config)  //json
            val rsp = getAllAppInfo(b)
            showDataResult(Option(rsp._1), Option(rsp._2), None)
        case ("-k" | "--killyarnapp" ) :: value :: tail =>
            val b = parse(config)  //json
            killApplication(b, value)
        case ("-sd" | "--showdatabases" ) :: tail =>
            val rsp = doShowDatabases(user, password)
            showDataResultJson(rsp)
        case ("-st" | "--showtables" ) :: tail =>
            val rsp = doShowTables(user, password, database)
            showDataResultJson(rsp)
        case ("-dt" | "--desctable" ) :: tail =>
            val rsp =  doDescTables(user, password, table, database)
            showDataResultJson(rsp)
        case ("-t" | "--table") :: value :: tail =>
            table = value
            doCommand(tail)
        case ("-d" | "--database") :: value :: tail =>
            database = value
            doCommand(tail)
        case ("-u" | "--user") :: value :: tail =>
            user = value
            doCommand(tail)
        case ("-p" | "--password") :: value :: tail =>
            password = value
            doCommand(tail)
        case ("--help") :: tail =>
            printUsageAndExit(0)
        case Nil =>
        case _ =>
            printUsageAndExit(1)
    }


    /**
    *(SINGLE CONFIG): {"yarn.resourcemanager.address", "host:port"
    *          "yarn.resourcemanager.hostname", "host"}
    *(HA CONFIG):{"yarn.resourcemanager.ha.enabled", "true"
    *          "yarn.resourcemanager.ha.rm-ids", "rm1,rm2"
    *          "yarn.resourcemanager.hostname.rm1", "myhost1"
    *          "yarn.resourcemanager.hostname.rm2", "myhost2"}
    */
    private def printUsageAndExit(exitCode: Int): Unit = {
        // scalastyle: off println
        System.err.println(
            "Usage: moonbox [options]\n" +
                    "options:\n" +
                    "   -t, --timeout  1000            Timeout for http request \n" +
                    "   -a, --addyarnapp config        Add Yarn App to this node\n" +
                    "   -r, --removeyarnapp id         Remove Yarn App to this node.\n" +
                    "   -sy, --showyarnapp             Show all yarn app in this node.\n" +
                    "   -c config -l, --listyarnapp    Show all yarn app by config \n" +
                    "   -c config -k, --killyarnapp    Kill Yarn App Id by config \n" +
                    "   -sd, --showdatabases           Show databases \n"+
                    "   -st, --showtables              Show tables in database" +
                    "   -dt, --desctable               Describe one table in database" +
                    "   --help"
        )
        System.exit(exitCode)
    }

}

