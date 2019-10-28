package moonbox.core


import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.datasys.HadoopFsBaseDataSystem
import org.scalatest.FunSuite

/**
  * Created by swallow on 2019/10/24.
  */
class ConfiguredRemoteFailoverProxyProviderSuite  extends FunSuite {

  test("hdfsHa") {

    val path = new Path("/user/wormhole")
    val conf = new Configuration()
//    conf.set("fs.defaultFS", "hdfs://edp")
//    conf.set("dfs.nameservices", "edp")
    conf.set("dfs.ha.namenodes.edp", "nn1,nn2")
    conf.set("dfs.namenode.rpc-address.edp.nn1", "hdp1:8020")
    conf.set("dfs.namenode.rpc-address.edp.nn2", "hdp2:8020")
    conf.set("dfs.client.failover.proxy.provider.edp", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredRemoteFailoverProxyProvider")

    val fs = path.getFileSystem(conf)

    fs.getChildFileSystems

  }

  test("uri") {
    val uri1 = new URI("hdfs://edp/4t5")
    val uri2 = new URI("hdfs://10:10/ret")
    val uri3 = new URI("//user/hdp")

    println()
  }

  test("ha") {
    val props: Map[String, String] = Map("remotehacluster" ->"true", "remote.dfs.nameservices" -> "edp", "namenodes" -> "hdp1:8020,hdp2:8020")
   val hadoop =  HadoopFsBaseDataSystem.mergeRemoteHadoopConf(props)
    println()
  }
}
