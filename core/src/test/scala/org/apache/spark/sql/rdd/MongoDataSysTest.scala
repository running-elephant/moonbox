package org.apache.spark.sql.rdd

import java.util.Properties

import com.mongodb.spark.MongoSpark
import com.mongodb.{ConnectionString, MongoClient, MongoClientURI}
import moonbox.catalyst.core.parser.SqlParser
import org.apache.spark.sql.datasys.MongoDataSystem
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.scalatest.FunSuite

import scala.collection.JavaConverters._

class MongoDataSysTest extends FunSuite {

  test("buildScan") {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("mongo-spark")
      .config("spark.mongodb.input.uri", "mongodb://yan:123456@localhost:27017/test.books?authSource=test")
      .config("spark.mongodb.output.uri", "mongodb://yan:123456@localhost:27017/test.books?authSource=test")
      .getOrCreate()
    val df = MongoSpark.load(spark)
    df.createOrReplaceTempView("books")

    val map = Map(
      "input.uri" -> "mongodb://yan:123456@localhost:27017/test.books?authSource=test",
      "output.uri" -> "mongodb://yan:123456@localhost:27017/test.books?authSource=test"
    )
    val df1 = spark.sql("select * from books limit 10")
    val planBySpark = df1.queryExecution.optimizedPlan
    new MongoDataSystem(map)(spark).buildScan(planBySpark).show()
  }

  test("build query") {
    val sql = "select name, price, author, pages from books limit 20"
    val uri = "mongodb://yan:123456@localhost:27017/test?authSource=test"
    val props = new Properties()
    props.setProperty("input.uri", uri)
    props.setProperty("input.database", "test")
    props.setProperty("input.collection", "books")
    val parser = new SqlParser()
    val dataSys = new MongoDataSystem(props.asScala.toMap)(null)
    val executor = dataSys.readExecutor
    executor.client.client.getDatabase("zhicheng").listCollectionNames().asScala.foreach(println)
    val schema = executor.getTableSchema
    val tableName = dataSys.readExecutor.client.collectionName
    parser.registerTable(tableName, schema, "mongo")
    executor.adaptorFunctionRegister(parser.getRegister)
    val dataTable = dataSys.buildQuery(parser.parse(sql))
    dataTable.foreach(r => println(r))
  }

  test("insert test") {
    val sql = "select name, price, author, pages from books limit 20"
    val inputUri = "mongodb://yan:123456@localhost:27017/test?authSource=test"
    val outputUri = "mongodb://yan:123456@localhost:27017/zhicheng?authSource=test"
    val props = new Properties()
    props.setProperty("input.uri", inputUri)
    props.setProperty("output.uri", outputUri)
    props.setProperty("input.database", "test")
    props.setProperty("output.database", "zhicheng")
    props.setProperty("input.collection", "books")
    props.setProperty("output.collection", "tt")
    val parser = new SqlParser()
    val dataSys = new MongoDataSystem(props.asScala.toMap)(null)
    val executor = dataSys.readExecutor
    executor.client.client.getDatabase("zhicheng").listCollectionNames().asScala.foreach(println)
    val schema = executor.getTableSchema
    val tableName = dataSys.readExecutor.client.collectionName
    parser.registerTable(tableName, schema, "mongo")
    executor.adaptorFunctionRegister(parser.getRegister)
    val plan = parser.parse(sql)
    val dataTable = dataSys.buildQuery(plan)
    //    dataTable.foreach(r => println(r))
    dataSys.insert(dataTable, SaveMode.ErrorIfExists)
  }

  test("truncate test") {
    val prop = new Properties()
    prop.setProperty("input.uri", "mongodb://yan:123456@localhost:27017/zhicheng?authSource=test")
    prop.setProperty("output.uri", "mongodb://yan:123456@localhost:27017/zhicheng?authSource=test")
    prop.setProperty("input.database", "zhicheng")
    prop.setProperty("output.database", "zhicheng")
    prop.setProperty("input.collection", "tt")
    prop.setProperty("output.collection", "tt")
    new MongoDataSystem(prop.asScala.toMap)(null).truncate()
  }

  test("tableNames") {
    val prop = new Properties()
    prop.setProperty("input.uri", "mongodb://yan:123456@localhost:27017/zhicheng?authSource=test")
    prop.setProperty("output.uri", "mongodb://yan:123456@localhost:27017/zhicheng?authSource=test")
    prop.setProperty("input.database", "zhicheng")
    prop.setProperty("output.database", "zhicheng")
    prop.setProperty("input.collection", "tt")
    prop.setProperty("output.collection", "tt")
    val names = new MongoDataSystem(prop.asScala.toMap)(null).tableNames()
    names.foreach(println)
  }

  test("tableProperties") {
    val prop = new Properties()
    prop.setProperty("input.uri", "mongodb://yan:123456@localhost:27017/zhicheng?authSource=test")
    prop.setProperty("output.uri", "mongodb://yan:123456@localhost:27017/zhicheng?authSource=test")
    prop.setProperty("input.database", "zhicheng")
    prop.setProperty("output.database", "zhicheng")
    val names = new MongoDataSystem(prop.asScala.toMap)(null).tableProperties("tt")
    names.foreach(println)
  }

  test("mongo spark with authentication") {
    val spark = SparkSession.builder().master("local[*]").appName("mongo-spark-auth")
      .config("spark.mongodb.input.uri", "mongodb://yan:123456@localhost:27017/test?authSource=test")
      .config("spark.mongodb.input.database", "zhicheng") // this config overrides the database in the uri
      .config("spark.mongodb.input.collection", "bigdecimal")
      .getOrCreate()
    val df = MongoSpark.load(spark)
    df.show()
  }

  test("mongo uri test") {
    val inputUri = "mongodb://yan:123456@localhost:27017/zhicheng"
    val connectionString = new ConnectionString(inputUri)
    connectionString.getDatabase
    connectionString.getCollection
    connectionString.getUsername
    connectionString.getPassword
    connectionString.getCredential
    connectionString.getConnectionString
    val cli = new MongoClient(new MongoClientURI(inputUri))
    val db = cli.getDatabase("test").getCollection("books")
    println(db)
  }

}
