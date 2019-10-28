package org.apache.spark.sql.datasys

import java.net.URI
import java.util.Locale

import org.apache.hadoop.hdfs.DFSConfigKeys

import scala.collection.mutable

object HadoopFsBaseDataSystem {

  val REMOTE_HA_CLUSTER = "remotehacluster"
  val NAMENODES = "namenodes"
  val PATH = "path"
  val REMOTE_DFS_CLIENT_FAILOVER_PROXY_PROVIDER = "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredRemoteFailoverProxyProvider"

  val remoteHaKeys = Seq(REMOTE_HA_CLUSTER, NAMENODES, PATH)

  val SPARK_HADOOP_KEY_PREFIX = "spark.hadoop."

  val PARQUET = "parquet"
  val TEXT = "text"
  val UMS = "ums"
  val CSV = "csv"
  val HIVE = "hive"

  val dsTypes = Seq(PARQUET, TEXT, UMS, CSV, HIVE)

  def mergeRemoteHadoopConf(props: Map[String, String]): Map[String, String] = {
    val hadoopConfMap = mutable.HashMap.empty[String, String]
    if (isHadoopFsType(props("type"))) {
      isRemoteHaCluster(props) match {
        case true =>
          checkRemoteOptions(props)
          val nameService = new URI(props(PATH)).getHost
          val nameNodes = props(NAMENODES).split(",")
          hadoopConfMap.put(SPARK_HADOOP_KEY_PREFIX + DFSConfigKeys.DFS_HA_NAMENODES_KEY_PREFIX + "." + nameService, "nn1,nn2")
          hadoopConfMap.put(SPARK_HADOOP_KEY_PREFIX + DFSConfigKeys.DFS_NAMENODE_RPC_ADDRESS_KEY + "." + nameService + ".nn1", nameNodes.head.trim)
          hadoopConfMap.put(SPARK_HADOOP_KEY_PREFIX + DFSConfigKeys.DFS_NAMENODE_RPC_ADDRESS_KEY + "." + nameService + ".nn2", nameNodes.last.trim)
          hadoopConfMap.put(SPARK_HADOOP_KEY_PREFIX + DFSConfigKeys.DFS_CLIENT_FAILOVER_PROXY_PROVIDER_KEY_PREFIX + "." + nameService, REMOTE_DFS_CLIENT_FAILOVER_PROXY_PROVIDER)
        case false => //good
      }
    }
    hadoopConfMap.toMap ++ props
  }

  def isHadoopFsType(dsType: String): Boolean = {
    dsTypes.contains(dsType.trim.toLowerCase(Locale.ROOT))
  }

  private def isRemoteHaCluster(props: Map[String, String]): Boolean = {
    props.get(REMOTE_HA_CLUSTER).exists(_.toBoolean)
  }

  private def checkRemoteOptions(props: Map[String, String]): Unit = {
    val message = s"${remoteHaKeys.mkString(", ")} must be in options."
    require(remoteHaKeys.forall(props.contains), message)
    val uri = new URI(props(PATH))
    require(uri.getScheme == "hdfs", "path must be hdfs system path.")
    require(uri.getHost == uri.getHost, "hdfs path must contains dfs nameservices name.")
  }

}