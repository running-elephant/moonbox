package org.apache.spark.sql.datasys

import org.apache.spark.sql.SparkSession

object DataSystemFactory {
	def getInstance(props: Map[String, String], sparkSession: SparkSession): DataSystem = {
		require(props.contains("type"))
		props("type").toLowerCase match {
			case "mysql" => new MysqlDataSystem(props)(sparkSession)
			case "presto" | "prestodb" => new PrestoDataSystem(props)(sparkSession)
			case "es" | "elasticsearch" => new ElasticSearchDataSystem(props)(sparkSession)
			case "mongo" | "mongodb" => new MongoDataSystem(props)(sparkSession)
			case _ => new SparkDataSystem(sparkSession)
		}
	}

	def typeToSparkDatasource(typ: String): String = {
		typ match {
			case "mysql" | "oracle" | "jdbc" => "org.apache.spark.sql.execution.datasources.mbjdbc"
			case "presto" | "prestodb" => "org.apache.spark.sql.execution.datasources.presto"
			case "hbase" => "org.apache.spark.sql.execution.datasources.hbase"
			case "redis" => "org.apache.spark.sql.execution.datasources.redis"
			case "mongo" | "mongodb" => "com.mongodb.spark.sql"
			case "es" | "elasticsearch" => "org.elasticsearch.spark.sql"
			case "parquet" => "parquet"
			case "json" => "json"
			case "csv" => "csv"
			case "text" => "text"
			case o => o
		}
	}
}
