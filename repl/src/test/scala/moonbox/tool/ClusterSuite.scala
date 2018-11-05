package moonbox.tool

import org.scalatest.FunSuite

class ClusterSuite extends FunSuite {
    test("show") {
        val ret = Cluster.showNodesInfo()
          println(ret)
    }
}
