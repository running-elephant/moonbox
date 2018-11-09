package moonbox.tool

import org.scalatest.FunSuite

class NodeSuite extends FunSuite {
    test("show") {
        val ret = Node.showYarnApp()
        //TABLE: id, yarnId, path, core, memory, freeCore, freeMemory, heartBeat, submitted
        //(app-20181006195520-00000, application_1538469632641_0004, akka.tcp://YarnAppSystem@kitty:34185/user/YarnAppActor, 1, 481925528, 1, 481925528, 2018-10-06 19:56:33, 2018-10-06 19:55:20)
        println(ret)
    }

    test("remove") {
        val ret = Node.removeYarnApp("app-20181108190544-00000_test-ddd_adhoc")
        println(ret)
    }

    test("showDatabases") {
        val token = Node.login("sally", "123456")
        val rsp = Node.showDatabases(token, true)
        println(rsp)

    }

    test("showTables") {
        val token = Node.login("sally", "123456")
        val rsp = Node.showTables(token, "default")
        println(rsp)
    }

    test("descTable") {
        val token = Node.login("sally", "123456")
        val rsp = Node.descTable(token, "mysql_test_booklist", "default")
        println(rsp)
    }

    test("batch submit") {
        val config =
            """
              |{       name = "test-ccc"
              |        spark.master = "yarn"
              |        spark.submit.deployMode = "cluster"
              |        spark.yarn.resourcemanager.address = "kitty:8032"
              |        spark.yarn.resourcemanager.hostname = "kitty"
              |        spark.yarn.access.namenodes = "hdfs://kitty:8020"
              |        spark.loglevel = "INFO"
              |        spark.app.name = "test1"
              |        spark.cores.max = 1
              |        spark.yarn.am.memory = "64m"
              |        spark.yarn.am.cores = 1
              |        spark.executor.instances = 1
              |        spark.executor.cores = 1
              |        spark.executor.memory = "512m"
              |        pushdown.enable = true
              |        job.mode = "batch"
              |      }
            """.stripMargin

        val token = Node.login("sally", "123456")
        val ret = Node.submit(token, Seq("select * from mysql_test_booklist"), config)

        println(ret)
    }

    test("add") {
        val config =
            """
              |{       implementation = "spark"
              |        name = "test-ddd"
              |        spark.master = "yarn"
              |        spark.submit.deployMode = "cluster"
              |        spark.yarn.resourcemanager.address = "kitty:8032"
              |        spark.yarn.resourcemanager.hostname = "kitty"
              |        spark.yarn.access.namenodes = "hdfs://kitty:8020"
              |        spark.loglevel = "INFO"
              |        spark.app.name = "test1"
              |        spark.cores.max = 1
              |        spark.yarn.am.memory = "64m"
              |        spark.yarn.am.cores = 1
              |        spark.executor.instances = 1
              |        spark.executor.cores = 1
              |        spark.executor.memory = "512m"
              |        pushdown.enable = true
              |        job.mode = "adhoc"
              |      }
            """.stripMargin

        val ret = Node.addYarn(config)

        println(ret)
    }

    test("list") {
        val config =
            """
              |{"yarn.resourcemanager.address": "master:8032",
              | "yarn.resourcemanager.hostname": "master"}
            """.stripMargin
        val b = Node.parse(config)
        val ret = Node.getAllAppInfo(b)
        println(ret)
    }

    test("kill") {
        val config =
            """
              |{"yarn.resourcemanager.address": "kitty:8032",
              | "yarn.resourcemanager.hostname": "kitty"}
            """.stripMargin
        val b = Node.parse(config)
        val ret = Node.killApplication(b, "application_1539572721220_0008")
        println(ret)
    }


}
