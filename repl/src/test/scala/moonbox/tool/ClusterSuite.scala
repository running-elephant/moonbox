package moonbox.tool

import org.scalatest.FunSuite

class ClusterSuite extends FunSuite {
    test("show nodes") {
        val ret = Cluster.showNodesInfo()
          println(ret)
    }

    test("show running events") {
        val ret = Cluster.showRunningEvents()
        println(ret)
    }

    test("show cluster jobs") {
        val ret = Cluster.showClusterJobs()
        println(ret)
    }

    test("show node jobs") {
        val ret = Cluster.showNodeJobs()
        println(ret)
    }


}
