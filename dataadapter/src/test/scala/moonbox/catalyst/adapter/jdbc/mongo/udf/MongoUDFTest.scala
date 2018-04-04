package moonbox.catalyst.adapter.jdbc.mongo.udf

import com.mongodb.spark.MongoSpark
import moonbox.catalyst.core.parser.udf.udfParser.ParserUtil
import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterEach, FunSuite}

class MongoUDFTest extends FunSuite with BeforeAndAfterEach{
  val sparkSession = SparkSession.builder().master("local[*]").appName("mongoVisitorTest")
    .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/test")
    .config("spark.mongodb.input.collection", "author_withArray")
    .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/test")
    .config("spark.mongodb.output.collection", "author_withArray")
    .getOrCreate()

  val df = MongoSpark.load(sparkSession)
  df.createOrReplaceTempView("author_withArray")

  test("array udf"){
    sparkSession.sql(sqlTransform(sql)).show(false)
  }

  def sqlTransform(sql: String): String ={
    ParserUtil.sqlTransform(sql)
  }

//  val sql = """ select authorname, age, array_map(books.price, value => value + 1) from author_withArray"""
//  val sql = """ select authorname, age, books.price, array_map(books.price, value => value * 2 + 1) from author_withArray where array_exists(books.price, value => value > 5) OR array_exists(books.price, value => value < 3)"""
  val sql = """ select authorname, age, books.price, array_map(books.price, value => value * 2 + 1) from author_withArray where array_exists(array_map(books.price, value => value + 2), value => value > 5)"""

  override protected def beforeEach(): Unit = {
    new MbUDFRegister(sparkSession).register()
  }

  override protected def afterEach(): Unit = super.afterEach()
}
