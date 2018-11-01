package moonbox.yarnapp

import java.net.InetAddress

import org.scalatest.FunSuite

class MbAppSuite extends FunSuite{

    test("start mb app actor in local") {
        val host = InetAddress.getLocalHost.getHostName
        val args = Seq("moonbox.mixcal.cluster.actor.path", s"akka.tcp://moonbox@$host:12551/user/MbClusterActor", //fake param1
            "moonbox.mixcal.cluster.yarn.id", "app-201810271148-001",  //fake param2
            "moonbox.grid.actor.yarn.on" , "false",
            "moonbox.mixcal.cluster.spark.app.name", "test1",   //for local test pass
            "moonbox.mixcal.clusterspark.master", "local[2]")  //for local test pass
        MbApp.runApp(args.toArray)
    }


}
