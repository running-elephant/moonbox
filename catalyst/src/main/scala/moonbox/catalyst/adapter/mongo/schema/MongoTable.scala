package moonbox.catalyst.adapter.mongo.schema

import com.mongodb.MongoClient
import moonbox.catalyst.core.TableMetaData
import org.apache.spark.sql.types.StructType

class MongoTable(client: MongoClient, database: String, table: String) extends TableMetaData {
  var mongoSchemaInfer = new MongoSchemaInfer

  lazy val schema: StructType = mongoSchemaInfer.inferSchema(client, database, table)

  override def getTableSchema: StructType = this.schema

  def getTableSchema(sampleSize: Int) = {
    new MongoSchemaInfer(sampleSize).inferSchema(client, database, table)
  }

  override def getTableStats = (0L, 0L)
}
