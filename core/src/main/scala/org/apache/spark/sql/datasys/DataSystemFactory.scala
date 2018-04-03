package org.apache.spark.sql.datasys

import org.apache.spark.sql.SparkSession

object DataSystemFactory {
	def getInstance(props: Map[String, String], sparkSession: SparkSession): DataSystem = {
		require(props.contains("type"))
		props("type").toLowerCase match {
			case "mysql" => new MysqlDataSystem(props)
			case _ => new SparkDataSystem(sparkSession)
		}
	}
}
