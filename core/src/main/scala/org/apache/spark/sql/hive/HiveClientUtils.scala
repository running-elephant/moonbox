package org.apache.spark.sql.hive

import java.util.concurrent.ConcurrentHashMap

import org.apache.spark.SparkConf
import org.apache.spark.deploy.SparkHadoopUtil
import org.apache.spark.sql.hive.client.HiveClient

object HiveClientUtils {
	private val clients = new ConcurrentHashMap[String, HiveClient]()

	def getHiveClient(props: Map[String, String]) = {
		Option(clients.get(props("metastore.url"))).getOrElse {
			val sparkConf = new SparkConf()
				.set("spark.hadoop.javax.jdo.option.ConnectionURL",props("metastore.url"))
				.set("spark.hadoop.javax.jdo.option.ConnectionDriverName", props("metastore.driver"))
				.set("spark.hadoop.javax.jdo.option.ConnectionUserName", props("metastore.user"))
				.set("spark.hadoop.javax.jdo.option.ConnectionPassword", props("metastore.password"))
			val client = HiveUtils.newClientForMetadata(sparkConf, SparkHadoopUtil.get.newConfiguration(sparkConf))
			clients.put(props("metastore.url"), client)
			client
		}
	}
}
